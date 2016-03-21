/**
 * 
 */
package com.nutrisystem.orange.java.range;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class RangeConfiguration {
    @Autowired
    private TimeBucketRepository timeBucketRepository;
    
    @Bean
    public IntegerRange timeBucketIdRange() {
	IntegerRange range = new IntegerRange();
	range.setMin(timeBucketRepository.findMinTimeBucketId());
	range.setMax(timeBucketRepository.findMaxTimeBucketId());
	return range;
    }
}
