/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import java.util.List;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyapp.UserProfileHistory;
import com.nutrisystem.orange.java.mapper.UserProfileHistoryMapper;
import com.nutrisystem.orange.java.mapper.UserProfileMapper;
import com.nutrisystem.orange.java.repository.app.UserProfileHistoryRepository;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;
import com.nutrisystem.orange.java.sequence.SequenceContext;
import com.nutrisystem.orange.java.util.EntityUtil;
import com.nutrisystem.orange.utility.date.DateUtil;
import com.nutrisystem.orange.utility.date.SimpleDateFormatThreadSafe;

/**
 * @author Wei Gao
 * 
 *         sync user profile data between frontend and backend.
 * 
 */
@Component
public class UserProfileHistorySynchronizer {
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserProfileHistoryRepository userProfileHistoryRepository;

	@Autowired
	private UserProfileMapper userProfileMapper;

	@Autowired
	private UserProfileHistoryMapper userProfileHistoryMapper;

	@Autowired
	private SequenceContext userProfileHistoryIdSequenceContext;

	/**
	 * sync the profilehistory and userprofilehistory for user specified by
	 * userId
	 * 
	 * @param userId
	 */
	@Transactional
	public void sync(int userId, String timeZone) {
		UserProfile userProfile = userProfileRepository.findByUserId(userId).get(0);
		if (timeZone == null)
			timeZone = userProfile.getTimezone();

		if (userProfileHistoryRepository.hasUser(userId)) {
			// update last userProfileHistory
			UserProfileHistory lastUserProfileHistory = userProfileHistoryRepository
					.findByUserIdOrderByUserProfileHistoryIdDesc(userId).get(0);
			if (!EntityUtil.equal(new BeanWrapperImpl(userProfile),
					new BeanWrapperImpl(userProfileMapper.mapping(lastUserProfileHistory)))) {
				lastUserProfileHistory.setIneffectiveDate(SimpleDateFormatThreadSafe.format(DateUtil.addDays(
						DateUtil.getLocalTime(timeZone), -1)));
				addUserProfileHistory(userProfile, timeZone, false);
			}
		} else {
			addUserProfileHistory(userProfile, timeZone, true);
		}
	}

	@Transactional
	public void check(int userId, String timeZone) {
		List<UserProfileHistory> userProfileHistoryList = userProfileHistoryRepository
				.findByUserIdOrderByUserProfileHistoryIdAsc(userId);
		String lastIneffectiveDate = DateUtil.MIN_DATE_STRING;
		UserProfileHistory userProfileHistory = null;
		for (int i = 0; i < userProfileHistoryList.size(); i++) {
			userProfileHistory = userProfileHistoryList.get(i);
			if (DateUtil.dayDiff(userProfileHistory.getEffectiveDate(), userProfileHistory.getIneffectiveDate()) >= 0) {
				if (lastIneffectiveDate.equals(DateUtil.MIN_DATE_STRING)) {
					if (!userProfileHistory.getEffectiveDate().equals(DateUtil.MIN_DATE_STRING))
						userProfileHistory.setEffectiveDate(DateUtil.MIN_DATE_STRING);
				} else {
					if (DateUtil.dayDiff(lastIneffectiveDate, userProfileHistory.getEffectiveDate()) != 1)
						userProfileHistoryList.get(i - 1).setIneffectiveDate(
								DateUtil.addDays(userProfileHistory.getEffectiveDate(), -1));
				}
				lastIneffectiveDate = userProfileHistory.getIneffectiveDate();
			}
		}

		// check most recent user profile
		UserProfile userProfile = userProfileRepository.findByUserId(userId).get(0);
		if (timeZone == null)
			timeZone = userProfile.getTimezone();
		if (EntityUtil.equal(new BeanWrapperImpl(userProfile),
				new BeanWrapperImpl(userProfileMapper.mapping(userProfileHistory)))) {
			userProfileHistory.setIneffectiveDate(DateUtil.MAX_DATE_STRING);
		} else {
			userProfileHistory.setIneffectiveDate(SimpleDateFormatThreadSafe.format(DateUtil.addDays(
					DateUtil.getLocalTime(timeZone), -1)));
			addUserProfileHistory(userProfile, timeZone, false);
		}
	}

	private void addUserProfileHistory(UserProfile userProfile, String timeZone, boolean isFirstProfile) {
		UserProfileHistory userProfileHistory = userProfileHistoryMapper.mapping(userProfile);
		userProfileHistory.setEffectiveDate(isFirstProfile ? DateUtil.MIN_DATE_STRING : DateUtil
				.getLocalTimeString(timeZone));
		userProfileHistory.setIneffectiveDate(DateUtil.MAX_DATE_STRING);
		userProfileHistory.setUserProfileHistoryId(userProfileHistoryIdSequenceContext.nextVal());
		userProfileHistory.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
		userProfileHistoryRepository.save(userProfileHistory);
	}
}
