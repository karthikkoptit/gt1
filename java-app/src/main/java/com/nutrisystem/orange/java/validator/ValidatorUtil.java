/**
 * 
 */
package com.nutrisystem.orange.java.validator;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.entity.diyapp.Serving;
import com.nutrisystem.orange.java.entity.diyfdb.CustomFood;
import com.nutrisystem.orange.java.range.IntegerRange;
import com.nutrisystem.orange.java.repository.app.FoodRepository;
import com.nutrisystem.orange.java.repository.app.ServingRepository;
import com.nutrisystem.orange.java.repository.fdb.ActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomActivityRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomFoodRepository;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * @author Wei Gao
 * 
 */
@Component
public class ValidatorUtil {
    @Autowired
    private IntegerRange timeBucketIdRange;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private CustomFoodRepository customFoodRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private CustomActivityRepository customActivityRepository;

    @Autowired
    private ServingRepository servingRepository;

    public void verifyActivityId(Errors errors, String field, String alias) {
	Integer activityId = (Integer) errors.getFieldValue(field);
	Boolean custom = (Boolean) errors.getFieldValue("custom");
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidActivityId(activityId, custom))
	    errors.rejectValue(field, ErrorCode.INVALID, args, null);
    }

    public boolean isValidActivityId(Integer activityId, Boolean custom) {
	return activityId == null
		|| (custom ? customActivityRepository.findOne(activityId) != null : activityRepository
			.findOne(activityId) != null);
    }

    public void verifyDuration(Errors errors, String field, String alias) {
	Integer duration = (Integer) errors.getFieldValue(field);
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidDuration(duration))
	    errors.rejectValue(field, ErrorCode.OUT_OF_RANGE, args, null);
    }

    public void verifyCalories(Errors errors, String field, String alias) {
	Float calories = (Float) errors.getFieldValue(field);
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidCalories(calories))
	    errors.rejectValue(field, ErrorCode.OUT_OF_RANGE, args, null);
    }

    public boolean isValidDuration(Integer duration) {
	return (duration != null && duration >= 0 && duration < 1440);
    }

    public boolean isValidCalories(Float calories) {
	return (calories != null && calories >= 0 && calories < 4000);
    }

    public void verifyTimeBucketId(Errors errors, String field, String alias) {
	Integer timeBucketId = (Integer) errors.getFieldValue(field);
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidTimeBucketId(timeBucketId))
	    errors.rejectValue(field, ErrorCode.OUT_OF_RANGE, args, null);
    }

    public boolean isValidTimeBucketId(Integer timeBucketId) {
	return timeBucketId == null
		|| (timeBucketId >= timeBucketIdRange.getMin() && timeBucketId <= timeBucketIdRange.getMax());
    }

    public void verifyDateFormat(Errors errors, String field, String alias) {
	String dateString = (String) errors.getFieldValue(field);
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidDateFormat(dateString))
	    errors.rejectValue(field, ErrorCode.WRONG_DATE_FORMAT, args, null);
    }

    public boolean isValidDateFormat(String dateString) {
	return dateString == null || DateUtil.isValid(dateString);
    }

    public void verifyFoodId(Errors errors, String field, String alias) {
	Integer foodId = (Integer) errors.getFieldValue(field);
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidFoodId(foodId))
	    errors.rejectValue(field, ErrorCode.INVALID, args, null);
    }

    public boolean isValidFoodId(Integer foodId) {
	return foodId == null || foodRepository.findOne(foodId) != null;
    }

    public boolean isValidCreatedFoodId(Integer createdFoodId, Integer userId) {
	if (createdFoodId == null)
	    return true;
	CustomFood customFood = customFoodRepository.findOne(createdFoodId);
	if (customFood != null && customFood.getUserId().equals(userId))
	    return true;
	return false;
    }

    public void verifyServingSize(Errors errors, String field, String alias) {
	BigDecimal servingSize = (BigDecimal) errors.getFieldValue(field);
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidServingSize(servingSize))
	    errors.rejectValue(field, ErrorCode.INVALID, args, null);
    }

    public boolean isValidServingSize(BigDecimal servingSize) {
	return servingSize == null || servingSize.compareTo(BigDecimal.ZERO) == 1;
    }

    public void verifyServingId(Errors errors, String field, String alias) {
	Integer servingId = (Integer) errors.getFieldValue(field);
	Integer foodId = (Integer) errors.getFieldValue("foodId");
	Object[] args = new Object[1];
	args[0] = alias;
	if (!isValidServingId(servingId, foodId))
	    errors.rejectValue(field, ErrorCode.INVALID, args, null);
    }

    public boolean isValidServingId(Integer servingId, Integer foodId) {
	if (servingId == null)
	    return true;

	if (servingId == 0) // user created food servings.
	    return true;

	Serving serving = servingRepository.findOne(servingId);
	return serving != null && serving.getFoodId().equals(foodId);
    }

    private ValidatorUtil() {
    }
}
