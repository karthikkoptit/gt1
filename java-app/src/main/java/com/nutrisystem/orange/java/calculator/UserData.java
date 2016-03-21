/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.util.ArrayList;
import java.util.List;

import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;

/**
 * The Command Object for Pipeline pattern.
 * 
 * @author Wei Gao
 * 
 */
public class UserData {
	private String calculationDate;

	private String timeZone;

	private int recommendOccasionId;

	private UserProfile userProfile;

	private List<ActivityLog> userActivityLogs;

	private List<FoodLog> userFoodLogs;

	private UserResult result;

	public UserData() {
		result = new UserResult();
	}

	public String getCalculationDate() {
		return calculationDate;
	}

	public void setCalculationDateString(String calculationDate) {
		this.calculationDate = calculationDate;
	}

	public String getTimeZone() {
		if (timeZone == null)
			timeZone = userProfile.getTimezone();
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public List<ActivityLog> getUserActivityLogs() {
		return userActivityLogs == null ? new ArrayList<ActivityLog>() : userActivityLogs;
	}

	public void setUserActivityLogs(List<ActivityLog> userActivityLogs) {
		this.userActivityLogs = userActivityLogs;
	}

	public List<FoodLog> getUserFoodLogs() {
		return userFoodLogs == null ? new ArrayList<FoodLog>() : userFoodLogs;
	}

	public void setUserFoodLogs(List<FoodLog> userFoodLogs) {
		this.userFoodLogs = userFoodLogs;
	}

	public UserResult getResult() {
		return result;
	}

	public int getRecommendOccasionId() {
		return recommendOccasionId;
	}

	public void setRecommendOccasionId(int recommendOccasionId) {
		this.recommendOccasionId = recommendOccasionId;
	}
}
