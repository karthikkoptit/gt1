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
public class UserProfileMapper {
    public UserProfile mapping(UserProfileHistory userProfileHistory) {
	UserProfile userProfile = new UserProfile();
	userProfile.setAddress(userProfileHistory.getAddress());
	userProfile.setBirthdate(userProfileHistory.getBirthdate());
	userProfile.setBreastfeeding(userProfileHistory.getBreastfeeding());
	userProfile.setCity(userProfileHistory.getCity());
	userProfile.setCountry(userProfileHistory.getCountry());
	userProfile.setCurrentActivityLevel(userProfileHistory.getCurrentActivityLevel());
	userProfile.setCurrentLbs(userProfileHistory.getCurrentLbs());
	userProfile.setDietaryPreferences(userProfileHistory.getDietaryPreferences());
	userProfile.setFirstName(userProfileHistory.getFirstName());
	userProfile.setGender(userProfileHistory.getGender());
	userProfile.setGoalLbs(userProfileHistory.getGoalLbs());
	userProfile.setHeight(userProfileHistory.getHeight());
	userProfile.setLastName(userProfileHistory.getLastName());
	userProfile.setMaintainingWeight(userProfileHistory.getMaintainingWeight());
	userProfile.setNickname(userProfileHistory.getNickname());
	userProfile.setPhoneNumber(userProfileHistory.getPhoneNumber());
	userProfile.setStartingLbs(userProfileHistory.getStartingLbs());
	userProfile.setState(userProfileHistory.getState());
	userProfile.setStatus(userProfileHistory.getStatus());
	userProfile.setTimezone(userProfileHistory.getTimezone());
	userProfile.setUserId(userProfileHistory.getUserId());
	userProfile.setWeekdayEatingPattern(userProfileHistory.getWeekdayEatingPattern());
	userProfile.setWeekendEatingPattern(userProfileHistory.getWeekendEatingPattern());
	userProfile.setZipCode(userProfileHistory.getZipCode());
	userProfile.setRole(userProfileHistory.getRole());
	return userProfile;
    }
}
