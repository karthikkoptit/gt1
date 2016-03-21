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
import org.apache.solr.client.solrj.SolrServerException;
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
import com.nutrisystem.orange.java.ws.output.FoodSearchOutput;
import com.nutrisystem.orange.utility.data.UpcUtil;

/**
 * Provide restful service for Food Search Task CA-1224
 * 
 * @author Wei Gao
 * 
 */
@Component
@Path("/upc/search/{session_id}")
public class UpcSearch {
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
    public FoodSearchOutput upcSearch(@PathParam("session_id") String sessionId, @QueryParam("upc") String upc) {

	FoodSearchOutput output = new FoodSearchOutput();
	output.setSessionId(sessionId);
	try {
	    if (session.exists(sessionId)) {
		SolrQuery parameters = new SolrQuery();
		if (upc == null) {
		    output.setStatus(Status.ERROR);
		    output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.REQUIRED, "upc"));
		} else {
		    switch (upc.length()) {
		    case 8:
			upc = UpcUtil.convertUPCEtoEAN(upc);
			break;
		    case 12:
			upc = UpcUtil.convertUPCAtoEAN(upc);
			break;
		    case 13:
			break;
		    default:
			output.setStatus(Status.ERROR);
			output.setErrorMessage(errorMessage.getErrorMessage(ErrorCode.INVALID, "upc"));
		    }
		    parameters.setQuery("upc:" + upc + " AND data_type:food");
		    parameters.set("wt", OutputFormat.JSON);
		    SolrDocumentList solrDocumentList = solr.getSolrServer().query(parameters).getResults();
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
