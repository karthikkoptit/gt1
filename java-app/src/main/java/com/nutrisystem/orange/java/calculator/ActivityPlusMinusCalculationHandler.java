/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * @author Wei Gao
 * 
 */
public class ActivityPlusMinusCalculationHandler extends AbstractActivityCaloriesCalculationHandler {

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
		UserProfile userProfile = data.getUserProfile();

		float[] activityPlusMinus = new float[3];
		for (ActivityLog activityLog : data.getUserActivityLogs()) {
			switch (activityLog.getLogTimeBucketId()) {
				case TimeBucket.EVENING_INT:
					activityPlusMinus[TimeBucket.EVENING_INT - 1] += calcCalories(activityLog, userProfile);
					break;
				case TimeBucket.AFTERNOON_INT:
					activityPlusMinus[TimeBucket.AFTERNOON_INT - 1] += calcCalories(activityLog, userProfile) / 2.0;
					activityPlusMinus[TimeBucket.EVENING_INT - 1] += calcCalories(activityLog, userProfile) / 2.0;
					break;
				case TimeBucket.MORNING_INT:
					activityPlusMinus[TimeBucket.MORNING_INT - 1] += calcCalories(activityLog, userProfile) / 3.0;
					activityPlusMinus[TimeBucket.AFTERNOON_INT - 1] += calcCalories(activityLog, userProfile) / 3.0;
					activityPlusMinus[TimeBucket.EVENING_INT - 1] += calcCalories(activityLog, userProfile) / 3.0;
					break;
			}
		}

		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			result.setActivityPlusMinus(activityPlusMinus[i - 1], i);
		}
	}

}
