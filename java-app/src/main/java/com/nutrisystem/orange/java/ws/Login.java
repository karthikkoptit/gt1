/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.repository.app.UserProfileRepository;
import com.nutrisystem.orange.java.sequence.SequenceContext;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.UserProfileHistorySynchronizer;
import com.nutrisystem.orange.java.ws.output.LoginOutput;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Provide restful service for login Task CA-1223
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/login/{user_id}")
public class Login {
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private ValueOperations<String, String> stringValueOperations;

	@Autowired
	private SequenceContext userProfileIdSequenceContext;

	@Autowired
	private UserProfileHistorySynchronizer userProfileHistorySynchronizer;

	@Autowired
	private Session session;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LoginOutput login(@PathParam("user_id") String userIdString, @QueryParam("timezone") String timeZone) {
		LoginOutput output = new LoginOutput();

		if (!NumberUtils.isDigits(userIdString))
			output.setStatus(Status.INVALID_USER_ID);
		else {
			int userId = Integer.parseInt(userIdString);
			if (userProfileRepository.hasUser(userId)) {
				// save session id. to be improved when ELC is upgraded to 2.8
				// Redis.
				userProfileHistorySynchronizer.check(userId, timeZone);
				String sessionId = session.create(userIdString, timeZone);
				output.setSessionId(sessionId);
				output.setStatus(Status.OK);
			} else {
				output.setStatus(Status.INVALID_USER_ID);
			}
		}
		return output;
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public LoginOutput login(@PathParam("user_id") String userIdString, @QueryParam("timezone") String timeZone,
			UserProfile inputUserProfile) {
		LoginOutput output = new LoginOutput();

		if (!NumberUtils.isDigits(userIdString))
			output.setStatus(Status.INVALID_USER_ID);
		else {
			int userId = Integer.parseInt(userIdString);
			if (!userProfileRepository.hasUser(userId)) {
				// create new userprofile record when userId does not exist
				inputUserProfile.setUserId(userId);
				inputUserProfile.setUserProfileId(userProfileIdSequenceContext.nextVal());
				inputUserProfile.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
				userProfileRepository.save(inputUserProfile);
				userProfileHistorySynchronizer.sync(userId, timeZone);
			}
			// save session id. to be improved when ELC is upgraded to 2.8
			// Redis.
			// return session id when userId exists
			userProfileHistorySynchronizer.check(userId, timeZone);
			String sessionId = session.create(userIdString, timeZone);
			output.setSessionId(sessionId);
			output.setStatus(Status.OK);
		}
		return output;
	}
}
