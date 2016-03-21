/**
 * 
 */
package com.nutrisystem.orange.java.cache;

import java.util.Properties;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.nutrisystem.orange.java.util.GlassfishRestClient;
import com.sun.enterprise.ee.cms.core.CallBack;
import com.sun.enterprise.ee.cms.core.DistributedStateCache;
import com.sun.enterprise.ee.cms.core.GMSConstants;
import com.sun.enterprise.ee.cms.core.GMSException;
import com.sun.enterprise.ee.cms.core.GMSFactory;
import com.sun.enterprise.ee.cms.core.GroupManagementService;
import com.sun.enterprise.ee.cms.core.Signal;
import com.sun.enterprise.ee.cms.core.SignalAcquireException;
import com.sun.enterprise.ee.cms.core.SignalReleaseException;
import com.sun.enterprise.ee.cms.impl.client.JoinNotificationActionFactoryImpl;
import com.sun.enterprise.ee.cms.impl.client.JoinedAndReadyNotificationActionFactoryImpl;

/**
 * @author Wei Gao
 * 
 */
public class DistributedCache implements CallBack {
	private static final String GROUP_NAME = "java-app-group";
	private static final String SERVER_NAME = "java-app-server-" + UUID.randomUUID();
	private static final String SEQUENCE = "sequence";
	private static String dasPort;
	private GroupManagementService gms;
	private DistributedStateCache cache;
	private String testSequenceName;
	private String[] options;

	public static String USERNAME;
	public static String PASSWORD;

	public void init() {
		options = System.getProperty("sun.java.command").split(",,,");
		dasPort = options[3];
		Properties properties = new Properties();
		properties.setProperty("DISCOVERY_URI_LIST", getDiscoveryUriList());
		properties.setProperty("DISCOVERY_TIMEOUT", "15000");
		properties.setProperty("TCPSTARTPORT", "9999");
		properties.setProperty("TCPENDPORT", "9999");

		Runnable gmsRunnable = GMSFactory.startGMSModule(SERVER_NAME, GROUP_NAME,
				GroupManagementService.MemberType.CORE, properties);
		gms = (GroupManagementService) gmsRunnable;
		gms.addActionFactory(new JoinNotificationActionFactoryImpl(this));
		gms.addActionFactory(new JoinedAndReadyNotificationActionFactoryImpl(this));
		try {
			gms.join();
			cache = gms.getGroupHandle().getDistributedStateCache();
		} catch (GMSException e) {
			throw new RuntimeException(e);
		}
	}

	public String getDasHost() {
		return options[1];
	}

	public void cleanup() {
		gms.shutdown(GMSConstants.shutdownType.INSTANCE_SHUTDOWN);
	}

	public void updateSequence(String sequenceName, Long sequenceId) throws GMSException {
		cache.addToCache(GROUP_NAME, SEQUENCE, sequenceName, sequenceId);
	}

	public boolean isHealthy() {
		try {
			currVal(testSequenceName);
			return true;
		} catch (GMSException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Long currVal(String sequenceName) throws GMSException {
		return (Long) cache.getFromCache(GROUP_NAME, SEQUENCE, sequenceName);
	}

	public void processNotification(Signal signal) {
		try {
			signal.acquire();
			signal.release();
		} catch (SignalAcquireException e) {
		} catch (SignalReleaseException e) {
		}
	}

	private String getDiscoveryUriList() {
		JsonObject jsonObject = GlassfishRestClient.request("https://" + getDasHost() + ":" + dasPort
				+ "/management/domain/clusters/cluster/jscluster/property/GMS_DISCOVERY_URI_LIST",
				GlassfishRestClient.GET);
		String gmsDiscoveryUriList = jsonObject.getAsJsonObject("extraProperties").getAsJsonObject("entity")
				.getAsJsonPrimitive("value").getAsString();
		return gmsDiscoveryUriList.split(",", 2)[1];
	}

	public void setTestSequenceName(String testSequenceName) {
		this.testSequenceName = testSequenceName;
	}

	public static void setUSERNAME(String username) {
		USERNAME = username;
	}

	public static void setPASSWORD(String password) {
		PASSWORD = password;
	}
}
