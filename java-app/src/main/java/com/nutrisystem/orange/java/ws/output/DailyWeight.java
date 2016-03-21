/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

/**
 * @author Wei Gao
 *
 */
public class DailyWeight {
    private String date;
    
    private String day;
    
    private Float weight; 

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

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }
}
