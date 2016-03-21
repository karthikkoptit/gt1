/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.io.EOFException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
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
public class EOFExceptionMapper implements ExceptionMapper<EOFException> {
    @Autowired
    private ErrorMessage errorMessage;

     @Override
    public Response toResponse(EOFException ex) {
	ExceptionOutput output = new ExceptionOutput();
	output.setStatus(Status.ERROR);
	output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.ERROR, "Request payload is null."));
	return Response.status(200).entity(output).build();
    }
}
