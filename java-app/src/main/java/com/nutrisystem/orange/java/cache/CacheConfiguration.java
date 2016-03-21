/**
 * 
 */
package com.nutrisystem.orange.java.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.nutrisystem.orange.java.constant.SequenceName;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyapp.UserProfileHistory;
import com.nutrisystem.orange.java.entity.diyapp.Zipcode;
import com.nutrisystem.orange.java.mapper.UserProfileMapper;
import com.nutrisystem.orange.java.repository.app.UserProfileHistoryRepository;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;
import com.nutrisystem.orange.java.repository.app.ZipcodeRepository;
import com.nutrisystem.orange.java.ws.helper.GoogleService;

/**
 * @author Wei Gao
 * 
 */
@Configuration
public class CacheConfiguration {
	@Value("${glassfish.admin}")
	private String glassfishAdmin;

	@Value("${glassfish.password}")
	private String glassfishPassword;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserProfileHistoryRepository userProfileHistoryRepository;

	@Autowired
	private UserProfileMapper userProfileMapper;

	@Autowired
	private ZipcodeRepository zipcodeRepository;

	@Autowired
	private GoogleService googleService;

	@Bean
	ZipcodeCache zipcodeCache() {
		ZipcodeCache zipcodeCache = new ZipcodeCache();
		zipcodeCache.setEntityClassName(Zipcode.class.getSimpleName());
		zipcodeCache.setZipcodeRepository(zipcodeRepository);
		zipcodeCache.setRedisTemplate(redisTemplate);
		zipcodeCache.setValueOperations(valueOperations);
		zipcodeCache.setGoogleService(googleService);
		return zipcodeCache;
	}

	@Bean
	UserProfileCache userProfileCache() {
		UserProfileCache userProfileCache = new UserProfileCache();
		userProfileCache.setEntityClassName(UserProfile.class.getSimpleName());
		userProfileCache.setUserProfileRepository(userProfileRepository);
		userProfileCache.setRedisTemplate(redisTemplate);
		userProfileCache.setValueOperations(valueOperations);
		return userProfileCache;
	}

	@Bean
	UserProfileHistoryCache userProfileHistoryCache() {
		UserProfileHistoryCache userProfileHistoryCache = new UserProfileHistoryCache();
		userProfileHistoryCache.setEntityClassName(UserProfileHistory.class.getSimpleName());
		userProfileHistoryCache.setUserProfileHistoryRepository(userProfileHistoryRepository);
		userProfileHistoryCache.setUserProfileMapper(userProfileMapper);
		userProfileHistoryCache.setRedisTemplate(redisTemplate);
		userProfileHistoryCache.setValueOperations(valueOperations);
		return userProfileHistoryCache;
	}

	@Bean(initMethod = "init", destroyMethod = "cleanup")
	DistributedCache distributedCache() {
		DistributedCache distributedCache = new DistributedCache();
		distributedCache.setTestSequenceName(SequenceName.FOODLOGID_SEQUENCE);
		DistributedCache.setUSERNAME(glassfishAdmin);
		DistributedCache.setPASSWORD(glassfishPassword);
		return distributedCache;
	}

}
