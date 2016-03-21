/**
 * 
 */
package com.nutrisystem.orange.java.mapper;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyapp.UserProfileHistory;


/**
 * @author Wei Gao
 *
 */
public class UserProfileHistoryMapper {
    public UserProfileHistory mapping(UserProfile userProfile) {
	UserProfileHistory userProfileHistory = new UserProfileHistory();
	userProfileHistory.setAddress(userProfile.getAddress());
	userProfileHistory.setBirthdate(userProfile.getBirthdate());
	userProfileHistory.setBreastfeeding(userProfile.getBreastfeeding());
	userProfileHistory.setCity(userProfile.getCity());
	userProfileHistory.setCountry(userProfile.getCountry());
	userProfileHistory.setCurrentActivityLevel(userProfile.getCurrentActivityLevel());
	userProfileHistory.setCurrentLbs(userProfile.getCurrentLbs());
	userProfileHistory.setDietaryPreferences(userProfile.getDietaryPreferences());
	userProfileHistory.setFirstName(userProfile.getFirstName());
	userProfileHistory.setGender(userProfile.getGender());
	userProfileHistory.setGoalLbs(userProfile.getGoalLbs());
	userProfileHistory.setHeight(userProfile.getHeight());
	userProfileHistory.setLastName(userProfile.getLastName());
	userProfileHistory.setMaintainingWeight(userProfile.getMaintainingWeight());
	userProfileHistory.setNickname(userProfile.getNickname());
	userProfileHistory.setPhoneNumber(userProfile.getPhoneNumber());
	userProfileHistory.setStartingLbs(userProfile.getStartingLbs());
	userProfileHistory.setState(userProfile.getState());
	userProfileHistory.setStatus(userProfile.getStatus());
	userProfileHistory.setTimezone(userProfile.getTimezone());
	userProfileHistory.setUserId(userProfile.getUserId());
	userProfileHistory.setWeekdayEatingPattern(userProfile.getWeekdayEatingPattern());
	userProfileHistory.setWeekendEatingPattern(userProfile.getWeekendEatingPattern());
	userProfileHistory.setZipCode(userProfile.getZipCode());
	userProfileHistory.setRole(userProfile.getRole());
	return userProfileHistory;
    }
}
