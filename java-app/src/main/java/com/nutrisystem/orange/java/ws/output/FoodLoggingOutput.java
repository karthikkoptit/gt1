/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Wei Gao
 * 
 */
@XmlType(propOrder = { "session_id", "food_log_id", "results", "status", "error_message", "debug" })
public class FoodLoggingOutput extends AbstractOutput {
    private Long foodLogId;

    private Object results;

    @XmlElement(name = "food_log_id")
    public Long getFoodLogId() {
	return foodLogId;
    }

    public void setFoodLogId(Long foodLogId) {
	this.foodLogId = foodLogId;
    }

    public Object getResults() {
	return results;
    }

    public void setResults(Object results) {
	this.results = results;
    }
}
