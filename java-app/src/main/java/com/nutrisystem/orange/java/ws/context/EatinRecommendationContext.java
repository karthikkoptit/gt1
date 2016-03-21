/**
 * 
 */
package com.nutrisystem.orange.java.ws.context;

import java.util.List;

/**
 * @author wgao
 *
 */
public final class EatinRecommendationContext {
	private final Integer totalTimeId;
	private final List<Integer> recipeServingsIdList;
	private final Boolean includeSide;
	private final Boolean nutrisystemOnly;
	
	public EatinRecommendationContext(Integer totalTimeId,
			List<Integer> recipeServingsIdList, Boolean includeSide, Boolean nutrisystemOnly) {
		super();
		this.totalTimeId = totalTimeId;
		this.recipeServingsIdList = recipeServingsIdList;
		this.includeSide = includeSide;
		this.nutrisystemOnly = nutrisystemOnly;
	}

	public Integer getTotalTimeId() {
		return totalTimeId;
	}

	public List<Integer> getRecipeServingsIdList() {
		return recipeServingsIdList;
	}

	public Boolean getIncludeSide() {
		return includeSide;
	}

	public Boolean getNutrisystemOnly() {
		return nutrisystemOnly;
	}
}
