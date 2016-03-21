/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.sql.Time;
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

import com.nutrisystem.orange.java.cache.UserProfileCache;
import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.repository.app.ActivityLogRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.java.sequence.SequenceContext;
import com.nutrisystem.orange.java.validator.Checker;
import com.nutrisystem.orange.java.validator.ValidatorUtil;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.output.ActivityLoggingOutput;
import com.nutrisystem.orange.java.ws.output.mapper.ActivityLogOutputMapper;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Provide restful service for Activity Logging Task CA-1240
 * 
 * @author Wei Gao
 * 
 */
@Component
@Transactional(readOnly = true)
@Path("/activity/logging/{session_id}")
public class ActivityLogging {
	@Autowired
	private ValueOperations<String, String> stringValueOperations;

	@Autowired
	private Session session;

	@Autowired
	private ActivityLogRepository activityLogRepository;

	@Autowired
	private TimeBucketRepository timeBucketRepository;

	@Autowired
	private SequenceContext activityLogIdSequenceContext;

	@Autowired
	private Checker activityLogChecker;

	@Autowired
	private ValidatorUtil validatorUtil;

	@Autowired
	private ErrorMessage errorMessage;

	@Autowired
	private ActivityLogOutputMapper activityLogOutputMapper;

	@Autowired
	private UserProfileCache userProfileCache;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public ActivityLoggingOutput activityLogCreate(@PathParam("session_id") String sessionId,
			@QueryParam("log_date") String logDateString, @QueryParam("time_bucket_id") Integer timeBucketId,
			@QueryParam("activity_id") Integer activityId, @QueryParam("duration") Integer duration,
			@QueryParam("timezone") String timeZone, @DefaultValue("false") @QueryParam("created") boolean created,
			@DefaultValue("false") @QueryParam("device") boolean device, @QueryParam("calories") Float calories,
			@QueryParam("customer_user_id") Integer customerUserId) {
		ActivityLoggingOutput output = new ActivityLoggingOutput();
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

			ActivityLog activityLog = new ActivityLog();
			activityLog.setUserId(userId);
			activityLog.setActivityLogDate(logDateString);
			activityLog.setTimeBucketId(timeBucketId);
			activityLog.setActivityId(activityId);
			activityLog.setDuration(device ? null : duration);
			activityLog.setCustom(created);
			activityLog.setDevice(device);
			activityLog.setCalories(device ? calories : null);

			output = (ActivityLoggingOutput) activityLogChecker.check(activityLog, output);
			if (output.getStatus().equals(Status.OK)) {
				activityLog.setActivityLogId(activityLogIdSequenceContext.nextVal());
				if (timeZone == null)
					timeZone = ((UserProfile) userProfileCache.read(session.getUserId(sessionId))).getTimezone();
				Time localTime = new Time(DateUtil.getLocalTime(timeZone).getTime());
				activityLog.setLogTimeBucketId(timeBucketRepository
						.findByTimeBucketStartLessThanEqualAndTimeBucketEndGreaterThanEqual(localTime.toString(),
								localTime.toString()).get(0).getTimeBucketId());
				activityLog.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
				activityLog = activityLogRepository.save(activityLog);
				output.setActivityLogId(activityLog.getActivityLogId());
			}
		} else {
			output.setStatus(Status.INVALID_SESSION_ID);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID_SESSION_ID, "session_id"));
		}
		return output;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ActivityLoggingOutput activityLogRead(@PathParam("session_id") String sessionId,
			@QueryParam("log_date_start") String logDateStartString,
			@QueryParam("log_date_end") String logDateEndString,
			@QueryParam("time_bucket_id[]") List<Integer> timeBucketIdList,
			@QueryParam("customer_user_id") Integer customerUserId) {
		ActivityLoggingOutput output = new ActivityLoggingOutput();
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
					List<ActivityLog> activityLogList;
					if (timeBucketIdList != null && timeBucketIdList.size() > 0)
						activityLogList = activityLogRepository
								.findByUserIdAndActivityLogDateBetweenAndTimeBucketIdInOrderByTimeBucketIdAsc(userId,
										logDateStartString, logDateEndString, timeBucketIdList);
					else
						activityLogList = activityLogRepository
								.findByUserIdAndActivityLogDateBetweenOrderByTimeBucketIdAsc(userId,
										logDateStartString, logDateEndString);
					output.setResults(activityLogOutputMapper.mapping(activityLogList));
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
	public ActivityLoggingOutput activityLogUpdate(@PathParam("session_id") String sessionId,
			@QueryParam("activity_log_id") Long activityLogId, @QueryParam("time_bucket_id") Integer timeBucketId,
			@QueryParam("duration") Integer duration, @QueryParam("calories") Float calories,
			@QueryParam("customer_user_id") Integer customerUserId) {
		ActivityLoggingOutput output = new ActivityLoggingOutput();
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

			output.setActivityLogId(activityLogId);

			if (activityLogId == null) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "activity_log_id"));
			} else {
				ActivityLog activityLog = activityLogRepository.findOne(activityLogId);
				if (activityLog == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "activity_log_id"));
				} else if (!activityLog.getUserId().equals(userId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "activity_log_id"));
				} else if (!validatorUtil.isValidTimeBucketId(timeBucketId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "time_bucket_id"));
				} else if (!activityLog.getDevice() && !validatorUtil.isValidDuration(duration)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "duration"));
				} else if (activityLog.getDevice() && !validatorUtil.isValidCalories(calories)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "duration"));
				} else {
					if (timeBucketId != null)
						activityLog.setTimeBucketId(timeBucketId);
					if (!activityLog.getDevice() && duration != null)
						activityLog.setDuration(duration);
					if (activityLog.getDevice() && calories != null)
						activityLog.setCalories(calories);
					activityLog.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
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
	public ActivityLoggingOutput activityLogDelete(@PathParam("session_id") String sessionId,
			@QueryParam("activity_log_id") Long activityLogId, @QueryParam("customer_user_id") Integer customerUserId) {
		ActivityLoggingOutput output = new ActivityLoggingOutput();
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

			output.setActivityLogId(activityLogId);

			if (activityLogId == null) {
				output.setStatus(Status.ERROR);
				output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "activity_log_id"));
			} else {
				ActivityLog activityLog = activityLogRepository.findOne(activityLogId);
				if (activityLog == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "activity_log_id"));
				} else if (!activityLog.getUserId().equals(userId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "activity_log_id"));
				} else {
					activityLogRepository.delete(activityLogId);
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
