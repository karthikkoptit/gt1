package com.nutrisystem.orange.java.ws;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.solr.client.solrj.SolrServerException;

import com.nutrisystem.orange.java.constant.AppConstant;
import com.nutrisystem.orange.java.ws.helper.SearchHelper;
import com.nutrisystem.orange.utility.date.DateUtil;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */

	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws SolrServerException
	 */
	public void testApp() throws Exception {
		InetAddress host = InetAddress.getByName("www.numi.com");
		System.out.println(host.getHostAddress());
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		String fingerprint = host.getHostName() + host.getHostAddress();
		System.out.println(new String(md5.digest(fingerprint.getBytes()), "UTF-8"));

		String a = "nutrisystem,eggs salad, chicken,34 432,salad 432, salad";
		System.out.println(a.replaceAll("\\W", " "));
		System.out.println("path: " + AppConstant.getNodeName());
		
		System.out.println((new SearchHelper()).filterQ("\"10\" pizza\""));
		System.out.println((new SearchHelper()).filterQ("10/pizza"));
		assertTrue(true);
	}

	
	public void test() throws Exception {
		Date localTime = DateUtil.getLocalTime("US/Central");
		System.out.println("localtime = "+ localTime);
		System.out.println("localtimestring = " + DateUtil.getLocalTimeString("US/Central"));
		Date startTime = DateUtil.getDateTime("2014-07-14" + " "
				+ "12:00");
		Date endTime = DateUtil.getDateTime("2014-07-14" + " "
				+ "17:00");
		System.out.println(((localTime.after(startTime) || localTime.equals(startTime)) && localTime.before(endTime)));
	}
}
