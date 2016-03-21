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
public class FoodLogOutput {
    private Long foodLogId;

    private String foodLogDate;

    private Integer timeBucketId;

    private String timeBucket;

    private Integer foodId;

    private String foodName;

    private String foodDescription;
    
    private String brandFoodName;

    private Integer calories;

    private Integer fat;

    private Integer saturatedFat;

    private Integer protein;

    private Integer carbohydrate;

    private Integer fiber;

    private Integer sugar;

    private Integer sodium;

    private BigDecimal servingSize;

    private Integer servingId;

    private String servingType;
    
    private Boolean custom;
    
    private Boolean scanned;

    @XmlElement(name = "food_log_id")
    public Long getFoodLogId() {
	return foodLogId;
    }

    public void setFoodLogId(Long foodLogId) {
	this.foodLogId = foodLogId;
    }

    @XmlElement(name = "food_log_date")
    public String getFoodLogDate() {
	return foodLogDate;
    }

    public void setFoodLogDate(String foodLogDate) {
	this.foodLogDate = foodLogDate;
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

    @XmlElement(name = "food_id")
    public Integer getFoodId() {
	return foodId;
    }

    public void setFoodId(Integer foodId) {
	this.foodId = foodId;
    }

    @XmlElement(name = "food_name")
    public String getFoodName() {
	return foodName;
    }

    public void setFoodName(String foodName) {
	this.foodName = foodName;
    }

    @XmlElement(name = "food_description")
    public String getFoodDescription() {
	return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
	this.foodDescription = foodDescription;
    }

    @XmlElement(name = "brand_food_name")
    public String getBrandFoodName() {
        return brandFoodName;
    }

    public void setBrandFoodName(String brandFoodName) {
        this.brandFoodName = brandFoodName;
    }

    public Integer getCalories() {
	return calories;
    }

    public void setCalories(Integer calories) {
	this.calories = calories;
    }

    public Integer getFat() {
	return fat;
    }

    public void setFat(Integer fat) {
	this.fat = fat;
    }

    @XmlElement(name = "saturated_fat")
    public Integer getSaturatedFat() {
	return saturatedFat;
    }

    public void setSaturatedFat(Integer saturatedFat) {
	this.saturatedFat = saturatedFat;
    }

    public Integer getProtein() {
	return protein;
    }

    public void setProtein(Integer protein) {
	this.protein = protein;
    }

    public Integer getCarbohydrate() {
	return carbohydrate;
    }

    public void setCarbohydrate(Integer carbohydrate) {
	this.carbohydrate = carbohydrate;
    }

    public Integer getFiber() {
	return fiber;
    }

    public void setFiber(Integer fiber) {
	this.fiber = fiber;
    }

    public Integer getSugar() {
	return sugar;
    }

    public void setSugar(Integer sugar) {
	this.sugar = sugar;
    }

    public Integer getSodium() {
	return sodium;
    }

    public void setSodium(Integer sodium) {
	this.sodium = sodium;
    }

    @XmlElement(name = "serving_size")
    public BigDecimal getServingSize() {
	return servingSize;
    }

    public void setServingSize(BigDecimal servingSize) {
	this.servingSize = servingSize;
    }

    public Integer getServingId() {
	return servingId;
    }

    public void setServingId(Integer servingId) {
	this.servingId = servingId;
    }

    @XmlElement(name = "serving_type")
    public String getServingType() {
	return servingType;
    }

    public void setServingType(String servingType) {
	this.servingType = servingType;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

	public Boolean getScanned() {
		return scanned;
	}

	public void setScanned(Boolean scanned) {
		this.scanned = scanned;
	}
}
