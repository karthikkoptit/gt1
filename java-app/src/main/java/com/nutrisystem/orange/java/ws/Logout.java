/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.output.LogoutOutput;

/**
 * @author Wei Gao
 *
 */
@Component
@Path("/logout/{session_id}")
public class Logout {
    @Autowired
    private Session session;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public LogoutOutput logout(@PathParam("session_id") String sessionId) {
	LogoutOutput output = new LogoutOutput();
	output.setSessionId(sessionId);
	if (session.exists(sessionId)) {
	    session.delete(sessionId);
	    
	    output.setStatus(Status.OK);
	} else
	    output.setStatus(Status.INVALID_SESSION_ID);
	    
	return output;
    }
}
