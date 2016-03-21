package com.nutrisystem.orange.java.calculator;

import java.util.Date;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.utility.data.UnitConversion;
import com.nutrisystem.orange.utility.date.DateUtil;
import com.nutrisystem.share.constant.ActivityLevel;
import com.nutrisystem.share.constant.EatingPattern;
import com.nutrisystem.share.constant.Gender;
import com.nutrisystem.share.constant.TimeBucket;

public class BaselineIntakeCaloriesCalculationHandler implements ICalculationHandler {

	@Override
	public void process(UserData data) {
		calcBMR(data);
		calcCalorieNeeds(data);
		calcBaselineIntakeGoal(data);
		calcTimeBucketBaselineIntakeGoal(data);
	}

	public void calcTimeBucketBaselineIntakeGoal(UserData data) {
		UserResult result = data.getResult();
		UserProfile userProfile = data.getUserProfile();
		float dailyBaselineIntakeGoal = result.getBaselineIntakeGoal();
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			float timeBucketBaselineIntakeGoal = dailyBaselineIntakeGoal
					* (float) EatingPattern.calorieAllocation(
							DateUtil.isWeekend(data.getCalculationDate()) ? userProfile.getWeekendEatingPattern()
									: userProfile.getWeekdayEatingPattern(), i);
			result.setBaselineIntakeGoal(timeBucketBaselineIntakeGoal, i);
		}
	}

	private void calcBaselineIntakeGoal(UserData data) {
		UserResult result = data.getResult();
		UserProfile userProfile = data.getUserProfile();
		float calorieNeeds = result.getCalorieNeeds();
		float bmi = Formula.calcBMI(userProfile.getCurrentLbs(), userProfile.getHeight());
		float intakeGoal;
		if (bmi < 20 || userProfile.getMaintainingWeight()) {
			float calorieDiff = calorieNeeds;
			if (calorieDiff <= 1000)
				intakeGoal = userProfile.getBreastfeeding() ? 1500 : 1000;
			else if (calorieDiff > 2500)
				intakeGoal = userProfile.getBreastfeeding() ? 3000 : 2500;
			else
				intakeGoal = userProfile.getBreastfeeding() ? calorieNeeds + 500 : calorieNeeds;
		} else {
			float calorieDiff = calorieNeeds - 750;
			if (calorieDiff <= 1000)
				intakeGoal = userProfile.getBreastfeeding() ? 1500 : 1000;
			else if (calorieDiff > 2500)
				intakeGoal = userProfile.getBreastfeeding() ? 3000 : 2500;
			else
				intakeGoal = userProfile.getBreastfeeding() ? calorieNeeds - 250 : calorieNeeds - 750;
		}
		result.setBaselineIntakeGoal(intakeGoal);
	}

	private void calcCalorieNeeds(UserData data) {
		UserResult result = data.getResult();
		float calorieNeeds = 0;
		float bmr = result.getBmr();
		switch (data.getUserProfile().getCurrentActivityLevel()) {
			case ActivityLevel.NONE:
				calorieNeeds = bmr * 1.25f;
				break;
			case ActivityLevel.MINIMAL:
				calorieNeeds = bmr * 1.4f;
				break;
			case ActivityLevel.MODERATE:
				calorieNeeds = bmr * 1.5f;
				break;
			case ActivityLevel.STEPPED_UP:
				calorieNeeds = bmr * 1.6f;
				break;
			case ActivityLevel.REVVED_UP:
				calorieNeeds = bmr * 1.7f;
				break;
		}
		result.setCalorieNeeds(calorieNeeds);
	}

	private void calcBMR(UserData data) {
		UserProfile userProfile = data.getUserProfile();
		UserResult result = data.getResult();
		result.setBmr(10 * (float) UnitConversion.lbToKg(userProfile.getCurrentLbs()) + 6.25f
				* (float) UnitConversion.inchToCm(userProfile.getHeight()) - 5
				* DateUtil.yearDiff(DateUtil.getDate(userProfile.getBirthdate()), new Date())
				+ (userProfile.getGender().equals(Gender.FEMALE) ? -161 : 5));
	}
}
