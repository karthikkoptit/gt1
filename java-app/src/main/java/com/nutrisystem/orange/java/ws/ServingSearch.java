/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
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
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.OutputFormat;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.Session;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.ServingSearchOutput;

/**
 * Provide restful service for Serving Search Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/serving/search/{session_id}")
public class ServingSearch {
    @Autowired
    private ValueOperations<String, String> stringValueOperations;

    @Autowired
    private Session session;

    @Autowired
    private Solr solr;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ServingSearchOutput servingSearch(@PathParam("session_id") String sessionId,
	    @DefaultValue("*") @QueryParam("food_id") String food_id, @DefaultValue("0") @QueryParam("start") int start,
	    @DefaultValue("50") @QueryParam("rows") int rows) {

	ServingSearchOutput output = new ServingSearchOutput();
	output.setSessionId(sessionId);
	try {
	    if (session.exists(sessionId)) {
		SolrQuery parameters = new SolrQuery();
		parameters.setQuery("food_id:" + food_id + " AND data_type:serving");
		parameters.setStart(start);
		parameters.setRows(rows);
		List<SortClause> sortClauseList = new ArrayList<>(); 
		sortClauseList.add(new SortClause("food_id", ORDER.asc));
		sortClauseList.add(new SortClause("serving_seq", ORDER.asc));
		parameters.setSorts(sortClauseList);
		parameters.set("wt", OutputFormat.JSON);
		SolrDocumentList solrDocumentList = solr.getSolrServer().query(parameters).getResults();
		output.setNumFound(solrDocumentList.getNumFound());
		output.setDocs(solrDocumentList);
		output.setStatus(Status.OK);
	    } else
		output.setStatus(Status.INVALID_SESSION_ID);
	} catch (SolrServerException e) {
	    output.setStatus(Status.SOLR_SERVER_ERROR);
	}
	return output;
    }
}
