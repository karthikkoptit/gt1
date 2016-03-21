/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.util.Date;

import com.nutrisystem.orange.java.lookup.TimeBucketLookup;
import com.nutrisystem.orange.utility.date.DateUtil;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * @author wgao
 * 
 */
public class RecommendIntakeCaloriesCalculationHandler implements ICalculationHandler {
	private TimeBucketLookup timeBucketLookup;

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

		// determine current and open windows
		Date localTime = DateUtil.getLocalTime(data.getTimeZone());
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			Date startTime = DateUtil.getDateTime(data.getCalculationDate() + " "
					+ timeBucketLookup.getTimeBucketStart(i).substring(0, 5));
			Date endTime = DateUtil.getDateTime(data.getCalculationDate() + " "
					+ timeBucketLookup.getTimeBucketEnd(i).substring(0, 5));
			if ((localTime.after(startTime) || localTime.equals(startTime)) && localTime.before(endTime)) {
				result.setCurrent(i, 0);
				break;
			}
		}

		// calculate recommend intake calories.
		float remainingCalories = 0;
		// compute daily remaining calories
		float dailyRemainingCalories = result.getBaselineIntakeGoal() - result.getNetCalories();
		// count number of open time buckets
		float numOpenTimeBuckets = 0;
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			if (!result.isClosedout(i))
				numOpenTimeBuckets += 1.0f;
		}

		// calculate remaining calories and recommend intake calories for closed
		// out or overated timebuckets.
		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			if (result.isClosedout(i)
					|| (!result.isCurrent(i) && result.getLoggedFoodCalories(i) >= Math.round(dailyRemainingCalories
							/ numOpenTimeBuckets))) {
				result.setRecommendIntakeCalories(result.getLoggedFoodCalories(i), i);
				remainingCalories += result.getBaselineIntakeGoal(i) + result.getActivityPlusMinus(i)
						- result.getLoggedFoodCalories(i);
			}
		}

		for (int i = TimeBucket.MORNING_INT; i <= TimeBucket.EVENING_INT; i++) {
			if (!result.isClosedout(i)
					&& (result.isCurrent(i) || result.getLoggedFoodCalories(i) < Math.round(dailyRemainingCalories
							/ numOpenTimeBuckets))) {
				float remainingCaloriesDistributedToThisOpenTimeBucket = 0;
				if (i == TimeBucket.AFTERNOON_INT) {
					remainingCaloriesDistributedToThisOpenTimeBucket = remainingCalories / 2.0f;
					remainingCalories -= remainingCaloriesDistributedToThisOpenTimeBucket;
				} else {
					remainingCaloriesDistributedToThisOpenTimeBucket = remainingCalories;
				}
				result.setRecommendIntakeCalories(result.getBaselineIntakeGoal(i) + result.getActivityPlusMinus(i)
						+ remainingCaloriesDistributedToThisOpenTimeBucket, i);
			}
		}
	}

	public void setTimeBucketLookup(TimeBucketLookup timeBucketLookup) {
		this.timeBucketLookup = timeBucketLookup;
	}
}
