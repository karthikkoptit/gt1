/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.util.Date;

import com.nutrisystem.orange.java.repository.app.ActivityLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodLogRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.utility.date.DateUtil;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * @author Wei Gao
 * 
 */
public class FoodRecommendationDataPreparationHandler implements ICalculationHandler {
	private TimeBucketRepository timeBucketRepository;

	private FoodLogRepository foodLogRepository;

	private ActivityLogRepository activityLogRepository;

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
		// set user calculation date to current local date
		data.setCalculationDateString(DateUtil.getLocalTimeString(data.getTimeZone()));

		// determine current and open windows
		Date localTime = DateUtil.getLocalTime(data.getTimeZone());
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			com.nutrisystem.orange.java.entity.diyapp.TimeBucket timeBucket = timeBucketRepository.findOne(i);
			Date startTime = DateUtil.getDateTime(data.getCalculationDate() + " "
					+ timeBucket.getTimeBucketStart().substring(0, 5));
			Date endTime = DateUtil.getDateTime(data.getCalculationDate() + " "
					+ timeBucket.getTimeBucketEnd().substring(0, 5));
			if ((localTime.after(startTime) || localTime.equals(startTime)) && localTime.before(endTime)) {
				result.setCurrent(i, data.getRecommendOccasionId());
				break;
			}
		}

		// prepare log data
		data.setUserActivityLogs(activityLogRepository.findByUserIdAndActivityLogDateBetweenOrderByTimeBucketIdAsc(data
				.getUserProfile().getUserId(), data.getCalculationDate(), data.getCalculationDate()));
		data.setUserFoodLogs(foodLogRepository.findByUserIdAndFoodLogDateBetween(data.getUserProfile().getUserId(),
				data.getCalculationDate(), data.getCalculationDate()));
	}

	public void setTimeBucketRepository(TimeBucketRepository timeBucketRepository) {
		this.timeBucketRepository = timeBucketRepository;
	}

	public void setFoodLogRepository(FoodLogRepository foodLogRepository) {
		this.foodLogRepository = foodLogRepository;
	}

	public void setActivityLogRepository(ActivityLogRepository activityLogRepository) {
		this.activityLogRepository = activityLogRepository;
	}
}
