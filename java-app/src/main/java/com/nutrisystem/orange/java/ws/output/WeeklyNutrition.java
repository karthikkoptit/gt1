/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Wei Gao
 * 
 */
public class WeeklyNutrition {
    private Integer actual;

    private Integer min;

    private Integer max;

    private Float actualPercentage;

    private Float minPercentage;

    private Float maxPercentage;

    public Integer getActual() {
	return actual;
    }

    public void setActual(Integer actual) {
	this.actual = actual;
    }

    public Integer getMin() {
	return min;
    }

    public void setMin(Integer min) {
	this.min = min;
    }

    public Integer getMax() {
	return max;
    }

    public void setMax(Integer max) {
	this.max = max;
    }

    @XmlElement(name = "actual_percentage")
    public Float getActualPercentage() {
	return actualPercentage;
    }

    public void setActualPercentage(Float actualPercentage) {
	this.actualPercentage = actualPercentage;
    }

    @XmlElement(name = "min_percentage")
    public Float getMinPercentage() {
	return minPercentage;
    }

    public void setMinPercentage(Float minPercentage) {
	this.minPercentage = minPercentage;
    }

    @XmlElement(name = "max_percentage")
    public Float getMaxPercentage() {
	return maxPercentage;
    }

    public void setMaxPercentage(Float maxPercentage) {
	this.maxPercentage = maxPercentage;
    }
}
