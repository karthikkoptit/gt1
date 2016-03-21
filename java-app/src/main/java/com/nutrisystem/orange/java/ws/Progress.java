/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.cache.UserProfileHistoryCache;
import com.nutrisystem.orange.java.calculator.CalculationPipeline;
import com.nutrisystem.orange.java.calculator.UserData;
import com.nutrisystem.orange.java.calculator.UserResult;
import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyapp.Food;
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;
import com.nutrisystem.orange.java.entity.diyapp.Serving;
import com.nutrisystem.orange.java.entity.diyfdb.CustomFood;
import com.nutrisystem.orange.java.repository.app.ActivityLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodRepository;
import com.nutrisystem.orange.java.repository.app.ServingRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomFoodRepository;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.output.DailyCalories;
import com.nutrisystem.orange.java.ws.output.ProgressOutput;
import com.nutrisystem.orange.java.ws.output.TimeBucketCalories;
import com.nutrisystem.orange.java.ws.output.WeeklyNutrition;
import com.nutrisystem.orange.utility.date.DateUtil;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * Provide restful service for Food Search Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/progress/{session_id}")
public class Progress {
	@Autowired
	private Session session;

	@Autowired
	private ActivityLogRepository activityLogRepository;

	@Autowired
	private FoodLogRepository foodLogRepository;

	@Autowired
	private FoodRepository foodRepository;

	@Autowired
	private CustomFoodRepository customFoodRepository;

	@Autowired
	private ServingRepository servingRepository;

	@Autowired
	private UserProfileHistoryCache userProfileHistoryCache;

	@Autowired
	private CalculationPipeline calculationPipelineForProgress;

	@Autowired
	private ErrorMessage errorMessage;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ProgressOutput readProgress(@PathParam("session_id") String sessionId,
			@QueryParam("progress_date") String progressDate, @QueryParam("timezone") String timeZone,
			@QueryParam("start_date") String startDate, @QueryParam("end_date") String endDate,
			@QueryParam("customer_user_id") Integer customerUserId) {

		ProgressOutput output = new ProgressOutput();
		output.setSessionId(sessionId);
		if (!session.exists(sessionId)) {
			output.setStatus(Status.INVALID_SESSION_ID);
			return output;
		}

		if (progressDate == null && startDate == null || progressDate != null && (startDate != null || endDate != null)) {
			output.setStatus(Status.ERROR);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "date"));
			return output;
		}

		String userId;
		if (session.isAdmin(sessionId) && customerUserId != null) {
			if (session.hasUser(customerUserId))
				userId = String.valueOf(customerUserId);
			else {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "customer_user_id"));
				return output;
			}
		} else
			userId = session.getUserId(sessionId);

		String logDateStart;
		String logDateEnd;
		if (progressDate == null) {
			logDateStart = startDate;
			if (endDate == null) {
				logDateEnd = DateUtil.getDateString(DateUtil.addDays(DateUtil.getDate(logDateStart), 6));
			} else {
				logDateEnd = endDate;
			}
		} else {
			logDateEnd = progressDate;
			logDateStart = DateUtil.getDateString(DateUtil.addDays(DateUtil.getDate(progressDate), -6));
		}

		Map<String, List<ActivityLog>> activityLogMap = new HashMap<>();
		List<ActivityLog> fullActivityLogList = activityLogRepository.findByUserIdAndActivityLogDateBetween(
				Integer.parseInt(userId), logDateStart, logDateEnd);
		for (ActivityLog activityLog : fullActivityLogList) {
			List<ActivityLog> activityLogList = activityLogMap.get(activityLog.getActivityLogDate());
			if (activityLogList == null) {
				activityLogList = new ArrayList<>();
				activityLogMap.put(activityLog.getActivityLogDate(), activityLogList);
			}
			activityLogList.add(activityLog);
		}

		Map<String, List<FoodLog>> foodLogMap = new HashMap<>();
		List<FoodLog> fullFoodLogList = foodLogRepository.findByUserIdAndFoodLogDateBetween(Integer.parseInt(userId),
				logDateStart, logDateEnd);
		for (FoodLog foodLog : fullFoodLogList) {
			List<FoodLog> foodLogList = foodLogMap.get(foodLog.getFoodLogDate());
			if (foodLogList == null) {
				foodLogList = new ArrayList<>();
				foodLogMap.put(foodLog.getFoodLogDate(), foodLogList);
			}
			foodLogList.add(foodLog);
		}

		Map<String, DailyCalories> calorieGraph = new HashMap<>();
		int totalDaysWithlogs = 0;
		float totalFat = 0.0f;
		float totalProtein = 0.0f;
		float totalCarbohydrate = 0.0f;
		float totalFiber = 0.0f;
		float totalSodium = 0.0f;
		float totalSugar = 0.0f;
		float totalBaselineCalories = 0.0f;
		float totalFoodCalories = 0.0f;
		Integer baselineCalories = 0;

		for (String logDate = logDateStart; logDate.compareTo(logDateEnd) <= 0; logDate = DateUtil
				.getDateString(DateUtil.addDays(DateUtil.getDate(logDate), 1))) {
			UserData userData = new UserData();
			userData.setCalculationDateString(logDate);
			// find user profile
			userData.setUserProfile(userProfileHistoryCache.getUserProfile(userId, logDate));

			userData.setUserActivityLogs(activityLogMap.get(logDate));

			List<FoodLog> foodLogList = foodLogMap.get(logDate);
			userData.setUserFoodLogs(foodLogList);

			DailyCalories dailyCalories = new DailyCalories();
			calculationPipelineForProgress.process(userData);
			UserResult userResult = userData.getResult();
			baselineCalories = Math.round(userResult.getBaselineIntakeGoal());
			dailyCalories.setGoal(baselineCalories);
			dailyCalories.setActivity(Math.round(userResult.getLoggedActivityCalories()));
			int foodCalories = Math.round(userResult.getLoggedFoodCalories());
			dailyCalories.setFood(foodCalories);
			dailyCalories.setNet(Math.round(userResult.getNetCalories()));
			dailyCalories.setDate(logDate);
			dailyCalories.setDay(DateUtil.getDay(logDate));
			calorieGraph.put(logDate, dailyCalories);

			if (logDate.equals(progressDate)) {
				// today's calories for Springboard
				output.setActivityCalories(dailyCalories.getActivity());
				output.setFoodCalories(dailyCalories.getFood());
				output.setBaselineIntakeGoal(dailyCalories.getGoal());
				output.setNetCalories(dailyCalories.getGoal() - dailyCalories.getNet());

				// timebucket calories calculation for Springboard
				int totalNetCaloriesForNonCurrentTimeBuckets = 0;
//				int totalGoalCaloriesForNonCurrentTimeBuckets = 0;
				int currentTimeBucketId = 0;
				Map<Integer, TimeBucketCalories> timeBucketCaloriesMap = new HashMap<>();
				for (int j = TimeBucket.MORNING_INT; j <= TimeBucket.EVENING_INT; j++) {
					TimeBucketCalories timeBucketCalories = new TimeBucketCalories();
					timeBucketCalories.setActivity(Math.round(userResult.getLoggedActivityCalories(j)));
					timeBucketCalories.setActivityPlusMinus(Math.round(userResult.getActivityPlusMinus(j)));
					timeBucketCalories.setFood(Math.round(userResult.getLoggedFoodCalories(j)));
					timeBucketCalories.setGoal(Math.round(userResult.getBaselineIntakeGoal(j)));
					if (userResult.isCurrent(j))
						currentTimeBucketId = j;
					else {
						timeBucketCalories.setNet(Math.round(userResult.getRemainingRecommendIntakeCalories(j)));
						totalNetCaloriesForNonCurrentTimeBuckets += timeBucketCalories.getNet();
//						timeBucketCalories.setGoal(Math.round(userResult.getRecommendIntakeCalories(j)));
//						totalGoalCaloriesForNonCurrentTimeBuckets += timeBucketCalories.getGoal();
					}
					timeBucketCalories.setTimeBucket(TimeBucket.get(j));
					timeBucketCaloriesMap.put(j, timeBucketCalories);

				}
				output.setTimeBucketCalories(timeBucketCaloriesMap);

				// match the total of remaining intake calories to daily
				// remaining calories
				if (currentTimeBucketId != 0) {
					timeBucketCaloriesMap.get(currentTimeBucketId).setNet(
							output.getNetCalories() - totalNetCaloriesForNonCurrentTimeBuckets);
//					timeBucketCaloriesMap.get(currentTimeBucketId).setGoal(
//							output.getBaselineIntakeGoal() - totalGoalCaloriesForNonCurrentTimeBuckets);
				}
			}

			// nutrition calculations
			if (foodLogList != null && foodLogList.size() != 0) {
				totalDaysWithlogs++;
				totalBaselineCalories += baselineCalories;
				totalFoodCalories += foodCalories;
				for (FoodLog foodLog : foodLogList) {
					if (foodLog.getCustom()) {
						CustomFood food = customFoodRepository.findOne(foodLog.getFoodId());
						totalFat += foodLog.getServingSize().floatValue()
								* (food.getTotalFat() == null ? 0.0 : food.getTotalFat().floatValue());
						totalProtein += foodLog.getServingSize().floatValue()
								* (food.getProtein() == null ? 0.0 : food.getProtein().floatValue());
						totalCarbohydrate += foodLog.getServingSize().floatValue()
								* (food.getTotalCarbohydrate() == null ? 0.0 : food.getTotalCarbohydrate().floatValue());
						totalFiber += foodLog.getServingSize().floatValue()
								* (food.getFiber() == null ? 0.0 : food.getFiber().floatValue());
						totalSodium += foodLog.getServingSize().floatValue()
								* (food.getSodium() == null ? 0.0 : food.getSodium().floatValue());
						totalSugar += foodLog.getServingSize().floatValue()
								* (food.getSugar() == null ? 0.0 : food.getSugar().floatValue());
					} else {
						Food food = foodRepository.findOne(foodLog.getFoodId());
						Serving serving = servingRepository.findOne(foodLog.getServingId());
						totalFat += foodLog.getServingSize().floatValue()
								* (food.getFoodFat() == null ? 0.0 : food.getFoodFat()) * serving.getCoefficient();
						totalProtein += foodLog.getServingSize().floatValue()
								* (food.getFoodProtein() == null ? 0.0 : food.getFoodProtein())
								* serving.getCoefficient();
						totalCarbohydrate += foodLog.getServingSize().floatValue()
								* (food.getFoodCarbohydrate() == null ? 0.0 : food.getFoodCarbohydrate())
								* serving.getCoefficient();
						totalFiber += foodLog.getServingSize().floatValue()
								* (food.getFoodFiber() == null ? 0.0 : food.getFoodFiber()) * serving.getCoefficient();
						totalSodium += foodLog.getServingSize().floatValue()
								* (food.getFoodSodium() == null ? 0.0 : food.getFoodSodium())
								* serving.getCoefficient();
						totalSugar += foodLog.getServingSize().floatValue()
								* (food.getFoodSugar() == null ? 0.0 : food.getFoodSugar()) * serving.getCoefficient();
					}
				}
			}
		}
		output.setCalorieGraph(new ArrayList<>(calorieGraph.values()));

		if (totalDaysWithlogs == 0) {
			totalBaselineCalories = baselineCalories;
			totalDaysWithlogs = 1;
		}
		float remainingPercentage = 100.0f;

		Map<String, WeeklyNutrition> nutritionGraph = new HashMap<>();
		WeeklyNutrition weeklyNutrition = new WeeklyNutrition();
		weeklyNutrition.setActual(Math.round(totalFat * 9.0f / totalDaysWithlogs));
		weeklyNutrition.setMin(Math.round(0.2f * totalBaselineCalories / totalDaysWithlogs));
		weeklyNutrition.setMax(Math.round(0.3f * totalBaselineCalories / totalDaysWithlogs));
		float fatPercentage = Math.round(Math.min(
				totalFoodCalories == 0 ? 0.0f : 900.0f * totalFat / totalFoodCalories, remainingPercentage));
		remainingPercentage = Math.max(remainingPercentage - fatPercentage, 0.0f);
		weeklyNutrition.setActualPercentage(fatPercentage);
		weeklyNutrition.setMinPercentage(20.0f);
		weeklyNutrition.setMaxPercentage(30.0f);
		nutritionGraph.put("fat", weeklyNutrition);

		weeklyNutrition = new WeeklyNutrition();
		weeklyNutrition.setActual(Math.round(totalProtein * 4.0f / totalDaysWithlogs));
		weeklyNutrition.setMin(Math.round(0.2f * totalBaselineCalories / totalDaysWithlogs));
		weeklyNutrition.setMax(Math.round(0.3f * totalBaselineCalories / totalDaysWithlogs));
		float proteinPercentage = Math.round(Math.min(totalFoodCalories == 0 ? 0.0f : 400.0f * totalProtein
				/ totalFoodCalories, remainingPercentage));
		remainingPercentage = Math.max(remainingPercentage - proteinPercentage, 0.0f);
		weeklyNutrition.setActualPercentage(proteinPercentage);
		weeklyNutrition.setMinPercentage(20.0f);
		weeklyNutrition.setMaxPercentage(30.0f);
		nutritionGraph.put("protein", weeklyNutrition);

		weeklyNutrition = new WeeklyNutrition();
		weeklyNutrition.setActual(Math.round(totalCarbohydrate * 4.0f / totalDaysWithlogs));
		weeklyNutrition.setMin(Math.round(0.4f * totalBaselineCalories / totalDaysWithlogs));
		weeklyNutrition.setMax(Math.round(0.6f * totalBaselineCalories / totalDaysWithlogs));
		weeklyNutrition.setActualPercentage(totalFoodCalories == 0 ? 0.0f : remainingPercentage);
		weeklyNutrition.setMinPercentage(40.0f);
		weeklyNutrition.setMaxPercentage(60.0f);
		nutritionGraph.put("carbohydrate", weeklyNutrition);

		weeklyNutrition = new WeeklyNutrition();
		weeklyNutrition.setActual(Math.round(totalFiber / totalDaysWithlogs));
		weeklyNutrition.setMin(28);
		weeklyNutrition.setMax(null);
		nutritionGraph.put("fiber", weeklyNutrition);

		weeklyNutrition = new WeeklyNutrition();
		weeklyNutrition.setActual(Math.round(totalSodium / totalDaysWithlogs));
		weeklyNutrition.setMin(0);
		weeklyNutrition.setMax(2300);
		nutritionGraph.put("sodium", weeklyNutrition);

		weeklyNutrition = new WeeklyNutrition();
		weeklyNutrition.setActual(Math.round(totalSugar / totalDaysWithlogs));
		weeklyNutrition.setMin(0);
		weeklyNutrition.setMax(90);
		nutritionGraph.put("sugar", weeklyNutrition);
		output.setNutritionGraph(nutritionGraph);

		output.setStatus(Status.OK);
		return output;
	}
}
