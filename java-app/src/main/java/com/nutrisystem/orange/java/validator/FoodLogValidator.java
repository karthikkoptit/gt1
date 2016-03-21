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
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;

/**
 * @author Wei Gao
 * 
 */
@Component
public class FoodLogValidator implements Validator {
    @Autowired
    private ValidatorUtil validatorUtil;

    @Override
    public boolean supports(Class<?> clazz) {
	return FoodLog.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	if (target == null)
	    errors.reject(ErrorCode.NULL_OBJECT);

	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "foodLogDate", ErrorCode.REQUIRED,
		new Object[] { "food_log_date" });
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "timeBucketId", ErrorCode.REQUIRED,
		new Object[] { "time_bucket_id" });
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "foodId", ErrorCode.REQUIRED, new Object[] { "food_id" });
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "servingSize", ErrorCode.REQUIRED,
		new Object[] { "serving_size" });
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "servingId", ErrorCode.REQUIRED,
		new Object[] { "serving_id" });

	validatorUtil.verifyDateFormat(errors, "foodLogDate", "food_log_date");
	validatorUtil.verifyTimeBucketId(errors, "timeBucketId", "time_bucket_id");
	validatorUtil.verifyFoodId(errors, "foodId", "food_id");
	validatorUtil.verifyServingSize(errors, "servingSize", "serving_size");
	validatorUtil.verifyServingId(errors, "servingId", "serving_id");
    }
}
