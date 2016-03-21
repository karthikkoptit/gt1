/**
 * 
 */
package com.nutrisystem.orange.java.ws.output;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Wei Gao
 * 
 */
public class ActivityLogOutput {
    private Long activityLogId;

    private String activityLogDate;

    private Integer timeBucketId;

    private String timeBucket;

    private Integer activityId;
    
    private String category;
    
    private String details;

    private Integer calories;

    private BigDecimal mets;

    private Integer duration;

    private String iconKey;
    
    private Boolean device;
    
    private Boolean custom;
    
    @XmlElement(name = "activity_log_id")
    public Long getActivityLogId() {
	return activityLogId;
    }

    public void setActivityLogId(Long activityLogId) {
	this.activityLogId = activityLogId;
    }

    @XmlElement(name = "activity_log_date")
    public String getActivityLogDate() {
	return activityLogDate;
    }

    public void setActivityLogDate(String activityLogDate) {
	this.activityLogDate = activityLogDate;
    }

    @XmlElement(name = "time_bucket_id")
    public Integer getTimeBucketId() {
	return timeBucketId;
    }

    public void setTimeBucketId(Integer timeBucketId) {
	this.timeBucketId = timeBucketId;
    }

    @XmlElement(name = "time_bucket")
    public String getTimeBucket() {
	return timeBucket;
    }

    public void setTimeBucket(String timeBucket) {
	this.timeBucket = timeBucket;
    }

    @XmlElement(name = "activity_id")
    public Integer getActivityId() {
	return activityId;
    }

    public void setActivityId(Integer activityId) {
	this.activityId = activityId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public BigDecimal getMets() {
        return mets;
    }

    public void setMets(BigDecimal mets) {
        this.mets = mets;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @XmlElement(name="icon_key")
    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public Boolean getDevice() {
        return device;
    }

    public void setDevice(Boolean device) {
        this.device = device;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }
}
