/**
 * 
 */
package com.nutrisystem.orange.java.ws.context;

import java.util.List;

/**
 * @author wgao
 * 
 */
public final class CommonRecommendationContext {
	private final Integer eatinoutId;
	private final Integer timeBucketId;
	private final Integer occasionId;
	private final List<Integer> cuisineIdList;
	
	private final String outputType;

	public CommonRecommendationContext(Integer eatinoutId, Integer timeBucketId, Integer occasionId,
			List<Integer> cuisineIdList, String outputType) {
		super();
		this.eatinoutId = eatinoutId;
		this.timeBucketId = timeBucketId;
		this.occasionId = occasionId;
		this.cuisineIdList = cuisineIdList;
		this.outputType = outputType;
	}

	public Integer getEatinoutId() {
		return eatinoutId;
	}

	public String getOutputType() {
		return outputType;
	}

	public Integer getTimeBucketId() {
		return timeBucketId;
	}

	public Integer getOccasionId() {
		return occasionId;
	}

	public List<Integer> getCuisineIdList() {
		return cuisineIdList;
	}
}
