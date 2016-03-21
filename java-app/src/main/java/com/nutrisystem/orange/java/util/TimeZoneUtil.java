/**
 * 
 */
package com.nutrisystem.orange.java.util;

import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.GoogleService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author Wei Gao
 * 
 */
public class TimeZoneUtil {
    private static ClientConfig config = new DefaultClientConfig();

    private static Client client = Client.create(config);
    
    private static GoogleService googleService;

    public static String getTimezone(float latitude, float longitude) {
	WebResource service = client
		.resource(googleService.getSignedUrl("https://maps.googleapis.com/maps/api/timezone/json?timestamp=0&sensor=false&location="
			+ latitude + "," + longitude));
	if (service.get(ClientResponse.class).getStatus() != 200)
	    return null;

	String response = service.accept(MediaType.APPLICATION_JSON).get(String.class);
	JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
	String status = jsonObject.getAsJsonPrimitive("status").getAsString();
	if (!status.equals(Status.OK))
	    return null;

	return jsonObject.getAsJsonPrimitive("timeZoneId").getAsString();

    }

    public static void setGoogleService(GoogleService googleService) {
        TimeZoneUtil.googleService = googleService;
    }

    private TimeZoneUtil() {
    }
}
