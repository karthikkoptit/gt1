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
public class LoggedActivityCaloriesCalculationHandler extends AbstractActivityCaloriesCalculationHandler {

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
	float[] activityCaloires = new float[TimeBucket.getList().size()];

	// calcuate activity calories for each time bucket and daily total
	float dailyActivityCalories = 0.0f;
	if (data.getUserActivityLogs() != null && data.getUserActivityLogs().size() != 0) {
	    for (ActivityLog activityLog : data.getUserActivityLogs()) {
		float loggedActivityCalories = calcCalories(activityLog, userProfile);
		activityCaloires[activityLog.getTimeBucketId() - 1] += Math.round(loggedActivityCalories);
		dailyActivityCalories += Math.round(loggedActivityCalories);
	    }
	}

	for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++)
	    result.setLoggedActivityCalories(activityCaloires[i - 1], i);

	result.setLoggedActivityCalories(dailyActivityCalories);
    }
}
