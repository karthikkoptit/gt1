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
@XmlType(propOrder = { "session_id", "status", "error_message", "debug" })
public abstract class AbstractOutput {
    private String sessionId;

    private String status;

    private String debug;

    private String errorMessage;

    @XmlElement(name = "session_id")
    public String getSessionId() {
	return sessionId;
    }

    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getDebug() {
	return debug;
    }

    public void setDebug(String debug) {
	this.debug = debug;
    }

    @XmlElement(name = "error_message")
    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }
}
