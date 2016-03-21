/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nutrisystem.orange.java.lookup.TimeBucketLookup;
import com.nutrisystem.orange.java.repository.app.ActivityLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodRepository;
import com.nutrisystem.orange.java.repository.app.ServingRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.java.repository.fdb.ActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomFoodRepository;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class CalculatorConfiguration {
    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private FoodRepository foodRepository;
    
    @Autowired
    private CustomActivityRepository customActivityRepository;
    
    @Autowired
    private CustomFoodRepository customFoodRepository;
    
    @Autowired
    private ServingRepository servingRepository;
    
    @Autowired
    private TimeBucketRepository timeBucketRepository;
    
    @Autowired
    private FoodLogRepository foodLogRepository;
    
    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    @Autowired
    private TimeBucketLookup timeBucketLookup;
    
    @Bean
    public CalculationPipeline calculationPipelineForFoodRecommendation() {
	CalculationPipeline calculationPipeline = new CalculationPipeline();
	
	FoodRecommendationDataPreparationHandler dataPreparationHandler = new FoodRecommendationDataPreparationHandler();
	dataPreparationHandler.setTimeBucketRepository(timeBucketRepository);
	dataPreparationHandler.setActivityLogRepository(activityLogRepository);
	dataPreparationHandler.setFoodLogRepository(foodLogRepository);
	calculationPipeline.add(dataPreparationHandler);
	
	ICalculationHandler baselineIntakeCaloriesCalculationHandler = new BaselineIntakeCaloriesCalculationHandler();
	calculationPipeline.add(baselineIntakeCaloriesCalculationHandler);
	
	LoggedActivityCaloriesCalculationHandler loggedActivityCaloriesCalculationHandler = new LoggedActivityCaloriesCalculationHandler();
	loggedActivityCaloriesCalculationHandler.setActivityRepository(activityRepository);
	loggedActivityCaloriesCalculationHandler.setCustomActivityRepository(customActivityRepository);
	calculationPipeline.add(loggedActivityCaloriesCalculationHandler);
	
	LoggedFoodCaloriesCalculationHandler loggedFoodCaloriesCalculationHandler = new LoggedFoodCaloriesCalculationHandler();
	loggedFoodCaloriesCalculationHandler.setCustomFoodRepository(customFoodRepository);
	loggedFoodCaloriesCalculationHandler.setFoodRepository(foodRepository);
	loggedFoodCaloriesCalculationHandler.setServingRepository(servingRepository);
	calculationPipeline.add(loggedFoodCaloriesCalculationHandler);
	
	ActivityPlusMinusCalculationHandler activityPlusMinusCalculationHandler = new ActivityPlusMinusCalculationHandler();
	activityPlusMinusCalculationHandler.setActivityRepository(activityRepository);
	activityPlusMinusCalculationHandler.setCustomActivityRepository(customActivityRepository);
	calculationPipeline.add(activityPlusMinusCalculationHandler);
	
//	ICalculationHandler morningActiveIntakeGoalCalculationHandler = new MorningActiveIntakeGoalCalculationHandler();
//	calculationPipeline.add(morningActiveIntakeGoalCalculationHandler);
//	
//	ICalculationHandler afternoonActiveIntakeGoalCalculationHandler = new AfternoonActiveIntakeGoalCalculationHandler();
//	calculationPipeline.add(afternoonActiveIntakeGoalCalculationHandler);
//
//	ICalculationHandler eveningActiveIntakeGoalCalculationHandler = new EveningActiveIntakeGoalCalculationHandler();
//	calculationPipeline.add(eveningActiveIntakeGoalCalculationHandler);
	RecommendIntakeCaloriesCalculationHandler recommendIntakeCaloriesCalculationHandler = new RecommendIntakeCaloriesCalculationHandler();
	recommendIntakeCaloriesCalculationHandler.setTimeBucketLookup(timeBucketLookup);
	calculationPipeline.add(recommendIntakeCaloriesCalculationHandler);
	
	return calculationPipeline;
    }
    
    @Bean
    public CalculationPipeline calculationPipelineForProgress() {
	CalculationPipeline calculationPipeline = new CalculationPipeline();
	
	ICalculationHandler baselineIntakeCaloriesCalculationHandler = new BaselineIntakeCaloriesCalculationHandler();
	calculationPipeline.add(baselineIntakeCaloriesCalculationHandler);
	
	LoggedActivityCaloriesCalculationHandler loggedActivityCaloriesCalculationHandler = new LoggedActivityCaloriesCalculationHandler();
	loggedActivityCaloriesCalculationHandler.setActivityRepository(activityRepository);
	loggedActivityCaloriesCalculationHandler.setCustomActivityRepository(customActivityRepository);
	calculationPipeline.add(loggedActivityCaloriesCalculationHandler);
	
	LoggedFoodCaloriesCalculationHandler loggedFoodCaloriesCalculationHandler = new LoggedFoodCaloriesCalculationHandler();
	loggedFoodCaloriesCalculationHandler.setCustomFoodRepository(customFoodRepository);
	loggedFoodCaloriesCalculationHandler.setFoodRepository(foodRepository);
	loggedFoodCaloriesCalculationHandler.setServingRepository(servingRepository);
	calculationPipeline.add(loggedFoodCaloriesCalculationHandler);
	
	ActivityPlusMinusCalculationHandler activityPlusMinusCalculationHandler = new ActivityPlusMinusCalculationHandler();
	activityPlusMinusCalculationHandler.setActivityRepository(activityRepository);
	activityPlusMinusCalculationHandler.setCustomActivityRepository(customActivityRepository);
	calculationPipeline.add(activityPlusMinusCalculationHandler);
	
	RecommendIntakeCaloriesCalculationHandler recommendIntakeCaloriesCalculationHandler = new RecommendIntakeCaloriesCalculationHandler();
	recommendIntakeCaloriesCalculationHandler.setTimeBucketLookup(timeBucketLookup);
	calculationPipeline.add(recommendIntakeCaloriesCalculationHandler);
	
	return calculationPipeline;
    }
}
