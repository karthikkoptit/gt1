/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import com.nutrisystem.share.constant.TimeBucket;

/**
 * @author Wei Gao
 * 
 */
public class EveningActiveIntakeGoalCalculationHandler extends AbstractActiveIntakeGoalCalculationHandler {

    public EveningActiveIntakeGoalCalculationHandler() {
	THIS_TIME_BUCKET_ID = TimeBucket.EVENING_INT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcMinSatus(com.nutrisystem.orange.java.calculator.UserData)
     */
    @Override
    protected void calcMinSatus(UserData data) {
	UserResult result = data.getResult();
	result.setUseMinimum(
		(Math.max(result.getLoggedFoodCalories(TimeBucket.MORNING_INT),
			MIN_INTAKE_GOAL[TimeBucket.MORNING_INT - 1])
			+ Math.max(result.getLoggedFoodCalories(TimeBucket.AFTERNOON_INT),
				MIN_INTAKE_GOAL[TimeBucket.AFTERNOON_INT - 1])
			+ result.getBaselineIntakeGoal(THIS_TIME_BUCKET_ID)
			+ result.getCarryoverFromMorning(THIS_TIME_BUCKET_ID)
			+ result.getCarryoverFromAfternoon(THIS_TIME_BUCKET_ID) > result.getActiveIntakeGoal() && !useBalance(result))
			|| result.isOpen(THIS_TIME_BUCKET_ID)
			&& result.getBaselineIntakeGoal(THIS_TIME_BUCKET_ID)
				+ result.getCarryoverFromMorning(THIS_TIME_BUCKET_ID)
				+ result.getCarryoverFromAfternoon(THIS_TIME_BUCKET_ID) <= MIN_INTAKE_GOAL[THIS_TIME_BUCKET_ID - 1],
		THIS_TIME_BUCKET_ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcPlaceholderA(com.nutrisystem.orange.java.calculator.UserData)
     */
    @Override
    protected void calcPlaceholderA(UserData data) {
	UserResult result = data.getResult();
	result.setPlaceholderA(
		result.getLoggedFoodCalories(TimeBucket.MORNING_INT)
			+ result.getLoggedFoodCalories(TimeBucket.AFTERNOON_INT)
			+ MIN_INTAKE_GOAL[THIS_TIME_BUCKET_ID - 1] < result.getActiveIntakeGoal() ? result
			.getFoodPlusMinus(THIS_TIME_BUCKET_ID) + result.getActivityPlusMinus(THIS_TIME_BUCKET_ID)
			: MIN_INTAKE_GOAL[THIS_TIME_BUCKET_ID - 1] - result.getLoggedFoodCalories(THIS_TIME_BUCKET_ID),
		THIS_TIME_BUCKET_ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcCarryoverFromMorning(com.nutrisystem
     * .orange.java.calculator.UserData)
     */
    @Override
    protected void calcCarryoverFromMorning(UserData data) {
	// do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcPlaceholderB(com.nutrisystem.orange.java.calculator.UserData)
     */
    @Override
    protected void calcPlaceholderB(UserData data) {
	UserResult result = data.getResult();
	if (result.isOpen(TimeBucket.AFTERNOON_INT)) {
	    if (result.isUseMinimum(THIS_TIME_BUCKET_ID))
		result.setPlaceholderB(
			MIN_INTAKE_GOAL[THIS_TIME_BUCKET_ID - 1] - result.getLoggedFoodCalories(THIS_TIME_BUCKET_ID),
			THIS_TIME_BUCKET_ID);
	    else
		result.setPlaceholderB(
			result.getPlaceholderA(THIS_TIME_BUCKET_ID)
				+ result.getCarryoverFromMorning(THIS_TIME_BUCKET_ID), THIS_TIME_BUCKET_ID);
	} else
	    result.setPlaceholderB(0, THIS_TIME_BUCKET_ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcCarryOverFromAfternoon(com.
     * nutrisystem.orange.java.calculator.UserData)
     */
    @Override
    protected void calcCarryOverFromAfternoon(UserData data) {
	// do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcPlaceholderC(com.nutrisystem.orange.java.calculator.UserData)
     */
    @Override
    protected void calcPlaceholderC(UserData data) {
	UserResult result = data.getResult();
	if (result.isOpen(THIS_TIME_BUCKET_ID)) {
	    if (useBalance(result))
		result.setPlaceholderC(result.getActiveIntakeGoal() - result.getLoggedFoodCalories(),
			THIS_TIME_BUCKET_ID);
	    if (result.isUseMinimum(THIS_TIME_BUCKET_ID))
		result.setPlaceholderC(result.getPlaceholderA(THIS_TIME_BUCKET_ID), THIS_TIME_BUCKET_ID);
	    else
		result.setPlaceholderC(
			result.getPlaceholderB(THIS_TIME_BUCKET_ID)
				+ result.getCarryoverFromAfternoon(THIS_TIME_BUCKET_ID), THIS_TIME_BUCKET_ID);
	} else
	    result.setPlaceholderC(0, THIS_TIME_BUCKET_ID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.calculator.
     * AbstractActiveIntakeGoalCalculationHandler
     * #calcActiveIntakeGoal(com.nutrisystem.orange.java.calculator.UserData)
     */
    @Override
    protected void calcActiveIntakeGoal(UserData data) {
	UserResult result = data.getResult();
	float activeIntakeGoal = 0;
	if (!result.isOpen(TimeBucket.AFTERNOON_INT))
	    activeIntakeGoal = result.getPlaceholderA(THIS_TIME_BUCKET_ID);
	else if (result.isOpen(TimeBucket.AFTERNOON_INT) && !result.isOpen(TimeBucket.EVENING_INT))
	    activeIntakeGoal = result.getPlaceholderB(THIS_TIME_BUCKET_ID);
	else if (result.isOpen(TimeBucket.EVENING_INT))
	    activeIntakeGoal = result.getPlaceholderC(THIS_TIME_BUCKET_ID);

	result.setActiveIntakeGoal(activeIntakeGoal, THIS_TIME_BUCKET_ID);
    }

    private boolean useBalance(UserResult result) {
	return result.getLoggedFoodCalories(TimeBucket.MORNING_INT) > 0
		&& result.getLoggedFoodCalories(TimeBucket.AFTERNOON_INT) > 0
		&& result.getActiveIntakeGoal() - result.getLoggedFoodCalories(TimeBucket.EVENING_INT) > MIN_INTAKE_GOAL[TimeBucket.EVENING_INT - 1];
    }
}
