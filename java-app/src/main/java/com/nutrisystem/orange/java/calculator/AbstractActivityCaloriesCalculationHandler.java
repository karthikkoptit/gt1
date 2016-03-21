/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyfdb.Activity;
import com.nutrisystem.orange.java.entity.diyfdb.CustomActivity;
import com.nutrisystem.orange.java.repository.fdb.ActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomActivityRepository;

/**
 * @author Wei Gao
 * 
 */
public abstract class AbstractActivityCaloriesCalculationHandler implements ICalculationHandler {
    private CustomActivityRepository customActivityRepository;
    private ActivityRepository activityRepository;

    protected float calcCalories(ActivityLog activityLog, UserProfile userProfile) {
	if (activityLog.getCustom()) { // custom food
	    CustomActivity customActivity = customActivityRepository.findOne(activityLog.getActivityId());
	    return Formula.calcActivityCalories(userProfile.getCurrentLbs(), customActivity.getMets(),
		    activityLog.getDuration());
	} else if (activityLog.getDevice()) {
	    return activityLog.getCalories();
	} else {
	    Activity activity = activityRepository.findOne(activityLog.getActivityId());
	    return Formula.calcActivityCalories(userProfile.getCurrentLbs(), activity.getMets(),
		    activityLog.getDuration());
	}
    }

    public void setCustomActivityRepository(CustomActivityRepository customActivityRepository) {
	this.customActivityRepository = customActivityRepository;
    }

    public void setActivityRepository(ActivityRepository activityRepository) {
	this.activityRepository = activityRepository;
    }
}
