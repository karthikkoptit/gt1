/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.cache.UserProfileHistoryCache;
import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.output.DailyWeight;
import com.nutrisystem.orange.java.ws.output.WeightHistoryOutput;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Provide restful service for Weight History Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/weight/history/{session_id}")
public class WeightHistory {
    @Autowired
    private Session session;

    @Autowired
    private UserProfileHistoryCache userProfileHistoryCache;

    @Autowired
    private ErrorMessage errorMessage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public WeightHistoryOutput readProgress(@PathParam("session_id") String sessionId,
	    @QueryParam("timezone") String timeZone, @QueryParam("start_date") String startDate,
	    @QueryParam("end_date") String endDate, @QueryParam("customer_user_id") Integer customerUserId) {

	WeightHistoryOutput output = new WeightHistoryOutput();
	output.setSessionId(sessionId);
	if (!session.exists(sessionId)) {
	    output.setStatus(Status.INVALID_SESSION_ID);
	    return output;
	}

	if (startDate == null) {
	    output.setStatus(Status.ERROR);
	    output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "start_date"));
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

	String logDateEnd;
	String logDateStart = startDate;
	if (endDate == null) {
	    logDateEnd = DateUtil.getDateString(DateUtil.addDays(DateUtil.getDate(logDateStart), 6));
	} else {
	    logDateEnd = endDate;
	}

	List<DailyWeight> weightHistory = new ArrayList<>();
	for (String logDate = logDateStart; logDate.compareTo(logDateEnd) <= 0; logDate = DateUtil
		.getDateString(DateUtil.addDays(DateUtil.getDate(logDate), 1))) {
	    DailyWeight dailyWeight = new DailyWeight();
	    dailyWeight.setDate(logDate);
	    dailyWeight.setDay(DateUtil.getDay(logDate));

	    // find weight
	    dailyWeight.setWeight(userProfileHistoryCache.getUserProfile(userId, logDate).getCurrentLbs());
	    weightHistory.add(dailyWeight);
	}
	output.setWeightHistory(weightHistory);
	output.setStatus(Status.OK);
	return output;
    }
}
