/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
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
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {
    @Autowired
    private ErrorMessage errorMessage;

     @Override
    public Response toResponse(UnrecognizedPropertyException ex) {
	ExceptionOutput output = new ExceptionOutput();
	output.setStatus(Status.ERROR);
	output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "property: " + ex.getUnrecognizedPropertyName()));
	return Response.status(200).entity(output).build();
    }
}
