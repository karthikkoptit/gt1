/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.OutputFormat;
import com.nutrisystem.orange.java.text.EnglishTextParser;
import com.nutrisystem.share.constant.FoodCategory;

/**
 * @author Wei Gao
 * 
 */
@Component
public class FoodSearchParameterFactory {
    private static final String MAX_BOOST = "9999999999";

    public static final int FOOD_ID_SEARCH_TYPE = 0;
    private static final int ALL_FOOD_SEARCH_TYPE = 1;
    private static final int RESTAURANT_FOOD_SEARCH_TYPE = 2;
    private static final int RECIPE_SEARCH_TYPE = 3;
    private static final int BRANDED_FOOD_SEARCH_TYPE = 4;

    private static final String FOOD_NAME_FIELD = "food_name";
    private static final String RESTAURANT_BRAND_FIELD = "restaurant_brand";
    private static final String MANUFACTURER_BRAND_FIELD = "manufacturer_brand";
    private static final String INGREDIENTS_FIELD = "ingredients";
    private static final String FOOD_CATEGORY_ID_FIELD = "food_category_id";

    @Autowired
    private Solr solr;
    
    @Autowired
    private SearchHelper searchHelper;

    public SolrQuery create(int searchType, String q, int start, int rows, Integer foodId) throws SolrServerException {
	SolrQuery parameters = new SolrQuery();
	parameters.setFilterQueries("data_type:food");
	parameters.set("wt", OutputFormat.JSON);
	parameters.setStart(start);
	parameters.setRows(rows);
	q = searchHelper.filterQ(q);

	if (foodId != null)
	    searchType = FOOD_ID_SEARCH_TYPE;

	switch (searchType) {
	    case FOOD_ID_SEARCH_TYPE:
		parameters.setQuery("food_id:" + foodId);
		break;
	    case ALL_FOOD_SEARCH_TYPE:
		String elevatedFoodIdsString = getElevatedFoodIdsString(q);
		if (elevatedFoodIdsString == null)
		    parameters.setQuery(q);
		else
		    parameters.setQuery("(" + q + ") (elevate_keyword:(" + q + ")^" + MAX_BOOST + " AND food_id:("
			    + elevatedFoodIdsString + "))");
		break;
	    case RESTAURANT_FOOD_SEARCH_TYPE:
		parameters.setFilterQueries(FOOD_CATEGORY_ID_FIELD + ":" + FoodCategory.RESTAURANT_INT);
		parameters.setQuery(RESTAURANT_BRAND_FIELD + ":(" + q + ")^" + MAX_BOOST + " " + FOOD_NAME_FIELD + ":("
			+ q + ")");
		break;
	    case RECIPE_SEARCH_TYPE:
		parameters.setFilterQueries(FOOD_CATEGORY_ID_FIELD + ":" + FoodCategory.RECIPE_INT);
		parameters.setQuery(FOOD_NAME_FIELD + ":(" + q + ")^" + MAX_BOOST + " " + INGREDIENTS_FIELD + ":(" + q
			+ ")");
		break;
	    case BRANDED_FOOD_SEARCH_TYPE:
		parameters.setFilterQueries(FOOD_CATEGORY_ID_FIELD + ":" + FoodCategory.PACKAGED_INT);
		parameters.setQuery(MANUFACTURER_BRAND_FIELD + ":(" + q + ")^" + MAX_BOOST + " " + FOOD_NAME_FIELD
			+ ":(" + q + ")");
		break;
	    default:
		throw new RuntimeException("search_type is invalid.");
	}
	if (foodId == null) {
	} else {
	    parameters.setQuery("food_id:" + foodId);
	}

	return parameters;
    }

    private String getElevatedFoodIdsString(String q) throws SolrServerException {
	int keywordCount = EnglishTextParser.parse(q).size();
	SolrQuery parameters = new SolrQuery();
	parameters.setRows(0);
	parameters.set("wt", "json");
	parameters.setFacet(true);
	parameters.setFacetLimit(-1);
	parameters.setFacetMinCount(1);
	parameters.addFilterQuery("elevate_keyword_count:" + keywordCount);
	parameters.addFilterQuery("data_type:elevate_keyword");
	parameters.setQuery("{!lucene q.op=AND} elevate_keyword:(" + q + ") ");
	parameters.addFacetField("food_id");

	FacetField field;
	field = solr.getSolrServer().query(parameters).getFacetField("food_id");
	List<Count> values = field.getValues();
	if (values.size() == 0)
	    return null;

	String elevatedFoodIdsString = "0";
	for (Count value : values) {
	    elevatedFoodIdsString += " " + value.getName();
	}
	return elevatedFoodIdsString;
    }
}
