/**
 * 
 */
package com.nutrisystem.orange.java.constant;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Wei Gao
 *
 */
public class AppConstant {
    public static final String DELIMITER = "|";
    public static final String NAMESPACE = "j:";
    public static final String SERVERLOG_PREFIX = "server.log_";
    public static final String SERVERLOG_RELATIVE_PATH = "/../logs";
    public static final String ACCESSLOG_PREFIX = "server_access_log.";
    public static final String ACCESSLOG_RELATIVE_PATH = "/../logs/access";
    public static final String ADMINACCESSLOG_PREFIX = "__asadmin_access_log.";
    public static final String ADMINACCESSLOG_RELATIVE_PATH = "/../logs/access";
    public static final String DATALOG_PREFIX = "data.";
    public static final String DATALOG_RELATIVE_PATH = "/../logs/access";
    private static String nodeName = null;

    public static final String getNodeName() {
	if (nodeName == null) {
	    Path currPath = Paths.get("");
	    Path path = currPath.toAbsolutePath().getParent();
	    nodeName = path.getFileName().toString();
	}
	return nodeName;
    }
    
    private AppConstant() {}
}
