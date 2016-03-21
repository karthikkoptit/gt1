/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.utility.googleapi.UrlSigner;

/**
 * @author Wei Gao
 * 
 */
@Component
public class GoogleService {
    @Value("${google.api.client}")
    private String client;

    @Value("${google.api.key}")
    private String key;

    private boolean urlSignerKeyIsSet = false;

    public String getSignedUrl(String url) {
	if (!urlSignerKeyIsSet) {
	    UrlSigner.setKey(key);
	    urlSignerKeyIsSet = true;
	};

	return UrlSigner.signUrl(url + "&client=" + client);
    }
}
