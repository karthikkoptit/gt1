/**
 * 
 */
package com.nutrisystem.orange.java.ws.context;

/**
 * @author wgao
 *
 */
public final class SessionContext {
	private final String sessionId;
	
	private final Integer customerUserId;

	public SessionContext(String sessionId, Integer customerUserId) {
		super();
		this.sessionId = sessionId;
		this.customerUserId = customerUserId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public Integer getCustomerUserId() {
		return customerUserId;
	}
}
