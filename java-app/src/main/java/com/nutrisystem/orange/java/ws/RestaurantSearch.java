/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.cache.UserProfileCache;
import com.nutrisystem.orange.java.cache.ZipcodeCache;
import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.OutputFormat;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyapp.Zipcode;
import com.nutrisystem.orange.java.lookup.RadiusLookup;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.RestaurantSearchOutput;
import com.nutrisystem.orange.utility.data.UnitConversion;

/**
 * Provide restful service for Restaurant Search Task CA-1260
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/restaurant/search/{session_id}")
public class RestaurantSearch {
	@Autowired
	private ValueOperations<String, String> stringValueOperations;

	@Autowired
	private Session session;

	@Autowired
	private Solr solr;

	@Autowired
	private ErrorMessage errorMessage;

	@Autowired
	private UserProfileCache userProfileCache;

	@Autowired
	private ZipcodeCache zipcodeCache;

	@Autowired
	private RadiusLookup radiusLookup;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RestaurantSearchOutput restaurantSearch(@PathParam("session_id") String sessionId,
			@QueryParam("restaurant_brand_id") Integer restaurantBrandId,
			@QueryParam("distance_id") Integer distanceId, @QueryParam("zipcode") String zipcode,
			@QueryParam("current_lat") Float currentLat, @QueryParam("current_long") Float currentLong) {

		RestaurantSearchOutput output = new RestaurantSearchOutput();
		output.setSessionId(sessionId);
		try {
			if (session.exists(sessionId)) {
				String userId = session.getUserId(sessionId);
				if (restaurantBrandId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "restaurant_brand_id"));
				} else if (distanceId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "distance_id"));
				} else if (currentLat == null ^ currentLong == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED,
							"current_lat or current_long"));
				} else {
					UserProfile userProfile = (UserProfile) userProfileCache.read(userId);
					SolrQuery parameters = new SolrQuery();
					parameters.setQuery("*:*");
					parameters.setStart(0);
					parameters.setRows(Integer.MAX_VALUE);
					parameters.set("wt", OutputFormat.JSON);
					parameters.addFilterQuery("restaurant_brand_id:" + restaurantBrandId);
					if (currentLat == null && currentLong == null) {
						// find user current geocode
						if (zipcode == null) {
							zipcode = "00000";
							zipcode = userProfile.getZipCode();
						}
						Zipcode zipcodeEntity = (Zipcode) zipcodeCache.read(zipcode);

						if (zipcodeEntity == null) {
							output.setStatus(Status.APPERROR);
							output.setErrorMessage(ErrorCode.INVALID_ZIPCODE);
							return output;
						}
						
						currentLat = zipcodeEntity.getLatitude();
						currentLong = zipcodeEntity.getLongitude();
					}
					parameters.addFilterQuery("{!geofilt}");
					parameters.setParam("sfield", "geocode");
					parameters.set("pt", currentLat + "," + currentLong);
					parameters.set("d", String.valueOf(UnitConversion.mileToKm(radiusLookup.getRadius(distanceId))));
					parameters.setSort("geodist()", ORDER.asc);
					parameters.setFields("*", "distance:div(geodist(),1.60934)");
					SolrDocumentList solrDocumentList = solr.getSolrServer(Solr.LOCATION_SEARCH_CORE).query(parameters)
							.getResults();
					output.setNumFound(solrDocumentList.getNumFound());
					output.setDocs(solrDocumentList);
					output.setStatus(Status.OK);
				}
			} else
				output.setStatus(Status.INVALID_SESSION_ID);
		} catch (SolrServerException e) {
			output.setStatus(Status.SOLR_SERVER_ERROR);
		}
		return output;
	}
}
