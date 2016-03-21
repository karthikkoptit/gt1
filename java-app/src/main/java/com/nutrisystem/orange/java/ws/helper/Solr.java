/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Wei Gao
 * 
 */
@Component
public class Solr {
    public static final String FOOD_SEARCH_CORE = "collection1";
    
    public static final String FOOD_RECOMMENDATION_CORE = "collection2";
    
    public static final String LOCATION_SEARCH_CORE = "collection3";
    
    public static final int MAX_ROWS = Integer.MAX_VALUE;
    
    @Value("${solr.host.name}")
    private String solrHostName;

    private SolrServer solrServer;
    
    private Map<String, SolrServer> solrServerMap = new HashMap<>();
  
    public SolrServer getSolrServer() {
	if (solrServer == null) {
	    solrServer = new HttpSolrServer("http://" + solrHostName + ":8080/solr");
	}
	return solrServer;
    }
    
    public SolrServer getSolrServer(String core) {
	SolrServer server = solrServerMap.get(core);
	if (server == null) {
	    server = new HttpSolrServer("http://" + solrHostName + ":8080/solr/" + core);
	    solrServerMap.put(core, server);
	}
	return server;
    }
}
