/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * @author Wei Gao
 *
 */
@Component
public class ErrorMessage {
    @Autowired
    private MessageSource messageSource;
    
    public String getErrorMessage(String errorCode, String field) {
	Object[] args = new Object[1];
	args[0] = field;
	return messageSource.getMessage(errorCode, args, null);
    }
}
