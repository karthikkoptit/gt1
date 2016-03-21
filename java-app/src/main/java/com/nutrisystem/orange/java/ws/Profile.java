/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.appservice.UserProfileAppService;
import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.output.ProfileOutput;

/**
 * @author Wei Gao
 * 
 */
@Component
@Path("/profile/{session_id}")
public class Profile {
	@Autowired
	private Session session;

	@Autowired
	private UserProfileAppService userProfileAppService;

	@Autowired
	private ErrorMessage errorMessage;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileOutput profileChangeNotification(@PathParam("session_id") String sessionId,
			@QueryParam("timezone") String timeZone, UserProfile inputUserProfile,
			@QueryParam("customer_user_id") Integer customerUserId) {
		ProfileOutput output = new ProfileOutput();
		output.setSessionId(sessionId);

		if (!session.exists(sessionId)) {
			output.setStatus(Status.INVALID_SESSION_ID);
			return output;
		}
		if (inputUserProfile == null) {
			output.setStatus(Status.ERROR);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "request_payload"));
			return output;
		}

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

		inputUserProfile.setUserId(userId);
		userProfileAppService.update(inputUserProfile);
		session.cache(sessionId, String.valueOf(userId), timeZone);
		output.setStatus(Status.OK);
		return output;
	}
}
