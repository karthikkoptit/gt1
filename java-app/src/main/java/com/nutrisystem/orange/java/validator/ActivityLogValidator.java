/**
 * 
 */
package com.nutrisystem.orange.java.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.entity.diyapp.ActivityLog;

/**
 * @author Wei Gao
 * 
 */
@Component
public class ActivityLogValidator implements Validator {
    @Autowired
    private ValidatorUtil validatorUtil;

    @Override
    public boolean supports(Class<?> clazz) {
	return ActivityLog.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (target == null)
	    errors.reject(ErrorCode.NULL_OBJECT);

	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "activityLogDate", ErrorCode.REQUIRED,
		new Object[] { "activity_log_date" });
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeBucketId", ErrorCode.REQUIRED,
		new Object[] { "time_bucket_id" });
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "activityId", ErrorCode.REQUIRED,
		new Object[] { "activity_id" });
	if ((Boolean) errors.getFieldValue("device")) {
	    ValidationUtils.rejectIfEmpty(errors, "calories", ErrorCode.REQUIRED, new Object[] { "calories" });
	    validatorUtil.verifyCalories(errors, "calories", "calories");
	} else {
	    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "duration", ErrorCode.REQUIRED,
		    new Object[] { "duration" });
	    validatorUtil.verifyDuration(errors, "duration", "duraion");
	}

	validatorUtil.verifyDateFormat(errors, "activityLogDate", "activity_log_date");
	validatorUtil.verifyTimeBucketId(errors, "timeBucketId", "time_bucket_id");
	validatorUtil.verifyActivityId(errors, "activityId", "activity_id");
    }
}
