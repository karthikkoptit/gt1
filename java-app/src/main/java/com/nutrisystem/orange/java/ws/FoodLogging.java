/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;
import com.nutrisystem.orange.java.repository.app.FoodLogRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.java.sequence.SequenceContext;
import com.nutrisystem.orange.java.validator.Checker;
import com.nutrisystem.orange.java.validator.ValidatorUtil;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.FoodLoggingOutput;
import com.nutrisystem.orange.java.ws.output.mapper.FoodLogOutputMapper;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Provide restful service for Food Logging Task CA-1236
 * 
 * @author Wei Gao
 * 
 */
@Component
@Transactional(readOnly = true)
@Path("/food/logging/{session_id}")
public class FoodLogging {
	@Autowired
	private ValueOperations<String, String> stringValueOperations;

	@Autowired
	private Session session;

	@Autowired
	private FoodLogRepository foodLogRepository;

	@Autowired
	private TimeBucketRepository timeBucketRepository;

	@Autowired
	private SequenceContext foodLogIdSequenceContext;

	@Autowired
	private Checker foodLogChecker;

	@Autowired
	private ValidatorUtil validatorUtil;

	@Autowired
	private ErrorMessage errorMessage;

	@Autowired
	private FoodLogOutputMapper foodLogOutputMapper;

	@Autowired
	private Solr solr;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public FoodLoggingOutput foodLogCreate(@PathParam("session_id") String sessionId,
			@QueryParam("log_date") String logDateString, @QueryParam("time_bucket_id") Integer timeBucketId,
			@QueryParam("food_id") Integer foodId, @QueryParam("serving_size") BigDecimal servingSize,
			@QueryParam("serving_id") Integer servingId, @DefaultValue("false") @QueryParam("created") boolean created,
			@QueryParam("customer_user_id") Integer customerUserId,
			@DefaultValue("false") @QueryParam("scanned") boolean scanned) {
		FoodLoggingOutput output = new FoodLoggingOutput();
		if (session.exists(sessionId)) {
			output.setSessionId(sessionId);

			Integer userId;
			if (session.isAdmin(sessionId) && customerUserId != null) {
				if (session.hasUser(customerUserId))
					userId = customerUserId;
				else {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "customer_user_id"));
					return output;
				}
			} else
				userId = Integer.valueOf(session.getUserId(sessionId));

			FoodLog foodLog = new FoodLog();

			if (created) {
				if (logDateString == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "log_date"));
				} else if (timeBucketId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "time_bucket_id"));
				} else if (foodId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "food_id"));
				} else if (servingSize == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "serving_size"));
				} else if (!validatorUtil.isValidDateFormat(logDateString)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.WRONG_DATE_FORMAT, "log_date"));
				} else if (!validatorUtil.isValidTimeBucketId(timeBucketId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "time_bucket_id"));
				} else if (!validatorUtil.isValidServingSize(servingSize)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "serving_size"));
				} else if (!validatorUtil.isValidCreatedFoodId(foodId, userId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "food_id"));
				} else {
					foodLog.setUserId(userId);
					foodLog.setFoodLogDate(logDateString);
					foodLog.setTimeBucketId(timeBucketId);
					foodLog.setFoodId(foodId);
					foodLog.setServingSize(servingSize);
					foodLog.setServingId(0);
					foodLog.setCustom(true);
					output.setStatus(Status.OK);
				}
			} else {
				foodLog.setUserId(userId);
				foodLog.setFoodLogDate(logDateString);
				foodLog.setTimeBucketId(timeBucketId);
				foodLog.setFoodId(foodId);
				foodLog.setServingSize(servingSize);
				foodLog.setServingId(servingId);
				foodLog.setCustom(false);
				output = (FoodLoggingOutput) foodLogChecker.check(foodLog, output);
			}
			if (output.getStatus().equals(Status.OK)) {
				foodLog.setFoodLogId(foodLogIdSequenceContext.nextVal());
				foodLog.setScanned(scanned);
				foodLog.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
				foodLog = foodLogRepository.save(foodLog);
				output.setFoodLogId(foodLog.getFoodLogId());
			}
		} else {
			output.setStatus(Status.INVALID_SESSION_ID);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID_SESSION_ID, "session_id"));
		}
		return output;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public FoodLoggingOutput foodLogRead(@PathParam("session_id") String sessionId,
			@QueryParam("log_date_start") String logDateStartString,
			@QueryParam("log_date_end") String logDateEndString,
			@QueryParam("time_bucket_id[]") List<Integer> timeBucketIdList,
			@QueryParam("customer_user_id") Integer customerUserId) {
		FoodLoggingOutput output = new FoodLoggingOutput();
		if (session.exists(sessionId)) {
			output.setSessionId(sessionId);

			Integer userId;
			if (session.isAdmin(sessionId) && customerUserId != null) {
				if (session.hasUser(customerUserId))
					userId = customerUserId;
				else {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "customer_user_id"));
					return output;
				}
			} else
				userId = Integer.valueOf(session.getUserId(sessionId));

			// set defaults
			if (logDateEndString == null)
				logDateEndString = logDateStartString;

			if (logDateStartString == null) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "log_date_start"));
			} else if (!validatorUtil.isValidDateFormat(logDateStartString)) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.WRONG_DATE_FORMAT, "log_date_start"));
			} else if (!validatorUtil.isValidDateFormat(logDateEndString)) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.WRONG_DATE_FORMAT, "log_date_end"));
			} else {
				output.setStatus(Status.OK);
				if (timeBucketIdList != null && timeBucketIdList.size() > 0) {
					for (Integer timeBucketId : timeBucketIdList) {
						if (!validatorUtil.isValidTimeBucketId(timeBucketId)) {
							output.setStatus(Status.ERROR);
							output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.OUT_OF_RANGE,
									"time_bucket_id"));
							break;
						}
					}
				}
				if (output.getStatus().equals(Status.OK)) {
					List<FoodLog> foodLogList;
					if (timeBucketIdList != null && timeBucketIdList.size() > 0)
						foodLogList = foodLogRepository.findByUserIdAndFoodLogDateBetweenAndTimeBucketIdIn(userId,
								logDateStartString, logDateEndString, timeBucketIdList);
					else
						foodLogList = foodLogRepository.findByUserIdAndFoodLogDateBetween(userId, logDateStartString,
								logDateEndString);
					output.setResults(foodLogOutputMapper.mapping(foodLogList));
				}
			}
		} else {
			output.setStatus(Status.INVALID_SESSION_ID);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID_SESSION_ID, "session_id"));
		}
		return output;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public FoodLoggingOutput foodLogUpdate(@PathParam("session_id") String sessionId,
			@QueryParam("food_log_id") Long foodLogId, @QueryParam("time_bucket_id") Integer timeBucketId,
			@QueryParam("serving_size") BigDecimal servingSize, @QueryParam("serving_id") Integer servingId,
			@QueryParam("customer_user_id") Integer customerUserId) {
		FoodLoggingOutput output = new FoodLoggingOutput();
		if (session.exists(sessionId)) {
			output.setSessionId(sessionId);

			Integer userId;
			if (session.isAdmin(sessionId) && customerUserId != null) {
				if (session.hasUser(customerUserId))
					userId = customerUserId;
				else {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "customer_user_id"));
					return output;
				}
			} else
				userId = Integer.valueOf(session.getUserId(sessionId));

			output.setFoodLogId(foodLogId);

			if (foodLogId == null) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "food_log_id"));
			} else {
				FoodLog foodLog = foodLogRepository.findOne(foodLogId);
				if (foodLog == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "food_log_id"));
				} else if (!foodLog.getUserId().equals(userId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "food_log_id"));
				} else if (!validatorUtil.isValidTimeBucketId(timeBucketId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "time_bucket_id"));
				} else if (!validatorUtil.isValidServingSize(servingSize)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "serving_size"));
				} else if (!validatorUtil.isValidServingId(servingId, foodLog.getFoodId())) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "serving_id"));
				} else {
					if (timeBucketId != null)
						foodLog.setTimeBucketId(timeBucketId);
					if (servingSize != null)
						foodLog.setServingSize(servingSize);
					if (servingId != null)
						foodLog.setServingId(servingId);
					foodLog.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
					output.setStatus(Status.OK);
				}
			}
		} else {
			output.setStatus(Status.INVALID_SESSION_ID);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID_SESSION_ID, "session_id"));
		}
		return output;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public FoodLoggingOutput foodLogDelete(@PathParam("session_id") String sessionId,
			@QueryParam("food_log_id") Long foodLogId, @QueryParam("customer_user_id") Integer customerUserId) {
		FoodLoggingOutput output = new FoodLoggingOutput();
		if (session.exists(sessionId)) {
			output.setSessionId(sessionId);

			Integer userId;
			if (session.isAdmin(sessionId) && customerUserId != null) {
				if (session.hasUser(customerUserId))
					userId = customerUserId;
				else {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "customer_user_id"));
					return output;
				}
			} else
				userId = Integer.valueOf(session.getUserId(sessionId));

			output.setFoodLogId(foodLogId);

			if (foodLogId == null) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "food_log_id"));
			} else {
				FoodLog foodLog = foodLogRepository.findOne(foodLogId);
				if (foodLog == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "food_log_id"));
				} else if (!foodLog.getUserId().equals(userId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "food_log_id"));
				} else {
					foodLogRepository.delete(foodLogId);
					output.setStatus(Status.OK);
				}
			}
		} else {
			output.setStatus(Status.INVALID_SESSION_ID);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID_SESSION_ID, "session_id"));
		}
		return output;
	}
}
