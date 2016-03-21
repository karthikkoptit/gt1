/**
 * 
 */
package com.nutrisystem.orange.java.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nutrisystem.orange.java.cache.DistributedCache;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

/**
 * @author Wei Gao
 * 
 */
public class GlassfishRestClient {

    /**
     * @author Wei Gao
     * 
     */
    private static final String SUCCESS = "SUCCESS";
    public static final String HOST_AND_PORT = "localhost:4848";
    public static final String GET = "GET";
    public static final String POST = "POST";

    public static JsonObject request(String url, String method) {
	TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	    public X509Certificate[] getAcceptedIssuers() {
		return null;
	    }

	    public void checkClientTrusted(X509Certificate[] certs, String authType) {
	    }

	    public void checkServerTrusted(X509Certificate[] certs, String authType) {
	    }
	} };
	SSLContext sslContext;
	Client client = null;
	try {
	    sslContext = SSLContext.getInstance("SSL");
	    sslContext.init(null, trustAllCerts, new SecureRandom());
	    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

	    ClientConfig config = new DefaultClientConfig();
	    Map<String, Object> properties = config.getProperties();
	    HTTPSProperties httpsProperties = new HTTPSProperties(new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
		    return true;
		}
	    }, sslContext);
	    properties.put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, httpsProperties);
	    client = Client.create(config);
	} catch (NoSuchAlgorithmException | KeyManagementException e) {
	    throw new RuntimeException(e);
	}
	client.addFilter(new HTTPBasicAuthFilter(DistributedCache.USERNAME, DistributedCache.PASSWORD));
	return sendRequest(client, url, method);
    }

    private static JsonObject sendRequest(Client client, String url, String method) {
	WebResource service = client.resource(url);
	String response = service.header("X-Requested-By", "jdas-app").accept(MediaType.APPLICATION_JSON)
		.method(method, String.class);
	JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
	String exitCode = jsonObject.getAsJsonPrimitive("exit_code").getAsString();
	if (!exitCode.equals(SUCCESS))
		throw new RuntimeException("glassfish REST API failed: " + method + " " + url);
	return jsonObject;
    }
}
