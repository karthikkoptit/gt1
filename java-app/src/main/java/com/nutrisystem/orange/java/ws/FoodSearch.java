/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.ErrorMessage;
import com.nutrisystem.orange.java.ws.helper.FoodSearchParameterFactory;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.FoodSearchOutput;

/**
 * Provide restful service for Food Search Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/food/search/{session_id}")
public class FoodSearch {
    @Autowired
    private ValueOperations<String, String> stringValueOperations;

    @Autowired
    private Session session;

    @Autowired
    private Solr solr;
    
    @Autowired
    private FoodSearchParameterFactory foodSearchParameterFactory;

    @Autowired
    private ErrorMessage errorMessage;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public FoodSearchOutput foodSearch(@PathParam("session_id") String sessionId,
	    @QueryParam("food_id") Integer foodId, @DefaultValue("*:*") @QueryParam("q") String q,
	    @DefaultValue("1") @QueryParam("search_type") int searchType,
	    @DefaultValue("0") @QueryParam("start") int start, @DefaultValue("50") @QueryParam("rows") int rows) {

	FoodSearchOutput output = new FoodSearchOutput();
	output.setSessionId(sessionId);
	if (!session.exists(sessionId)) {
	    output.setStatus(Status.INVALID_SESSION_ID);
	    return output;
	}
	if (searchType < 1 || searchType > 4) {
	    output.setStatus(Status.ERROR);
	    output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "search_type"));
	    return output;
	}

	try {
	    SolrDocumentList solrDocumentList;
	    SolrQuery parameters = foodSearchParameterFactory.create(searchType, q, start, rows, foodId);
	    solrDocumentList = solr.getSolrServer().query(parameters).getResults();
	    output.setNumFound(solrDocumentList.getNumFound());
	    output.setDocs(solrDocumentList);
	    output.setStatus(Status.OK);
	} catch (SolrServerException e) {
	    output.setStatus(Status.SOLR_SERVER_ERROR);
	}
	return output;
    }

}
