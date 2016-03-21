/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Wei Gao
 * 
 */
public class ProgressOutput extends AbstractOutput {
    private Integer baselineIntakeGoal;

    private Integer netCalories;

    private Integer foodCalories;

    private Integer activityCalories;

    private Map<Integer, TimeBucketCalories> timeBucketCalories;

    private List<DailyCalories> calorieGraph;

    private Map<String, WeeklyNutrition> nutritionGraph;

    @XmlElement(name = "baseline_intake_goal")
    public Integer getBaselineIntakeGoal() {
	return baselineIntakeGoal;
    }

    public void setBaselineIntakeGoal(Integer baselineIntakeGoal) {
	this.baselineIntakeGoal = baselineIntakeGoal;
    }

    @XmlElement(name = "net_calories")
    public Integer getNetCalories() {
	return netCalories;
    }

    public void setNetCalories(Integer netCalories) {
	this.netCalories = netCalories;
    }

    @XmlElement(name="food_calories")
    public Integer getFoodCalories() {
	return foodCalories;
    }

    public void setFoodCalories(Integer foodCalories) {
	this.foodCalories = foodCalories;
    }

    @XmlElement(name = "activity_calories")
    public Integer getActivityCalories() {
	return activityCalories;
    }

    public void setActivityCalories(Integer activityCalories) {
	this.activityCalories = activityCalories;
    }

    @XmlElement(name="time_bucket_calories")
    public Map<Integer, TimeBucketCalories> getTimeBucketCalories() {
	return timeBucketCalories;
    }

    public void setTimeBucketCalories(Map<Integer, TimeBucketCalories> timeBucketCalories) {
	this.timeBucketCalories = timeBucketCalories;
    }

    @XmlElement(name="calorie_graph")
    public List<DailyCalories> getCalorieGraph() {
	return calorieGraph;
    }

    public void setCalorieGraph(List<DailyCalories> calorieGraph) {
	this.calorieGraph = calorieGraph;
    }

    @XmlElement(name="nutrition_graph")
    public Map<String, WeeklyNutrition> getNutritionGraph() {
	return nutritionGraph;
    }

    public void setNutritionGraph(Map<String, WeeklyNutrition> nutritionGraph) {
	this.nutritionGraph = nutritionGraph;
    }
}
