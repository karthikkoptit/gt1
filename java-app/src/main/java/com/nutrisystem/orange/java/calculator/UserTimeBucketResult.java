/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

/**
 * Store calculated results for a specified time bucket. 
 * @author Wei Gao
 *
 */
public class UserTimeBucketResult {
    private float baselineIntakeGoal = Float.NaN;
    
    private float loggedActivityCalories = Float.NaN;
    
    private float loggedFoodCalories = Float.NaN;

    private boolean open = false;
    
    private boolean current = false;
    
    private boolean closedout = false;
    
    private float activityPlusMinus = Float.NaN;
    
    private float recommendIntakeCalories = Float.NaN;
    
    private boolean useMinimum = false;
    
    private float foodPlusMinus = Float.NaN;
    
    private float placeholderA = Float.NaN;
    
    private float placeholderB = Float.NaN;
    
    private float placeholderC = Float.NaN;
    
    private float carryoverFromMorning = Float.NaN;
    
    private float carryoverFromAfternoon = Float.NaN;
    
    private float activeIntakeGoal = Float.NaN;
    
    public float getBaselineIntakeGoal() {
        return baselineIntakeGoal;
    }
    
    public void setBaselineIntakeGoal(float baselineIntakeGoal) {
        this.baselineIntakeGoal = baselineIntakeGoal;
    }

    public float getLoggedActivityCalories() {
        return loggedActivityCalories;
    }

    public void setLoggedActivityCalories(float loggedActivityCalories) {
        this.loggedActivityCalories = loggedActivityCalories;
    }

    public float getLoggedFoodCalories() {
        return loggedFoodCalories;
    }

    public void setLoggedFoodCalories(float loggedFoodCalories) {
        this.loggedFoodCalories = loggedFoodCalories;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public float getActivityPlusMinus() {
        return activityPlusMinus;
    }

    public void setActivityPlusMinus(float activityPlusMinus) {
        this.activityPlusMinus = activityPlusMinus;
    }

    public boolean isUseMinimum() {
        return useMinimum;
    }

    public void setUseMinimum(boolean useMinimum) {
        this.useMinimum = useMinimum;
    }

    public float getFoodPlusMinus() {
        return foodPlusMinus;
    }

    public void setFoodPlusMinus(float foodPlusMinus) {
        this.foodPlusMinus = foodPlusMinus;
    }

    public float getPlaceholderA() {
        return placeholderA;
    }

    public void setPlaceholderA(float placeholderA) {
        this.placeholderA = placeholderA;
    }

    public float getPlaceholderB() {
        return placeholderB;
    }

    public void setPlaceholderB(float placeholderB) {
        this.placeholderB = placeholderB;
    }

    public float getPlaceholderC() {
        return placeholderC;
    }

    public void setPlaceholderC(float placeholderC) {
        this.placeholderC = placeholderC;
    }

    public float getCarryoverFromMorning() {
        return carryoverFromMorning;
    }

    public void setCarryoverFromMorning(float carryoverFromMorning) {
        this.carryoverFromMorning = carryoverFromMorning;
    }

    public float getCarryoverFromAfternoon() {
        return carryoverFromAfternoon;
    }

    public void setCarryoverFromAfternoon(float carryoverFromAfternoon) {
        this.carryoverFromAfternoon = carryoverFromAfternoon;
    }

    public float getActiveIntakeGoal() {
        return activeIntakeGoal;
    }

    public void setActiveIntakeGoal(float activeIntakeGoal) {
        this.activeIntakeGoal = activeIntakeGoal;
    }

	public float getRecommendIntakeCalories() {
		return recommendIntakeCalories;
	}

	public void setRecommendIntakeCalories(float recommendIntakeCalories) {
		this.recommendIntakeCalories = recommendIntakeCalories;
	}

	public boolean isClosedout() {
		return closedout;
	}

	public void setClosedout(boolean closedout) {
		this.closedout = closedout;
	}
}
