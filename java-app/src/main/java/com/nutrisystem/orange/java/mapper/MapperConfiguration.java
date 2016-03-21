/**
 * 
 */
package com.nutrisystem.orange.java.mapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class MapperConfiguration {
    @Bean
    public UserProfileMapper userProfileMapper() {
	UserProfileMapper mapper = new UserProfileMapper();
	return mapper;
    }
    
    @Bean
    public UserProfileHistoryMapper userProfileHistoryMapper() {
	UserProfileHistoryMapper mapper = new UserProfileHistoryMapper();
	return mapper;
    }
}
