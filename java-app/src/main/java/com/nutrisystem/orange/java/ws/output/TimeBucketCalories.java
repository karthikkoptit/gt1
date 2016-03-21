/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import javax.xml.bind.annotation.XmlElement;


/**
 * @author Wei Gao
 *
 */
public class TimeBucketCalories extends AbstractCalories {
    private String timeBucket;
    
    private Integer activityPlusMinus;

    @XmlElement(name="activity_plus_minus")
    public Integer getActivityPlusMinus() {
        return activityPlusMinus;
    }

    public void setActivityPlusMinus(Integer activityPlusMinus) {
        this.activityPlusMinus = activityPlusMinus;
    }

    @XmlElement(name="time_bucket")
    public String getTimeBucket() {
        return timeBucket;
    }

    public void setTimeBucket(String timeBucket) {
        this.timeBucket = timeBucket;
    }
}
