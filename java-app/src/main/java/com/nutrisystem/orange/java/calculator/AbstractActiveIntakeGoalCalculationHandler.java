/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

/**
 * @author Wei Gao
 * 
 */
public abstract class AbstractActiveIntakeGoalCalculationHandler implements ICalculationHandler {
	protected static final float[] MIN_INTAKE_GOAL = { 100, 200, 200 };

	protected int THIS_TIME_BUCKET_ID;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nutrisystem.orange.java.calculator.ICalculationHandler#process(com
	 * .nutrisystem.orange.java.calculator.UserData)
	 */
	@Override
	public void process(UserData data) {
		calcMinSatus(data);
		calcFoodPlusMinus(data);
		calcPlaceholderA(data);
		calcCarryoverFromMorning(data);
		calcPlaceholderB(data);
		calcCarryOverFromAfternoon(data);
		calcPlaceholderC(data);
		calcActiveIntakeGoal(data);
	}

	protected abstract void calcMinSatus(UserData data);

	protected void calcFoodPlusMinus(UserData data) {
		UserResult result = data.getResult();
		result.setFoodPlusMinus(result.isUseMinimum(THIS_TIME_BUCKET_ID) ? MIN_INTAKE_GOAL[THIS_TIME_BUCKET_ID - 1]
				- result.getLoggedFoodCalories(THIS_TIME_BUCKET_ID) : result.getBaselineIntakeGoal(THIS_TIME_BUCKET_ID)
				- result.getLoggedFoodCalories(THIS_TIME_BUCKET_ID), THIS_TIME_BUCKET_ID);
	};

	protected abstract void calcPlaceholderA(UserData data);

	protected abstract void calcCarryoverFromMorning(UserData data);

	protected abstract void calcPlaceholderB(UserData data);

	protected abstract void calcCarryOverFromAfternoon(UserData data);

	protected abstract void calcPlaceholderC(UserData data);

	protected abstract void calcActiveIntakeGoal(UserData data);
}
