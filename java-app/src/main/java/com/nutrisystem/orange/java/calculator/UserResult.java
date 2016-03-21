/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.util.HashMap;
import java.util.Map;

import com.nutrisystem.share.constant.Occasion;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * Store results from the calculation.
 * 
 * @author Wei Gao
 * 
 */
public class UserResult {
	private float bmr = Float.NaN;

	private float calorieNeeds = Float.NaN;

	private float baselineIntakeGoal = Float.NaN;

	private float loggedActivityCalories = Float.NaN;

	private float loggedFoodCalories = Float.NaN;

	private Map<Integer, UserTimeBucketResult> timeBucketResultMap;

	public UserResult() {
		timeBucketResultMap = new HashMap<>();
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			timeBucketResultMap.put(i, new UserTimeBucketResult());
		}
	}

	public float getNetCalories() {
		return loggedFoodCalories - loggedActivityCalories;
	}

	public float getRemainCalories(int timeBucketId) {
		UserTimeBucketResult userTimeBucketResult = timeBucketResultMap.get(timeBucketId);
		return userTimeBucketResult.getBaselineIntakeGoal() - userTimeBucketResult.getLoggedFoodCalories()
				+ userTimeBucketResult.getLoggedActivityCalories();
	}
	
	public float getRemainingRecommendIntakeCalories(int timeBucketId) {
		UserTimeBucketResult userTimeBucketResult = timeBucketResultMap.get(timeBucketId);
		return userTimeBucketResult.getRecommendIntakeCalories() - userTimeBucketResult.getLoggedFoodCalories();
	}

	public void setActiveIntakeGoal(float activeIntakeGoal, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setActiveIntakeGoal(activeIntakeGoal);
	}

	public float getActiveIntakeGoal(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getActiveIntakeGoal();
	}

	public void setFoodPlusMinus(float foodPlusMinus, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setFoodPlusMinus(foodPlusMinus);
	}

	public float getFoodPlusMinus(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getFoodPlusMinus();
	}

	public void setRecommendIntakeCalories(float recommendIntakeCalories, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setRecommendIntakeCalories(recommendIntakeCalories);
	}

	public float getRecommendIntakeCalories(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getRecommendIntakeCalories();
	}

	public void setPlaceholderA(float placeholderA, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setPlaceholderA(placeholderA);
	}

	public float getPlaceholderA(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getPlaceholderA();
	}

	public void setPlaceholderB(float placeholderB, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setPlaceholderB(placeholderB);
	}

	public float getPlaceholderB(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getPlaceholderB();
	}

	public void setPlaceholderC(float placeholderC, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setPlaceholderC(placeholderC);
	}

	public float getPlaceholderC(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getPlaceholderC();
	}

	public void setCarryoverFromMorning(float carryoverFromMorning, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setCarryoverFromMorning(carryoverFromMorning);
	}

	public float getCarryoverFromMorning(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getCarryoverFromMorning();
	}

	public void setCarryoverFromAfternoon(float carryoverFromAfternoon, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setCarryoverFromAfternoon(carryoverFromAfternoon);
	}

	public float getCarryoverFromAfternoon(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getCarryoverFromAfternoon();
	}

	public void setUseMinimum(boolean useMinimum, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setUseMinimum(useMinimum);
	}

	public boolean isUseMinimum(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).isUseMinimum();
	}

	public float getActiveIntakeGoal() {
		return baselineIntakeGoal + loggedActivityCalories;
	}

	public void setActivityPlusMinus(float activityPlusMinus, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setActivityPlusMinus(activityPlusMinus);
	}

	public float getActivityPlusMinus(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getActivityPlusMinus();
	}

	public void setCurrent(int timeBucketId, int recommendOccasionId) {
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			UserTimeBucketResult userTimeBucketResult = timeBucketResultMap.get(i);
			if (i <= timeBucketId) {
				userTimeBucketResult.setOpen(true);
				if (i == timeBucketId) {
					userTimeBucketResult.setCurrent(true);
					userTimeBucketResult.setClosedout(false);
				} else {
					userTimeBucketResult.setCurrent(false);
					userTimeBucketResult.setClosedout(true);
				}
			} else {
				userTimeBucketResult.setCurrent(false);
				userTimeBucketResult.setOpen(false);
				userTimeBucketResult.setClosedout(false);
			}
		}
		if (recommendOccasionId == Occasion.LUNCH_INT)
			timeBucketResultMap.get(TimeBucket.AFTERNOON_INT).setOpen(true);
		else if (recommendOccasionId == Occasion.DINNER_INT) {
			timeBucketResultMap.get(TimeBucket.AFTERNOON_INT).setOpen(true);
			timeBucketResultMap.get(TimeBucket.EVENING_INT).setOpen(true);
		}
	}

	public boolean isCurrent(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).isCurrent();
	}

	public boolean isOpen(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).isOpen();
	}
	
	public boolean isClosedout(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).isClosedout();
	}

	public void setLoggedFoodCalories(float loggedFoodCalories, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setLoggedFoodCalories(loggedFoodCalories);
	}

	public float getLoggedFoodCalories(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getLoggedFoodCalories();
	}

	public void setLoggedActivityCalories(float loggedActivityCalories, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setLoggedActivityCalories(loggedActivityCalories);
	}

	public float getLoggedActivityCalories(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getLoggedActivityCalories();
	}

	public void setBaselineIntakeGoal(float baselineIntakeGoal, int timeBucketId) {
		timeBucketResultMap.get(timeBucketId).setBaselineIntakeGoal(baselineIntakeGoal);
	}

	public float getBaselineIntakeGoal(int timeBucketId) {
		return timeBucketResultMap.get(timeBucketId).getBaselineIntakeGoal();
	}

	public float getBmr() {
		return bmr;
	}

	public void setBmr(float bmr) {
		this.bmr = bmr;
	}

	public float getCalorieNeeds() {
		return calorieNeeds;
	}

	public void setCalorieNeeds(float calorieNeeds) {
		this.calorieNeeds = calorieNeeds;
	}

	public float getBaselineIntakeGoal() {
		return baselineIntakeGoal;
	}

	public void setBaselineIntakeGoal(float baselineIntakeGoal) {
		this.baselineIntakeGoal = baselineIntakeGoal;
	}

	public float getLoggedActivityCalories() {
		return loggedActivityCalories;
	}

	public void setLoggedActivityCalories(float loggedActivityCalories) {
		this.loggedActivityCalories = loggedActivityCalories;
	}

	public float getLoggedFoodCalories() {
		return loggedFoodCalories;
	}

	public void setLoggedFoodCalories(float loggedFoodCalories) {
		this.loggedFoodCalories = loggedFoodCalories;
	}
}
