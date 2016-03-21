/**
 * 
 */
package com.nutrisystem.orange.java.constant;


/**
 * @author Wei Gao
 * 
 */
public class ErrorCode {
    private ErrorCode() {
    }

    public static final String NULL_OBJECT = "null.object";
    public static final String OUT_OF_RANGE = "out.of.range";
    public static final String WRONG_DATE_FORMAT = "wrong.date.format";
    public static final String REQUIRED = "required";
    public static final String INVALID = "invalid";
    public static final String INVALID_SESSION_ID = "invalid.sessionid";
    public static final String ERROR = "error";
    
    public static final String INVALID_USER_ID = "INVALID_USER_ID";
    
    public static final String INVALID_TIME_BUCKET_ID = "INVALID_TIME_BUCKET_ID";
    public static final String INVALID_LOG_DATE = "INVALID_LOG_DATE";
    public static final String SOLR_SERVER_ERROR ="SOLR_SERVER_ERROR";
    public static final String REDIS_SERVER_ERROR = "REDIS_SERVER_ERROR";
    
    //App errors
    public static final String MEAL_CALS_OVER = "MEAL_CALS_OVER";
    public static final String MEAL_NO_RESULTS = "MEAL_NO_RESULTS";
    public static final String INVALID_ZIPCODE = "INVALID_ZIPCODE";
}
