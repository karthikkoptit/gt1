/**
 * 
 */
package com.nutrisystem.orange.java.cache;

import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.Zipcode;
import com.nutrisystem.orange.java.repository.app.ZipcodeRepository;
import com.nutrisystem.orange.java.util.TimeZoneUtil;
import com.nutrisystem.orange.java.ws.helper.GoogleService;
import com.nutrisystem.orange.utility.date.DateUtil;
import com.nutrisystem.share.constant.Constant;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author Wei Gao
 * 
 */
public class ZipcodeCache extends Cache {
    private ClientConfig config = new DefaultClientConfig();
    private Client client = Client.create(config);
    private ZipcodeRepository zipcodeRepository;
    private GoogleService googleService;

    /*
     * (non-Javadoc)
     * 
     * @see com.nutrisystem.orange.java.cache.Cache#find(java.lang.String)
     */
    @Override
    protected Object find(String zipcodeString) {
	Zipcode zipcode = zipcodeRepository.findOne(zipcodeString);
	if (zipcode == null) {
	    // find geocode using google service
	    WebResource service = client
		    .resource(googleService.getSignedUrl("http://maps.googleapis.com/maps/api/geocode/json?components=postal_code&sensor=false&address="
			    + zipcodeString));
	    if (service.get(ClientResponse.class).getStatus() != 200)
		return null;

	    String response = service.accept(MediaType.APPLICATION_JSON).get(String.class);
	    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
	    String status = jsonObject.getAsJsonPrimitive("status").getAsString();
	    if (!status.equals(Status.OK))
		return null;
	    
	    JsonObject location = jsonObject.getAsJsonArray("results").get(0).getAsJsonObject()
		    .getAsJsonObject("geometry").getAsJsonObject("location");

	    zipcode = new Zipcode();
	    zipcode.setZipcode(zipcodeString);
	    float latitude = location.getAsJsonPrimitive("lat").getAsFloat();
	    zipcode.setLatitude(latitude);
	    float longitude = location.getAsJsonPrimitive("lng").getAsFloat();
	    zipcode.setLongitude(longitude);
	    
	    //find timezone
	    String timezone = TimeZoneUtil.getTimezone(latitude, longitude);
	    if (timezone == null)
		timezone = Constant.NA;
	    zipcode.setTimezone(timezone);
	    zipcode.setLastUpdateTime(DateUtil.getCurrentTimeStamp());
	    zipcodeRepository.save(zipcode);
	}

	return zipcode;
    }

    public void setZipcodeRepository(ZipcodeRepository zipcodeRepository) {
        this.zipcodeRepository = zipcodeRepository;
    }

    public void setGoogleService(GoogleService googleService) {
        this.googleService = googleService;
        TimeZoneUtil.setGoogleService(googleService);
    }
}
