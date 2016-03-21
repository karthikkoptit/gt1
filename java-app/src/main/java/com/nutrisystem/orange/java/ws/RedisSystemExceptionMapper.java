/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.output.ExceptionOutput;

/**
 * Provide exception mapper to map QueryParam data type errors.
 * 
 * @author Wei Gao
 * 
 */
@Component
@Provider
public class RedisSystemExceptionMapper implements ExceptionMapper<RedisSystemException> {
    @Autowired
    private ErrorMessage errorMessage;

     @Override
    public Response toResponse(RedisSystemException ex) {
	ExceptionOutput output = new ExceptionOutput();
	output.setStatus(Status.ERROR);
	output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.ERROR, ex.getRootCause().getMessage()));
	return Response.status(200).entity(output).build();
    }
}
