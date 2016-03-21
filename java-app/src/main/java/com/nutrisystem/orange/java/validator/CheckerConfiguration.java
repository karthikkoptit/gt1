/**
 * 
 */
package com.nutrisystem.orange.java.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wei Gao
 *
 */
@Configuration
public class CheckerConfiguration {
    @Autowired
    private FoodLogValidator foodLogValidator;
    
    @Autowired
    private ActivityLogValidator activityLogValidator;
    
    @Bean
    public Checker foodLogChecker() {
	Checker checker = new Checker();
	checker.setValidator(foodLogValidator);
	return checker;
    }
    
    @Bean
    public  Checker activityLogChecker() {
	Checker checker = new Checker();
	checker.setValidator(activityLogValidator);
	return checker;
    }
}
