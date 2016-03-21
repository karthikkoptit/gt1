/**
 * 
 */
package com.nutrisystem.orange.java.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nutrisystem.orange.java.repository.app.DietRepository;
import com.nutrisystem.orange.java.repository.app.RadiusRepository;
import com.nutrisystem.orange.java.repository.app.TimeBucketRepository;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class LookupConfiguration {
    @Autowired 
    private DietRepository dietRepository;
    
    @Autowired
    private RadiusRepository radiusRepository;
    
    @Autowired
    private TimeBucketRepository timeBucketRepository;

    @Bean
    RadiusLookup radiusLookup() {
	RadiusLookup radiusLookup = new RadiusLookup();
	radiusLookup.setRadiusRepository(radiusRepository);
	radiusLookup.init();
	return radiusLookup;
    }
    
    @Bean
    DietLookup dietLookup() {
	DietLookup dietLookup = new DietLookup();
	dietLookup.setDietRepository(dietRepository);
	dietLookup.init();
	return dietLookup;
    }
    
    @Bean
    TimeBucketLookup timeBucketLookup() {
	TimeBucketLookup timeBucketLookup = new TimeBucketLookup();
	timeBucketLookup.setTimeBucketRepository(timeBucketRepository);
	timeBucketLookup.init();
	return timeBucketLookup;
    }
}
