/**
 * 
 */
package com.nutrisystem.orange.java.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.output.AbstractOutput;

/**
 * @author Wei Gao
 * 
 */
public class Checker {
    private Validator validator;

    @Autowired
    private MessageSource messageSource;

    public AbstractOutput check(Object object, AbstractOutput output) {
	DataBinder dataBinder = new DataBinder(object, object.getClass().getSimpleName());
	dataBinder.setValidator(validator);
	dataBinder.validate();

	if (dataBinder.getBindingResult().getErrorCount() == 0) {
		output.setErrorMessage(null);	    
	    output.setStatus(Status.OK);
	} else {
		output.setStatus(Status.ERROR);
	    if (dataBinder.getBindingResult().getGlobalErrorCount() == 0)
		output.setErrorMessage(messageSource.getMessage(dataBinder.getBindingResult().getFieldError(), null));
	    else {
		output.setErrorMessage(messageSource.getMessage(dataBinder.getBindingResult().getGlobalError(), null));
	    }
	}
	return output;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }    
}
