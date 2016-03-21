package com.nutrisystem.orange.java.ws;

import static com.jayway.restassured.RestAssured.expect;
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.post;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class ServiceTest extends FoodRecommendation {

	@Before
	public void setUp() throws Exception {
		//RestAssured.baseURI = "http://10.128.1.55";
		//RestAssured.port = 28080;
		//RestAssured.baseURI="http://jlb-testing.numi.com";
		RestAssured.baseURI="http://jlb-dev.numi.com";
		RestAssured.port = 80;
		// RestAssured.port = 30080;
	}

	@Test
	public void testFoodSearch() {
		String sessionId = get("/java-app/v1/login/7").body().jsonPath().getString("session_id");
		expect().statusCode(200).body("num_found", greaterThan(0)).when()
				.get("/java-app/v1/food/search/" + sessionId + "?q=salad&start=1&rows=3");
		expect().statusCode(200).body("num_found", equalTo(1)).when()
				.get("/java-app/v1/serving/search/" + sessionId + "?food_id=22794");
		expect().statusCode(200).body("status", equalTo("OK")).when().get("/java-app/v1/logout/" + sessionId);
	}

	@Test
	public void testFoodRecommend() {
		String sessionId = get("/java-app/v1/login/7").body().jsonPath().getString("session_id");
		expect().statusCode(200)
				.body("num_found", greaterThan(0))
				.when()
				.get("/java-app/v1/food/recommendation/"
						+ sessionId
						+ "?q=salad&eatinout_id=1&occasion_id=2&time_bucket_id=2&total_time_id=2&recipe_servings_id[]=2&recipe_servings_id[]=3&rows=3");
		expect().statusCode(200).body("status", equalTo("OK")).when().get("/java-app/v1/logout/" + sessionId);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testFoodLogging() {
		String sessionId = get("/java-app/v1/login/7").body().jsonPath().getString("session_id");
		Response response = post("/java-app/v1/food/logging/" + sessionId
				+ "?log_date=2013-07-18&time_bucket_id=1&food_id=23999&serving_size=1.6667&serving_id=94480");
		assertEquals(200, response.getStatusCode());
		Integer[] foodLogIds = new Integer[3];
		foodLogIds[0] = response.jsonPath().getInt("food_log_id");

		response = post("/java-app/v1/food/logging/" + sessionId
				+ "?log_date=2013-07-18&time_bucket_id=2&food_id=23999&serving_size=0.3333&serving_id=94480");
		assertEquals(200, response.getStatusCode());
		foodLogIds[1] = response.jsonPath().getInt("food_log_id");

		response = post("/java-app/v1/food/logging/" + sessionId
				+ "?log_date=2013-07-18&time_bucket_id=3&food_id=23999&serving_size=1.125&serving_id=94480");
		assertEquals(200, response.getStatusCode());
		foodLogIds[2] = response.jsonPath().getInt("food_log_id");

		// test read
		response = given().param("time_bucket_id[]", "1", "3").when()
				.get("/java-app/v1/food/logging/" + sessionId + "?log_date_start=2013-07-18");
		assertEquals(200, response.getStatusCode());
		List<Object> resultList = response.jsonPath().getList("results");
		assertEquals(2, resultList.size());

		// test update
		expect().statusCode(200).body("status", equalTo("OK")).when()
				.put("/java-app/v1/food/logging/" + sessionId + "?food_log_id=" + foodLogIds[0] + "&serving_size=0.25");
		response = get("/java-app/v1/food/logging/" + sessionId + "?log_date_start=2013-07-18&time_bucket_id[]=1");
		assertEquals(200, response.getStatusCode());
		resultList = response.jsonPath().getList("results");
		assertEquals(0.25, (Float) ((HashMap) resultList.get(0)).get("serving_size"), 0.00001);

		// test delete
		for (Integer foodLogId : foodLogIds) {
			expect().statusCode(200).body("status", equalTo("OK")).when()
					.delete("/java-app/v1/food/logging/" + sessionId + "?food_log_id=" + String.valueOf(foodLogId));
		}

		expect().statusCode(200).body("status", equalTo("OK")).when().get("/java-app/v1/logout/" + sessionId);
	}

	@SuppressWarnings("rawtypes")
	@Test
	public void testActivityLogging() {
		// login
		String sessionId = get("/java-app/v1/login/7").body().jsonPath().getString("session_id");

		// create
		Response response = post("/java-app/v1/activity/logging/" + sessionId
				+ "?log_date=2013-07-18&time_bucket_id=1&activity_id=300&duration=20");
		assertEquals(200, response.getStatusCode());
		Integer[] activityLogIds = new Integer[3];
		activityLogIds[0] = response.jsonPath().getInt("activity_log_id");

		response = post("/java-app/v1/activity/logging/" + sessionId
				+ "?log_date=2013-07-18&time_bucket_id=2&activity_id=300&duration=25");
		assertEquals(200, response.getStatusCode());
		activityLogIds[1] = response.jsonPath().getInt("activity_log_id");

		response = post("/java-app/v1/activity/logging/" + sessionId
				+ "?log_date=2013-07-18&time_bucket_id=3&activity_id=300&duration=35");
		assertEquals(200, response.getStatusCode());
		activityLogIds[2] = response.jsonPath().getInt("activity_log_id");

		// read
		response = given().param("time_bucket_id[]", "1", "3").get(
				"/java-app/v1/activity/logging/" + sessionId + "?log_date_start=2013-07-18");
		assertEquals(200, response.getStatusCode());
		List<Object> resultList = response.jsonPath().getList("results");
		assertEquals("Morning", (String) ((HashMap) resultList.get(0)).get("time_bucket"));
		assertEquals("Evening", (String) ((HashMap) resultList.get(1)).get("time_bucket"));

		// update
		expect().statusCode(200)
				.body("status", equalTo("OK"))
				.when()
				.put("/java-app/v1/activity/logging/" + sessionId + "?activity_log_id=" + activityLogIds[0]
						+ "&duration=12");
		response = get("/java-app/v1/activity/logging/" + sessionId + "?log_date_start=2013-07-18&time_bucket_id[]=1");
		assertEquals(200, response.getStatusCode());
		resultList = response.jsonPath().getList("results");
		assertEquals((Integer) 12, (Integer) ((HashMap) resultList.get(0)).get("duration"));

		// delete
		for (Integer activityLogId : activityLogIds) {
			expect().statusCode(200)
					.body("status", equalTo("OK"))
					.when()
					.delete("/java-app/v1/activity/logging/" + sessionId + "?activity_log_id="
							+ String.valueOf(activityLogId));
		}

		// logout
		expect().statusCode(200).body("status", equalTo("OK")).when().get("/java-app/v1/logout/" + sessionId);
	}

	@Test
	public void testProgress() {
		// login
		String sessionId = get("/java-app/v1/login/7").body().jsonPath().getString("session_id");

		// progress data retrieval
		expect().statusCode(200).body("status", equalTo("OK")).when()
				.get("/java-app/v1/progress/" + sessionId + "?progress_date=2014-01-21");

		// logout
		expect().statusCode(200).body("status", equalTo("OK")).when().get("/java-app/v1/logout/" + sessionId);
	}
}
