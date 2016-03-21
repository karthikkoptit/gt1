/**
 * 
 */
package com.nutrisystem.orange.java.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.nutrisystem.orange.java.cache.DistributedCache;
import com.nutrisystem.orange.java.constant.SequenceName;
import com.nutrisystem.orange.java.repository.app.ActivityLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodLogRepository;
import com.nutrisystem.orange.java.repository.app.UserProfileHistoryRepository;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;
import com.nutrisystem.orange.java.ws.helper.Redis;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class SequenceConfiguration {
    @Autowired
    private FoodLogRepository foodLogRepository;
    
    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    @Autowired
    private UserProfileRepository userProfileRepository;
    
    @Autowired
    private UserProfileHistoryRepository userProfileHistoryRepository;
    
    @Autowired
    private Redis redis;
    
    @Autowired
    private DistributedCache distributedCache;
    
    @Autowired
    private ValueOperations<String, String> stringValueOperations;
    
    @Autowired 
    private JdbcTemplate jdbcTemplateApp;
    
    @Bean(initMethod="afterPropertiesSet") 
    public SequenceContext foodLogIdSequenceContext() {
	SequenceContext sequenceContext = new SequenceContext();
	sequenceContext.setDbSequenceState(foodLogIdDBSequence());
	sequenceContext.setRedisSequenceState(foodLogIdRedisSequence());
	return sequenceContext;
    }
    
    @Bean(initMethod="afterPropertiesSet") 
    public SequenceContext activityLogIdSequenceContext() {
	SequenceContext sequenceContext = new SequenceContext();
	sequenceContext.setDbSequenceState(activityLogIdDBSequence());
	sequenceContext.setRedisSequenceState(activityLogIdRedisSequence());
	return sequenceContext;
    }

    @Bean(initMethod="afterPropertiesSet") 
    public SequenceContext userProfileIdSequenceContext() {
	SequenceContext sequenceContext = new SequenceContext();
	sequenceContext.setDbSequenceState(userProfileIdDBSequence());
	sequenceContext.setRedisSequenceState(userProfileIdRedisSequence());
	return sequenceContext;
    }

    @Bean(initMethod="afterPropertiesSet")
    public SequenceContext userProfileHistoryIdSequenceContext() {
	SequenceContext sequenceContext = new SequenceContext();
	sequenceContext.setDbSequenceState(userProfileHistoryIdDBSequence());
	sequenceContext.setRedisSequenceState(userProfileHistoryIdRedisSequence());
	return sequenceContext;
    }

    @Bean(initMethod="afterPropertiesSet")
    public RedisSequenceState foodLogIdRedisSequence() {
	RedisSequenceState redisSequence = new RedisSequenceState();
	redisSequence.setSequenceName(SequenceName.FOODLOGID_SEQUENCE);
	redisSequence.setDistributedCache(distributedCache);
	redisSequence.setValueOperations(stringValueOperations);
	redisSequence.setDbSequenceState(foodLogIdDBSequence());
	return redisSequence;
    }
    
    @Bean(initMethod="afterPropertiesSet")
    public RedisSequenceState activityLogIdRedisSequence() {
	RedisSequenceState redisSequence = new RedisSequenceState();
	redisSequence.setSequenceName(SequenceName.ACTIVITYLOGID_SEQUENCE);
	redisSequence.setDistributedCache(distributedCache);
	redisSequence.setValueOperations(stringValueOperations);
	redisSequence.setDbSequenceState(activityLogIdDBSequence());
	return redisSequence;
    }
    
    @Bean(initMethod="afterPropertiesSet")
    public RedisSequenceState userProfileIdRedisSequence() {
	RedisSequenceState redisSequence = new RedisSequenceState();
	redisSequence.setSequenceName(SequenceName.USERPROFILEID_SEQUENCE);
	redisSequence.setDistributedCache(distributedCache);
	redisSequence.setValueOperations(stringValueOperations);
	redisSequence.setDbSequenceState(userProfileIdDBSequence());
	return redisSequence;
    }    

    @Bean(initMethod="afterPropertiesSet")
    public RedisSequenceState userProfileHistoryIdRedisSequence() {
	RedisSequenceState redisSequence = new RedisSequenceState();
	redisSequence.setSequenceName(SequenceName.USERPROFILEHISTORYID_SEQUENCE);
	redisSequence.setDistributedCache(distributedCache);
	redisSequence.setValueOperations(stringValueOperations);
	redisSequence.setDbSequenceState(userProfileHistoryIdDBSequence());
	return redisSequence;
    }    
    
    @Bean(initMethod="afterPropertiesSet")
    public DatabaseSequenceState foodLogIdDBSequence() {
	DatabaseSequenceState dbSequence = new DatabaseSequenceState();
	dbSequence.setSequenceName(SequenceName.FOODLOGID_SEQUENCE);
	dbSequence.setFindMaxIdMethod("findMaxFoodLogId");
	dbSequence.setDistributedCache(distributedCache);
	dbSequence.setJdbcTemplate(jdbcTemplateApp);
	dbSequence.setRedis(redis);
	dbSequence.setRepository(foodLogRepository);
	return dbSequence;
    }
    
    @Bean(initMethod="afterPropertiesSet")
    public DatabaseSequenceState activityLogIdDBSequence() {
	DatabaseSequenceState dbSequence = new DatabaseSequenceState();
	dbSequence.setSequenceName(SequenceName.ACTIVITYLOGID_SEQUENCE);
	dbSequence.setFindMaxIdMethod("findMaxActivityLogId");
	dbSequence.setDistributedCache(distributedCache);
	dbSequence.setJdbcTemplate(jdbcTemplateApp);
	dbSequence.setRedis(redis);
	dbSequence.setRepository(activityLogRepository);
	return dbSequence;
    }
    
    @Bean(initMethod="afterPropertiesSet")
    public DatabaseSequenceState userProfileIdDBSequence() {
	DatabaseSequenceState dbSequence = new DatabaseSequenceState();
	dbSequence.setSequenceName(SequenceName.USERPROFILEID_SEQUENCE);
	dbSequence.setFindMaxIdMethod("findMaxUserProfileId");
	dbSequence.setDistributedCache(distributedCache);
	dbSequence.setJdbcTemplate(jdbcTemplateApp);
	dbSequence.setRedis(redis);
	dbSequence.setRepository(userProfileRepository);
	return dbSequence;
    }

    @Bean(initMethod="afterPropertiesSet")
    public DatabaseSequenceState userProfileHistoryIdDBSequence() {
	DatabaseSequenceState dbSequence = new DatabaseSequenceState();
	dbSequence.setSequenceName(SequenceName.USERPROFILEHISTORYID_SEQUENCE);
	dbSequence.setFindMaxIdMethod("findMaxUserProfileHistoryId");
	dbSequence.setDistributedCache(distributedCache);
	dbSequence.setJdbcTemplate(jdbcTemplateApp);
	dbSequence.setRedis(redis);
	dbSequence.setRepository(userProfileHistoryRepository);
	return dbSequence;
    }
}
