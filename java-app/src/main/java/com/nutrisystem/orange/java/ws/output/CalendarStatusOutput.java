/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Wei Gao
 * 
 */
@XmlType(propOrder = { "session_id", "activity_log_id", "results", "status", "error_message", "debug" })
public class CalendarStatusOutput extends AbstractOutput {
    private List<CalendarStatus> calendarStatusList;

	@XmlElement(name = "calendar_statuses")
    public List<CalendarStatus> getCalendarStatusList() {
		return calendarStatusList;
	}

	public void setCalendarStatusList(List<CalendarStatus> calendarStatusList) {
		this.calendarStatusList = calendarStatusList;
	}
}
