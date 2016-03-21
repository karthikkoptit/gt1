/**
 * 
 */
package com.nutrisystem.orange.java.ws.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Wei Gao
 * 
 */
@Component
public class Redis {
    @SuppressWarnings("rawtypes")
    @Autowired
    private RedisTemplate redisTemplate;

    public boolean isAlive() {
	RedisConnection connection = null;
	try {
	    connection = redisTemplate.getConnectionFactory().getConnection();
	    connection.ping();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	    return false;
	} 
    }
}
