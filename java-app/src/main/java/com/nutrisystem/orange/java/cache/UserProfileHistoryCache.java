/**
 * 
 */
package com.nutrisystem.orange.java.cache;

import java.util.List;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyapp.UserProfileHistory;
import com.nutrisystem.orange.java.mapper.UserProfileMapper;
import com.nutrisystem.orange.java.repository.app.UserProfileHistoryRepository;

/**
 * @author Wei Gao
 * 
 */
public class UserProfileHistoryCache extends Cache {
	private UserProfileHistoryRepository userProfileHistoryRepository;

	private UserProfileMapper userProfileMapper;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nutrisystem.orange.java.cache.Cache#find(java.lang.String)
	 */
	@Override
	protected Object find(String id) {
		return userProfileHistoryRepository.findByUserIdOrderByUserProfileHistoryIdAsc(Integer.parseInt(id));
	}

	public void setUserProfileHistoryRepository(UserProfileHistoryRepository userProfileHistoryRepository) {
		this.userProfileHistoryRepository = userProfileHistoryRepository;
	}

	/**
	 * find UserProfile at particular date in UserProfileHistory.
	 * 
	 * @param userId
	 * @param dateString
	 * @return UserProfile at particular date.
	 */
	@SuppressWarnings("unchecked")
	public UserProfile getUserProfile(String userId, String dateString) {
		List<UserProfileHistory> userProfileHistoryList = (List<UserProfileHistory>) read(userId);
		UserProfile userProfile = null;
		for (UserProfileHistory userProfileHistory : userProfileHistoryList) {
			if (dateString.compareTo(userProfileHistory.getEffectiveDate()) >= 0
					&& dateString.compareTo(userProfileHistory.getIneffectiveDate()) <= 0) {
				userProfile = userProfileMapper.mapping(userProfileHistory);
				break;
			}
		}
		return userProfile;
	}

	public void setUserProfileMapper(UserProfileMapper userProfileMapper) {
		this.userProfileMapper = userProfileMapper;
	}
}
