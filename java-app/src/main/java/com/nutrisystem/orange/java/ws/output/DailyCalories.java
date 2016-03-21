/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

/**
 * @author Wei Gao
 *
 */
public class DailyCalories extends AbstractCalories {
    private String date;
    
    private String day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
