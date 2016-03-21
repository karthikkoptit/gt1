/**
 * 
 */
package com.nutrisystem.orange.java.ws.output.mapper;

import java.util.ArrayList;
import java.util.List;

import com.nutrisystem.orange.java.cache.UserProfileHistoryCache;
import com.nutrisystem.orange.java.calculator.Formula;
import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyfdb.Activity;
import com.nutrisystem.orange.java.entity.diyfdb.CustomActivity;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.java.repository.fdb.ActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomActivityRepository;
import com.nutrisystem.orange.java.ws.output.ActivityLogOutput;

/**
 * @author Wei Gao
 * 
 */
public class ActivityLogOutputMapper {
	private ActivityRepository activityRepository;

	private CustomActivityRepository customActivityRepository;

	private TimeBucketRepository timeBucketRepository;

	private UserProfileHistoryCache userProfileHistoryCache;

	public ActivityLogOutput mapping(ActivityLog activityLog) {
		if (activityLog == null)
			return null;

		ActivityLogOutput activityLogOutput = new ActivityLogOutput();
		activityLogOutput.setActivityLogId(activityLog.getActivityLogId());
		activityLogOutput.setActivityLogDate(activityLog.getActivityLogDate());
		activityLogOutput.setActivityId(activityLog.getActivityId());

		activityLogOutput.setTimeBucketId(activityLog.getTimeBucketId());
		activityLogOutput.setTimeBucket(timeBucketRepository.findOne(activityLog.getTimeBucketId()).getTimeBucket());

		if (activityLog.getCustom()) {
			CustomActivity customActivity = customActivityRepository.findOne(activityLog.getActivityId());
			activityLogOutput.setIconKey(null);
			activityLogOutput.setCategory("custom");
			activityLogOutput.setDetails(customActivity.getDescription());
			activityLogOutput.setMets(activityLog.getDevice() ? null : customActivity.getMets());
			activityLogOutput.setCustom(activityLog.getCustom());
			activityLogOutput.setDevice(activityLog.getDevice());

			activityLogOutput.setCalories(activityLog.getDevice() ? Math.round(activityLog.getCalories()) : Math
					.round(Formula.calcActivityCalories(
							userProfileHistoryCache.getUserProfile(String.valueOf(activityLog.getUserId()),
									activityLog.getActivityLogDate()).getCurrentLbs(), customActivity.getMets(),
							activityLog.getDuration())));

			activityLogOutput.setDuration(activityLog.getDevice() ? null : (int) activityLog.getDuration());
		} else {
			Activity activity = activityRepository.findOne(activityLog.getActivityId());
			activityLogOutput.setIconKey(activity.getIconKey());
			activityLogOutput.setCategory(activity.getCategory());
			activityLogOutput.setDetails(activity.getDetails());
			activityLogOutput.setMets(activityLog.getDevice() ? null : activity.getMets());
			activityLogOutput.setCustom(activityLog.getCustom());
			activityLogOutput.setDevice(activityLog.getDevice());

			activityLogOutput.setCalories(activityLog.getDevice() ? Math.round(activityLog.getCalories()) : Math
					.round(Formula.calcActivityCalories(
							userProfileHistoryCache.getUserProfile(String.valueOf(activityLog.getUserId()),
									activityLog.getActivityLogDate()).getCurrentLbs(), activity.getMets(),
							activityLog.getDuration())));

			activityLogOutput.setDuration(activityLog.getDevice() ? null : (int) activityLog.getDuration());
		}
		return activityLogOutput;
	}

	public List<ActivityLogOutput> mapping(List<ActivityLog> activityLogList) {
		if (activityLogList == null)
			return null;

		List<ActivityLogOutput> activityLogOutputList = new ArrayList<>();
		for (ActivityLog activityLog : activityLogList) {
			activityLogOutputList.add(mapping(activityLog));
		}
		return activityLogOutputList;
	}

	public void setCustomActivityRepository(CustomActivityRepository customActivityRepository) {
		this.customActivityRepository = customActivityRepository;
	}

	public void setActivityRepository(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public void setTimeBucketRepository(TimeBucketRepository timeBucketRepository) {
		this.timeBucketRepository = timeBucketRepository;
	}

	public UserProfileHistoryCache getUserProfileHistoryCache() {
		return userProfileHistoryCache;
	}

	public void setUserProfileHistoryCache(UserProfileHistoryCache userProfileHistoryCache) {
		this.userProfileHistoryCache = userProfileHistoryCache;
	}
}
