/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import com.nutrisystem.share.constant.TimeBucket;

/**
 * @author Wei Gao
 * 
 */
public class MorningActiveIntakeGoalCalculationHandler extends AbstractActiveIntakeGoalCalculationHandler {

    public MorningActiveIntakeGoalCalculationHandler() {
	THIS_TIME_BUCKET_ID = TimeBucket.MORNING_INT;
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
		Math.max(result.getLoggedFoodCalories(TimeBucket.AFTERNOON_INT),
			MIN_INTAKE_GOAL[TimeBucket.AFTERNOON_INT - 1])
			+ Math.max(result.getLoggedFoodCalories(TimeBucket.EVENING_INT),
				MIN_INTAKE_GOAL[TimeBucket.EVENING_INT - 1]) + MIN_INTAKE_GOAL[THIS_TIME_BUCKET_ID - 1] > result
			.getActiveIntakeGoal(), THIS_TIME_BUCKET_ID);
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
		result.getFoodPlusMinus(THIS_TIME_BUCKET_ID) + result.getActivityPlusMinus(THIS_TIME_BUCKET_ID),
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
	UserResult result = data.getResult();
	float carryoverFromMorning = 0;
	if (result.isOpen(TimeBucket.AFTERNOON_INT) && result.getLoggedFoodCalories(TimeBucket.MORNING_INT) != 0) {
	    carryoverFromMorning = result.getPlaceholderA(TimeBucket.MORNING_INT) / 2;
	}
	result.setCarryoverFromMorning(carryoverFromMorning, TimeBucket.AFTERNOON_INT);
	result.setCarryoverFromMorning(carryoverFromMorning, TimeBucket.EVENING_INT);
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
	if (!result.isOpen(TimeBucket.AFTERNOON_INT) && !result.isUseMinimum(THIS_TIME_BUCKET_ID)
		|| result.isOpen(TimeBucket.AFTERNOON_INT) && !result.isOpen(TimeBucket.EVENING_INT)
		&& result.getLoggedFoodCalories(THIS_TIME_BUCKET_ID) == 0)
	    activeIntakeGoal = result.getPlaceholderA(THIS_TIME_BUCKET_ID);
	else if (result.isOpen(TimeBucket.AFTERNOON_INT) && !result.isOpen(TimeBucket.EVENING_INT)
		&& result.getLoggedFoodCalories(THIS_TIME_BUCKET_ID) != 0)
	    activeIntakeGoal = result.getPlaceholderB(THIS_TIME_BUCKET_ID);
	else if (result.isOpen(TimeBucket.EVENING_INT))
	    activeIntakeGoal = result.getPlaceholderC(THIS_TIME_BUCKET_ID);

	result.setActiveIntakeGoal(activeIntakeGoal, THIS_TIME_BUCKET_ID);
    }

}
