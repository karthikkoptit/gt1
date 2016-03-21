/**
 * 
 */
package com.nutrisystem.orange.java.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.nutrisystem.orange.java.constant.ErrorCode;
import com.nutrisystem.orange.java.constant.Status;
import com.nutrisystem.orange.java.ws.helper.Solr;
import com.nutrisystem.orange.java.ws.output.PingOutput;

/**
 * @author Wei Gao
 * 
 */
@Component
@Path("/ping")
public class Ping {
    @Autowired
    private ValueOperations<String, String> stringValueOperations;
    
    @Autowired
    private Solr solr;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PingOutput ping(@PathParam("session_id") String sessionId) throws Exception {
	PingOutput output = new PingOutput();

	// test solr server
	SolrPingResponse response = solr.getSolrServer().ping();
	if (response.getStatus() != 0) 
	    throw new RuntimeException(ErrorCode.SOLR_SERVER_ERROR);

	output.setStatus(Status.OK);
	return output;
    }
}
