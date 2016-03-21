/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.OutputFormat;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.SingleFoodSearchOutput;

/**
 * Provide restful service for Serving Search Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/singlefood/search/{session_id}")
public class SingleFoodSearch {
    @Autowired
    private ValueOperations<String, String> stringValueOperations;

    @Autowired
    private Session session;

    @Autowired
    private Solr solr;

    @Autowired
    private ErrorMessage errorMessage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SingleFoodSearchOutput singleFoodSearch(@PathParam("session_id") String sessionId,
	    @QueryParam("food_id") Integer foodId) {

	SingleFoodSearchOutput output = new SingleFoodSearchOutput();
	output.setSessionId(sessionId);
	try {
	    if (session.exists(sessionId)) {
		if (foodId == null) {
		    output.setStatus(Status.ERROR);
		    output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "food_id"));
		} else {
		    SolrQuery parameters = new SolrQuery();
		    parameters.setQuery("food_id:" + foodId + " AND data_type:food");
		    parameters.set("wt", OutputFormat.JSON);
		    SolrDocumentList solrDocumentList = solr.getSolrServer().query(parameters).getResults();
		    output.setNumFound(solrDocumentList.getNumFound());
		    SolrDocument foodDocument = solrDocumentList.get(0);
		    output.setDocs(solrDocumentList);
		    
		    if (solrDocumentList.getNumFound() != 0) {
			parameters.setQuery("food_id:" + foodId + " AND data_type:serving");
			List<SortClause> sortClauseList = new ArrayList<>();
			sortClauseList.add(new SortClause("serving_seq", ORDER.asc));
			parameters.setSorts(sortClauseList);
			parameters.set("wt", OutputFormat.JSON);
			solrDocumentList = solr.getSolrServer().query(parameters).getResults();
			foodDocument.put("serving_list", solrDocumentList);
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
}
