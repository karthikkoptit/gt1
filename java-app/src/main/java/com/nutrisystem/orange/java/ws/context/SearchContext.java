/**
 * 
 */
package com.nutrisystem.orange.java.ws.context;

/**
 * @author wgao
 * 
 */
public final class SearchContext {
	private final String q;
	private final int start;
	private final int rows;
	
	public SearchContext(String q, int start, int rows) {
		super();
		this.q = q;
		this.start = start;
		this.rows = rows;
	}

	public String getQ() {
		return q;
	}

	public int getStart() {
		return start;
	}

	public int getRows() {
		return rows;
	}
}
