/**
 * 
 */
package com.nutrisystem.orange.java.ws.output.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nutrisystem.orange.java.cache.UserProfileHistoryCache;
import com.nutrisystem.orange.java.repository.app.ServingRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.java.repository.fdb.ActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomFoodRepository;
import com.nutrisystem.orange.java.ws.helper.Solr;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class OutputMapperConfiguration {
    @Autowired
    private CustomFoodRepository customFoodRepository;
    
    @Autowired
    private ServingRepository servingRepository;
    
    @Autowired
    private UserProfileHistoryCache userProfileHistoryCache;
    
    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private CustomActivityRepository customActivityRepository;
    
    @Autowired 
    private TimeBucketRepository timeBucketRepository;
    
    @Autowired
    private Solr solr;
    
    @Bean
    public FoodLogOutputMapper foodLogOutputMapper() {
	FoodLogOutputMapper mapper = new FoodLogOutputMapper();
	mapper.setServingRepository(servingRepository);
	mapper.setTimeBucketRepository(timeBucketRepository);
	mapper.setCustomFoodRepository(customFoodRepository);
	mapper.setSolr(solr);
	return mapper;
    }
    
    @Bean
    public ActivityLogOutputMapper activityLogOutputMapper() {
	ActivityLogOutputMapper mapper = new ActivityLogOutputMapper();
	mapper.setUserProfileHistoryCache(userProfileHistoryCache);
	mapper.setActivityRepository(activityRepository);
	mapper.setCustomActivityRepository(customActivityRepository);
	mapper.setTimeBucketRepository(timeBucketRepository);
	return mapper;
    }
}
