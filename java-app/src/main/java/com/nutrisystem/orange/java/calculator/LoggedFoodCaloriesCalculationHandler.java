/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.util.List;

import com.nutrisystem.orange.java.entity.diyapp.Food;
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;
import com.nutrisystem.orange.java.entity.diyapp.Serving;
import com.nutrisystem.orange.java.entity.diyfdb.CustomFood;
import com.nutrisystem.orange.java.repository.app.FoodRepository;
import com.nutrisystem.orange.java.repository.app.ServingRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomFoodRepository;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * @author Wei Gao
 * 
 */
public class LoggedFoodCaloriesCalculationHandler implements ICalculationHandler {
	private FoodRepository foodRepository;

	private ServingRepository servingRepository;

	private CustomFoodRepository customFoodRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nutrisystem.orange.java.calculator.ICalculationHandler#process(com
	 * .nutrisystem.orange.java.calculator.UserData)
	 */
	@Override
	public void process(UserData data) {
		UserResult result = data.getResult();
		float[] foodCalories = new float[TimeBucket.getList().size()];
		float dailyFoodCalories = 0.0f;
		List<FoodLog> foodLogList = data.getUserFoodLogs();
		if (foodLogList != null && foodLogList.size() != 0) {
			for (FoodLog foodLog : foodLogList) {
				float loggedFoodCalories;
				if (foodLog.getCustom()) { // custom food
					CustomFood customFood = customFoodRepository.findOne(foodLog.getFoodId());
					loggedFoodCalories = foodLog.getServingSize().floatValue() * customFood.getCalories().floatValue();
				} else {
					Food food = foodRepository.findOne(foodLog.getFoodId());
					Serving serving = servingRepository.findOne(foodLog.getServingId());
					loggedFoodCalories = foodLog.getServingSize().floatValue() * food.getFoodCalories()
							* serving.getCoefficient();
				}
				dailyFoodCalories += Math.round(loggedFoodCalories);
				foodCalories[foodLog.getTimeBucketId() - 1] += Math.round(loggedFoodCalories);
			}
		}

		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++)
			result.setLoggedFoodCalories(foodCalories[i - 1], i);

		result.setLoggedFoodCalories(dailyFoodCalories);
	}

	public void setFoodRepository(FoodRepository foodRepository) {
		this.foodRepository = foodRepository;
	}

	public void setServingRepository(ServingRepository servingRepository) {
		this.servingRepository = servingRepository;
	}

	public void setCustomFoodRepository(CustomFoodRepository customFoodRepository) {
		this.customFoodRepository = customFoodRepository;
	}
}
