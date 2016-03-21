/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nutrisystem.orange.java.cache.UserProfileCache;
import com.nutrisystem.orange.java.cache.UserProfileHistoryCache;
import com.nutrisystem.orange.java.constant.AppConstant;
import com.nutrisystem.orange.java.constant.UserRole;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.mapper.UserProfileMapper;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Provides session id generation and verification
 * 
 * @author Wei Gao
 * 
 */
@Component
public class Session {
    private static final String USER_SESSION = AppConstant.NAMESPACE + "user-session:";

    private static final String SESSION = AppConstant.NAMESPACE + "session:";

    @Value("${max.user.sessions}")
    private int maxUserSessions;

    @Value("${session.timeout.minutes}")
    private int sessionTimeoutMinutes;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ValueOperations<String, String> stringValueOperations;

    @Autowired
    private SetOperations<String, String> stringSetOperations;

    @Autowired
    private UserProfileMapper userProfileMapper;

    @Autowired
    private UserProfileCache userProfileCache;

    @Autowired
    private UserProfileHistoryCache userProfileHistoryCache;

    @Autowired
    private UserProfileHistorySynchronizer userProfileHistorySynchronizer;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private Logger dataLogger;

    private void resetTimeOut(String sessionId) {
	stringRedisTemplate.expire(SESSION + sessionId, sessionTimeoutMinutes, TimeUnit.MINUTES);
    }

    public boolean exists(String sessionId) {
	try {
	    if (stringValueOperations.get(SESSION + sessionId) == null)
		return false;
	    else {
		resetTimeOut(sessionId);
		return true;
	    }
	} catch (Exception e) {
	    return isValidUserId(sessionId);
	}
    }

    private boolean isValidUserId(String userId) {
	    int id = 0;
	    try {
		id = Integer.parseInt(userId);
	    } catch (NumberFormatException e) {
	    }
	return userProfileRepository.findByUserId(id).size() == 1;
    }

    @Transactional
    public String create(String userId, String timeZone) {
	String sessionId = createSessionId();
	String sessionKey = SESSION + sessionId;
	String userSessionSetKey = USER_SESSION + userId;

	// log user sessions
	String data[] = new String[4];
	data[0] = getClass().getSimpleName();
	data[1] = sessionId;
	data[2] = userId;
	data[3] = DateUtil.getDateTimeString(new Date());
	dataLogger.info(StringUtils.arrayToDelimitedString(data, AppConstant.DELIMITER));

	try {
	    // delete timeout sessions
	    Set<String> sessionKeyList = stringSetOperations.members(userSessionSetKey);
	    for (String skey : sessionKeyList) {
		if (stringRedisTemplate.getExpire(skey) <= 0) {
		    stringRedisTemplate.delete(skey);
		    stringSetOperations.remove(userSessionSetKey, skey);
		}
	    }
	    // keep number of user sessions <= max number of user sessions
	    while (stringSetOperations.size(userSessionSetKey) > maxUserSessions) {
		sessionKeyList = stringSetOperations.members(userSessionSetKey);

		long minTTL = Long.MAX_VALUE;
		String minTTLSessionKey = "";
		for (String skey : sessionKeyList) {
		    if (stringRedisTemplate.getExpire(skey) < minTTL) {
			minTTLSessionKey = skey;
			minTTL = stringRedisTemplate.getExpire(skey);
		    }
		}
		if (stringSetOperations.size(userSessionSetKey) > maxUserSessions) {
		    stringRedisTemplate.delete(minTTLSessionKey);
		    stringSetOperations.remove(userSessionSetKey, minTTLSessionKey);
		}
	    }
	    stringSetOperations.add(userSessionSetKey, sessionKey);
	    stringValueOperations.set(sessionKey, userSessionSetKey, sessionTimeoutMinutes, TimeUnit.MINUTES);
	} catch (Exception e) {
	    e.printStackTrace();
	    sessionId = userId;
	}

	// cache user data
	cacheByUser(userId);
	return sessionId;
    }

    public void cache(String sessionId, String userId, String timeZone) {
	String userIdString;
	if (userId == null)
	    userIdString = getUserId(sessionId);
	else
	    userIdString = userId;

	userProfileHistorySynchronizer.sync(Integer.parseInt(userIdString), timeZone);
	cacheByUser(userIdString);
    }

    private void cacheByUser(String userId) {
	userProfileCache.save(userId);
	userProfileHistoryCache.save(userId);
    }

    public boolean delete(String sessionId) {
	if (sessionId == null)
	    return false;

	String userId = getUserId(sessionId);
	String sessionKey = SESSION + sessionId;
	String userSessionSetKey = USER_SESSION + getUserId(sessionId);
	stringRedisTemplate.delete(sessionKey);
	stringSetOperations.remove(userSessionSetKey, sessionKey);
	if (stringSetOperations.size(userSessionSetKey) == 0) {
	    stringRedisTemplate.delete(userSessionSetKey);
	    userProfileCache.delete(userId);
	    userProfileHistoryCache.delete(userId);
	}
	return true;
    }

    public String getUserId(String sessionId) {
	try {
	    return exists(sessionId) ? stringValueOperations.get(SESSION + sessionId).replaceFirst(USER_SESSION, "")
		    : null;
	} catch (Exception e) {
	    return isValidUserId(sessionId) ? sessionId : null;
	}
    }

    public boolean isAdmin(String sessionId) {
	String userIdString = getUserId(sessionId);
	if (userIdString == null)
	    return false;

	UserProfile userProfile = (UserProfile) userProfileCache.read(userIdString);
	return userProfile.getRole().equals(UserRole.ADMIN) || userProfile.getRole().equals(UserRole.SUPER_ADMIN);
    }

    public boolean hasUser(Integer userId) {
	return userProfileRepository.hasUser(userId);
    }

    private String createSessionId() {
	return UUID.randomUUID().toString();
    }
}
