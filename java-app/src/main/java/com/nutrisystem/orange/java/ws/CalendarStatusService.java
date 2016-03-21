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

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;
import com.nutrisystem.orange.java.repository.app.ActivityLogRepository;
import com.nutrisystem.orange.java.repository.app.FoodLogRepository;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.output.CalendarStatus;
import com.nutrisystem.orange.java.ws.output.CalendarStatusOutput;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Provide restful service for Food Search Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/calendar/status/{session_id}")
public class CalendarStatusService {
	@Autowired
	private Session session;

	@Autowired
	private ActivityLogRepository activityLogRepository;

	@Autowired
	private FoodLogRepository foodLogRepository;

	@Autowired
	private ErrorMessage errorMessage;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CalendarStatusOutput readProgress(@PathParam("session_id") String sessionId, @QueryParam("year") Integer year,
			@QueryParam("month") Integer month, @QueryParam("customer_user_id") Integer customerUserId) {

		CalendarStatusOutput output = new CalendarStatusOutput();
		output.setSessionId(sessionId);
		if (!session.exists(sessionId)) {
			output.setStatus(Status.INVALID_SESSION_ID);
			return output;
		}

		if (year == null) {
			output.setStatus(Status.ERROR);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "year"));
			return output;
		}

		if (month == null) {
			output.setStatus(Status.ERROR);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "month"));
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

		String startDate = DateUtil.getMinDate(year, month);
		String endDate = DateUtil.getMaxDate(year, month);
		Map<String, Boolean> calendarStatusMap = getCalendarStatusMap(Integer.parseInt(userId), startDate, endDate);
		List<CalendarStatus> calendarStatusList = new ArrayList<>();
		for (String currDate = startDate; currDate.compareTo(endDate) <= 0; currDate = DateUtil.addDays(currDate, 1)) {
			CalendarStatus calendarStatus = new CalendarStatus();
			calendarStatus.setDate(currDate);
			calendarStatus.setStatus(calendarStatusMap.get(currDate) != null && calendarStatusMap.get(currDate));
			calendarStatusList.add(calendarStatus);
		}

		output.setCalendarStatusList(calendarStatusList);
		output.setStatus(Status.OK);
		return output;
	}
	
	private Map<String, Boolean> getCalendarStatusMap(Integer userId, String startDate, String endDate) {
		Map<String, Boolean> statusMap = new HashMap<>();
		List<FoodLog> foodLogList = foodLogRepository.findByUserIdAndFoodLogDateBetween(userId, startDate, endDate);
		for (FoodLog foodLog : foodLogList)
			statusMap.put(foodLog.getFoodLogDate(), true);
		List<ActivityLog> activityLogList = activityLogRepository.findByUserIdAndActivityLogDateBetween(userId, startDate, endDate);
		for (ActivityLog activityLog : activityLogList)
			statusMap.put(activityLog.getActivityLogDate(), true);
		return statusMap;
	}
}
