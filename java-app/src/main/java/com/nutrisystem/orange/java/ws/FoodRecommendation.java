/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.cache.UserProfileCache;
import com.nutrisystem.orange.java.cache.ZipcodeCache;
import com.nutrisystem.orange.java.calculator.CalculationPipeline;
import com.nutrisystem.orange.java.calculator.UserData;
import com.nutrisystem.orange.java.calculator.UserResult;
import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.OutputFormat;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.entity.diyapp.UserProfile;
import com.nutrisystem.orange.java.entity.diyapp.Zipcode;
import com.nutrisystem.orange.java.lookup.DietLookup;
import com.nutrisystem.orange.java.lookup.RadiusLookup;
import com.nutrisystem.orange.java.lookup.TimeBucketLookup;
import com.nutrisystem.orange.java.util.TimeZoneUtil;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.SearchHelper;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.FoodRecommendationOutput;
import com.nutrisystem.orange.utility.data.UnitConversion;
import com.nutrisystem.orange.utility.dataformat.YamlUtil;
import com.nutrisystem.share.constant.Constant;
import com.nutrisystem.share.constant.EatInOut;
import com.nutrisystem.share.constant.Occasion;
import com.nutrisystem.share.constant.TimeBucket;

/**
 * Provide restful service for Food Recommendation Task CA-1259
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/food/recommendation/{session_id}")
public class FoodRecommendation {
	private static final String OUTPUT_FOOD = "food";
	private static final String OUTPUT_LOCATION = "location";
	private static final String OUTPUT_JAWBONE = "jawbone";

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
	private CalculationPipeline calculationPipelineForFoodRecommendation;

	@Autowired
	private ZipcodeCache zipcodeCache;

	@Autowired
	private RadiusLookup radiusLookup;

	@Autowired
	private DietLookup dietLookup;

	@Autowired
	private TimeBucketLookup timeBucketLookup;

	@Autowired
	SearchHelper searchHelper;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public FoodRecommendationOutput foodRecommendation(@PathParam("session_id") String sessionId,
			@DefaultValue("*:*") @QueryParam("q") String q, @DefaultValue("0") @QueryParam("start") int start,
			@DefaultValue("50") @QueryParam("rows") int rows, @QueryParam("eatinout_id") Integer eatinoutId,
			@QueryParam("time_bucket_id") Integer timeBucketId, @QueryParam("occasion_id") Integer occasionId,
			@QueryParam("cuisine_id[]") List<Integer> cuisineIdList, @QueryParam("total_time_id") Integer totalTimeId,
			@QueryParam("recipe_servings_id[]") List<Integer> recipeServingsIdList,
			@DefaultValue("false") @QueryParam("include_side") Boolean includeSide,
			@QueryParam("distance_id") Integer distanceId, @QueryParam("zipcode") String zipcode,
			@QueryParam("current_lat") Float currentLat, @QueryParam("current_long") Float currentLong,
			@QueryParam("restaurant_brand_id") Integer restaurantBrandId,
			@DefaultValue("false") @QueryParam("nsonly") Boolean nutrisystemOnly,
			@DefaultValue("food") @QueryParam("output") String outputType,
			@QueryParam("customer_user_id") Integer customerUserId) {

		FoodRecommendationOutput output = new FoodRecommendationOutput();
		output.setSessionId(sessionId);
		try {
			if (session.exists(sessionId)) {

				String userId;
				if (session.isAdmin(sessionId) && customerUserId != null) {
					if (session.hasUser(customerUserId))
						userId = String.valueOf(customerUserId);
					else {
						output.setStatus(Status.ERROR);
						output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "customer_user_id"));
						return output;
					}
				} else
					userId = session.getUserId(sessionId);

				UserProfile userProfile = (UserProfile) userProfileCache.read(userId);
				SolrQuery parameters = new SolrQuery();
				parameters.setQuery("{!lucene q.op=AND} " + searchHelper.filterQ(q));
				parameters.setStart(start);
				parameters.setRows(rows);
				parameters.set("wt", OutputFormat.JSON);
				if (eatinoutId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "eatinout_id"));
				} else if (!EatInOut.isValid(eatinoutId)) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "eatinout_id"));
				} else if (occasionId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "occasion_id"));
				} else if (timeBucketId == null) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "time_bucket_id"));
				} else if (!timeBucketLookup.isValidTimeBucketId(timeBucketId)
						&& timeBucketId != TimeBucket.ANYTIME_INT) {
					output.setStatus(Status.ERROR);
					output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "time_bucket_id"));
				} else {
					UserData userData = new UserData();
					userData.setUserProfile(userProfile);

					String timezone = null;
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
						timezone = zipcodeEntity.getTimezone();
					} else {
						timezone = TimeZoneUtil.getTimezone(currentLat, currentLong);
					}
					if (timezone != null)
						userData.setTimeZone(timezone);

					switch (eatinoutId) {
						case EatInOut.EATIN_INT:
							if (totalTimeId == null) {
								output.setStatus(Status.ERROR);
								output.setErrorMessage(errorMessage
										.getErrorMessage(ErrorCode.REQUIRED, "total_time_id"));
								return output;
							}
							parameters.addFilterQuery("eatinout_id:" + EatInOut.EATIN_INT);
							parameters.addFilterQuery("total_time_id:" + totalTimeId);
							if (recipeServingsIdList != null && recipeServingsIdList.size() > 0) {
								String recipeServingsFilterQueryString = "recipe_servings_id:(";
								for (Integer recipeServingsId : recipeServingsIdList)
									recipeServingsFilterQueryString += " " + recipeServingsId;
								recipeServingsFilterQueryString += ")";
								parameters.addFilterQuery(recipeServingsFilterQueryString);
							}
							if (!includeSide) {
								parameters.addFilterQuery("-dish_id:1");
							}
							if (nutrisystemOnly)
								parameters.addFilterQuery("source_id:(3 4)");
							break;
						case EatInOut.EATOUT_INT:
							if (distanceId == null) {
								output.setStatus(Status.ERROR);
								output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "distance_id"));
								return output;
							}
							if (currentLat == null ^ currentLong == null) {
								output.setStatus(Status.ERROR);
								output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED,
										"current_lat or current_long"));
								return output;
							}
							if (!outputType.equals(OUTPUT_FOOD) && !outputType.equals(OUTPUT_LOCATION)
									&& !outputType.equals(OUTPUT_JAWBONE)) {
								output.setStatus(Status.ERROR);
								output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "output"));
								return output;
							}

							String radiusFilterQueryString = "restaurant_brand_id:(";
							radiusFilterQueryString += getRestaurantIdsString(currentLat, currentLong, distanceId);
							radiusFilterQueryString += ")";
							parameters.addFilterQuery(radiusFilterQueryString);

							// add restaurant brand fileter
							if (restaurantBrandId != null) {
								String restaurantBrandFilterString = "restaurant_brand_id:" + restaurantBrandId;
								parameters.addFilterQuery(restaurantBrandFilterString);
							}
							break;
					}

					// add occasion filter
					parameters.addFilterQuery("occasion_id:" + occasionId);

					// add diet filter
					List<String> dietList = YamlUtil.yamlToList(userProfile.getDietaryPreferences());
					if (dietList != null && dietList.size() != 0 && !dietList.contains(Constant.NA)) {
						String dietFilterQueryString = "diet_id:(" + dietLookup.getDietId(dietList.get(0));
						for (int i = 1; i < dietList.size(); i++)
							dietFilterQueryString += " AND " + dietLookup.getDietId(dietList.get(i));
						dietFilterQueryString += ")";
						parameters.addFilterQuery(dietFilterQueryString);
					}

					// add cuisine filter
					if (cuisineIdList != null && cuisineIdList.size() > 0) {
						String cuisineFilterQueryString = "cuisine_id:(";
						for (Integer cuisineId : cuisineIdList)
							cuisineFilterQueryString += " " + cuisineId;
						cuisineFilterQueryString += ")";
						parameters.addFilterQuery(cuisineFilterQueryString);
					}

					// add calorie constraints
					userData.setRecommendOccasionId(timeBucketId);
					calculationPipelineForFoodRecommendation.process(userData);
					float caloriesUpperBound = 0;
					UserResult userResult = userData.getResult();

					if (timeBucketId == TimeBucket.ANYTIME_INT || occasionId == Occasion.SNACK_INT)
						caloriesUpperBound = Math.min(200,
								userResult.getActiveIntakeGoal() - userResult.getLoggedFoodCalories());
					else
						caloriesUpperBound = userResult.getRecommendIntakeCalories(timeBucketId);
					
					if (outputType.equals(OUTPUT_JAWBONE)) {
						parameters.setRows(1);
						caloriesUpperBound -= Math.random() * 100.0; 
					}

					System.out.println("calories:[0 TO " + caloriesUpperBound + "]" + "t=" + timeBucketId);
					parameters.addFilterQuery("calories:[0 TO " + caloriesUpperBound + "]");
					parameters.setSort("calories", ORDER.desc);

					if (outputType.equals(OUTPUT_LOCATION) && eatinoutId == EatInOut.EATOUT_INT) {
						parameters.setRows(0);
						parameters.setFacet(true);
						parameters.setFacetLimit(-1);
						parameters.setFacetMinCount(1);
						parameters.addFacetField("restaurant_brand_id");
					}

					QueryResponse queryResponse = solr.getSolrServer(Solr.FOOD_RECOMMENDATION_CORE).query(parameters);
					SolrDocumentList solrDocumentList = queryResponse.getResults();

					if (solrDocumentList.getNumFound() == 0) {
						output.setStatus(Status.APPERROR);
						output.setErrorMessage(ErrorCode.MEAL_NO_RESULTS);
						if (caloriesUpperBound <= 0) {
							output.setErrorMessage(ErrorCode.MEAL_CALS_OVER);
						}
						return output;
					}

					if (outputType.equals(OUTPUT_LOCATION) && eatinoutId == EatInOut.EATOUT_INT) {
						FacetField field;
						field = queryResponse.getFacetField("restaurant_brand_id");
						List<Count> values = field.getValues();

						String restaurantBrandIdsString = "0";
						Map<Integer, Boolean> restaurantLocationFoundMap = new HashMap<Integer, Boolean>();
						for (Count value : values) {
							restaurantBrandIdsString += " " + value.getName();
							restaurantLocationFoundMap.put(Integer.parseInt(value.getName()), false);
						}

						parameters = new SolrQuery();
						parameters.setRows(Solr.MAX_ROWS);
						parameters.setQuery("*:*");
						parameters.set("wt", OutputFormat.JSON);
						parameters.addFilterQuery("restaurant_brand_id:(" + restaurantBrandIdsString + ")");
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
						parameters
								.set("d", String.valueOf(UnitConversion.mileToKm(radiusLookup.getRadius(distanceId))));
						parameters.setSort("geodist()", ORDER.asc);
						parameters.setFields("*", "distance:div(geodist(),1.60934)");
						solrDocumentList = solr.getSolrServer(Solr.LOCATION_SEARCH_CORE).query(parameters).getResults();

						// only keep the min distance location
						ListIterator<SolrDocument> it = solrDocumentList.listIterator();
						SolrDocumentList locationSolrDocumentList = new SolrDocumentList();
						locationSolrDocumentList.setNumFound(restaurantLocationFoundMap.keySet().size());
						while (it.hasNext()) {
							SolrDocument solrDocument = it.next();
							Integer restaurantId = (Integer) solrDocument.getFieldValue("restaurant_brand_id");
							if (!restaurantLocationFoundMap.get(restaurantId)) {
								locationSolrDocumentList.add(solrDocument);
								if (locationSolrDocumentList.size() >= rows)
									break;
								restaurantLocationFoundMap.put(restaurantId, true);
							}
						}
						output.setNumFound(locationSolrDocumentList.getNumFound());
						output.setDocs(locationSolrDocumentList);
					} else {
						output.setNumFound(solrDocumentList.getNumFound());
						output.setDocs(solrDocumentList);
					}
					output.setStatus(Status.OK);
				}
			} else
				output.setStatus(Status.INVALID_SESSION_ID);
		} catch (SolrServerException e) {
			output.setStatus(Status.SOLR_SERVER_ERROR);
		}
		return output;
	}

	private String getRestaurantIdsString(float latitude, float longitude, int distanceId) throws SolrServerException {
		SolrQuery parameters = new SolrQuery();
		parameters.setQuery("*:*");
		parameters.setRows(0);
		parameters.set("wt", "json");
		parameters.setFacet(true);
		parameters.setFacetLimit(-1);
		parameters.setFacetMinCount(1);
		parameters.addFacetField("restaurant_brand_id");
		parameters.addFilterQuery("{!geofilt}");
		parameters.setParam("sfield", "geocode");
		parameters.set("pt", latitude + "," + longitude);

		parameters.set("d", String.valueOf(UnitConversion.mileToKm(radiusLookup.getRadius(distanceId))));
		FacetField field;
		field = solr.getSolrServer(Solr.LOCATION_SEARCH_CORE).query(parameters).getFacetField("restaurant_brand_id");
		List<Count> values = field.getValues();

		String restaurantBrandIdsString = "0";
		for (Count value : values) {
			restaurantBrandIdsString += " " + value.getName();
		}
		return restaurantBrandIdsString;
	}
}
