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
@XmlType(propOrder = { "session_id", "activity_log_id", "results", "status", "error_message", "debug" })
public class ActivityLoggingOutput extends AbstractOutput {
    private Long activityLogId;

    private Object results;

    @XmlElement(name = "activity_log_id")
    public Long getActivityLogId() {
	return activityLogId;
    }

    public void setActivityLogId(Long activityLogId) {
	this.activityLogId = activityLogId;
    }

    public Object getResults() {
	return results;
    }

    public void setResults(Object results) {
	this.results = results;
    }
}
