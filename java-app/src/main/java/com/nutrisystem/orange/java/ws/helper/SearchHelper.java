/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import org.springframework.stereotype.Component;

/**
 * @author wgao
 *
 */

@Component
public class SearchHelper {
	public String filterQ(String q) {
		if (!q.equals("*:*")) {
			q = q.replaceAll(",|:|\\\\|\"|/", " ").trim();
			q = q.replaceAll("\\s{2,}", " ");
			q = q.replaceAll(" OR ", ":or:");
			q = q.toLowerCase();
			q = q.replaceAll(":or:", " OR ");
			q = q.length() == 0 ? "*:*" : q;
		}
		return q;
	}
}
