/**
 * 
 */
package com.nutrisystem.orange.java.calculator;

import java.math.BigDecimal;

import com.nutrisystem.orange.utility.data.UnitConversion;

/**
 * @author Wei Gao
 *
 */
public class Formula {
    public static float calcActivityCalories(float weight, BigDecimal mets, float duration) {
	return (float)calcActivityCalories((double)weight, mets.doubleValue(), (double)duration);
    }
    
    public static float calcDuration(int weight, BigDecimal mets, float calories) {
	return (float)calcDuration((double)weight, mets.doubleValue(), calories);
    }
    
    private static double calcDuration(double weight, double mets, double calories) {
	return calories * 200.0 / (mets * 3.5 * UnitConversion.lbToKg(weight));	
    }
    
    private static double calcActivityCalories(double weight, double mets, double duration) {
	return mets * 3.5 * UnitConversion.lbToKg(weight) * duration / 200.0;
    }
    
    public static float calcBMI(float weight, float height) {
	double heightM = UnitConversion.inchToCm((double)height) / 100.0;
	return (float)(UnitConversion.lbToKg((double)weight) / (heightM * heightM));
    }
    
    private Formula() {}
}
