/**
 * 
 */
package com.nutrisystem.orange.java.ws.output.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import com.nutrisystem.orange.java.constant.OutputFormat;
import com.nutrisystem.orange.java.entity.diyapp.FoodLog;
import com.nutrisystem.orange.java.entity.diyapp.Serving;
import com.nutrisystem.orange.java.entity.diyfdb.CustomFood;
import com.nutrisystem.orange.java.repository.app.ServingRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;
import com.nutrisystem.orange.java.repository.fdb.CustomFoodRepository;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.FoodLogOutput;

/**
 * @author Wei Gao
 * 
 */
public class FoodLogOutputMapper {
	private ServingRepository servingRepository;

	private TimeBucketRepository timeBucketRepository;

	private CustomFoodRepository customFoodRepository;

	private Solr solr;

	public FoodLogOutput mapping(FoodLog foodLog) {
		if (foodLog == null)
			return null;

		FoodLogOutput foodLogOutput = new FoodLogOutput();
		foodLogOutput.setFoodLogId(foodLog.getFoodLogId());
		foodLogOutput.setFoodLogDate(foodLog.getFoodLogDate());
		foodLogOutput.setFoodId(foodLog.getFoodId());

		foodLogOutput.setTimeBucketId(foodLog.getTimeBucketId());
		foodLogOutput.setTimeBucket(timeBucketRepository.findOne(foodLog.getTimeBucketId()).getTimeBucket());

		float coefficient;
		Serving serving = null;
		if (foodLog.getCustom()) { // custom food
			CustomFood food = customFoodRepository.findOne(foodLog.getFoodId());
			foodLogOutput.setFoodName(food.getName());
			foodLogOutput.setFoodDescription(food.getName());
			coefficient = 1.0f * foodLog.getServingSize().floatValue();
			foodLogOutput.setCalories(Math.round(food.getCalories().floatValue() * coefficient));
			foodLogOutput.setFat(Math.round(food.getTotalFat().floatValue() * coefficient));
			foodLogOutput.setSaturatedFat(Math.round(food.getSaturatedFat().floatValue() * coefficient));
			foodLogOutput.setProtein(Math.round(food.getProtein().floatValue() * coefficient));
			foodLogOutput.setCarbohydrate(Math.round(food.getTotalCarbohydrate().floatValue() * coefficient));
			foodLogOutput.setFiber(Math.round(food.getFiber().floatValue() * coefficient));
			foodLogOutput.setSugar(Math.round(food.getSugar().floatValue() * coefficient));
			foodLogOutput.setSodium(Math.round(food.getSodium().floatValue() * coefficient));
		} else {
			SolrDocument solrDocument = null;
			try {
				SolrQuery parameters = new SolrQuery();
				parameters.setQuery("food_id:" + foodLog.getFoodId() + " AND data_type:food");
				parameters.set("wt", OutputFormat.JSON);
				SolrDocumentList solrDocumentList = solr.getSolrServer().query(parameters).getResults();
				solrDocument = solrDocumentList.get(0);
			} catch (SolrServerException e) {
				throw new RuntimeException(e);
			}

			foodLogOutput.setFoodName((String) solrDocument.getFieldValue("food_name"));
			foodLogOutput.setFoodDescription((String) solrDocument.getFieldValue("food_description"));
			foodLogOutput.setBrandFoodName((String) solrDocument.getFieldValue("brand_food_name"));
			serving = servingRepository.findOne(foodLog.getServingId());
			coefficient = serving.getCoefficient() * foodLog.getServingSize().floatValue();
			Object caloriesObject = solrDocument.getFieldValue("calories");
			foodLogOutput.setCalories(caloriesObject == null ? null : Math.round((float) caloriesObject * coefficient));
			Object fatObject = solrDocument.getFieldValue("fat");
			foodLogOutput.setFat(fatObject == null ? null : Math.round((float) fatObject * coefficient));
			Object saturatedFatObject = solrDocument.getFieldValue("saturated_fat");
			foodLogOutput.setSaturatedFat(saturatedFatObject == null ? null : Math.round((float) saturatedFatObject
					* coefficient));
			Object proteinObject = solrDocument.getFieldValue("protein");
			foodLogOutput.setProtein(proteinObject == null ? null : Math.round((float) proteinObject * coefficient));
			Object carbohydrateObject = solrDocument.getFieldValue("carbohydrate");
			foodLogOutput.setCarbohydrate(carbohydrateObject == null ? null : Math.round((float) carbohydrateObject
					* coefficient));
			Object fiberObject = solrDocument.getFieldValue("fiber");
			foodLogOutput.setFiber(fiberObject == null ? null : Math.round((float) fiberObject * coefficient));
			Object sugarObject = solrDocument.getFieldValue("sugar");
			foodLogOutput.setSugar(sugarObject == null ? null : Math.round((float) sugarObject * coefficient));
			Object sodiumObject = solrDocument.getFieldValue("sodium");
			foodLogOutput.setSodium(sodiumObject == null ? null : Math.round((float) sodiumObject * coefficient));
		}

		foodLogOutput.setScanned(foodLog.getScanned());
		foodLogOutput.setServingSize(foodLog.getServingSize());
		foodLogOutput.setServingId(foodLog.getServingId());
		foodLogOutput.setServingType(foodLog.getServingId() == 0 ? "serving" : serving.getServingTypeDescription());
		foodLogOutput.setCustom(foodLog.getCustom());
		return foodLogOutput;
	}

	public List<FoodLogOutput> mapping(List<FoodLog> foodLogList) {
		if (foodLogList == null)
			return null;

		List<FoodLogOutput> foodLogOutputList = new ArrayList<>();
		for (FoodLog foodLog : foodLogList) {
			foodLogOutputList.add(mapping(foodLog));
		}
		return foodLogOutputList;
	}

	public void setServingRepository(ServingRepository servingRepository) {
		this.servingRepository = servingRepository;
	}

	public void setTimeBucketRepository(TimeBucketRepository timeBucketRepository) {
		this.timeBucketRepository = timeBucketRepository;
	}

	public void setCustomFoodRepository(CustomFoodRepository customFoodRepository) {
		this.customFoodRepository = customFoodRepository;
	}

	public void setSolr(Solr solr) {
		this.solr = solr;
	}
}
