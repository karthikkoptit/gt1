package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the food database table.
 * 
 */
@Entity
@Table(name="food")
@NamedQuery(name="Food.findAll", query="SELECT f FROM Food f")
public class Food implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="food_id", unique=true, nullable=false)
	private Integer foodId;

	@Column(nullable=false)
	private float boost;

	@Column(name="food_calories", nullable=false)
	private Float foodCalories;

	@Column(name="food_carbohydrate")
	private Float foodCarbohydrate;

	@Column(name="food_category_id", nullable=false)
	private Integer foodCategoryId;

	@Column(name="food_description", length=2147483647)
	private String foodDescription;

	@Column(name="food_fat")
	private Float foodFat;

	@Column(name="food_fiber")
	private Float foodFiber;

	@Column(name="food_keywords", length=2147483647)
	private String foodKeywords;

	@Column(name="food_name", nullable=false, length=255)
	private String foodName;

	@Column(name="food_protein")
	private Float foodProtein;

	@Column(name="food_saturated_fat")
	private Float foodSaturatedFat;

	@Column(name="food_sodium")
	private Float foodSodium;

	@Column(name="food_sugar")
	private Float foodSugar;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(nullable=false)
	private Boolean recommend;

	@Column(name="source_food_id", nullable=false, length=2147483647)
	private String sourceFoodId;

	@Column(name="source_id", nullable=false)
	private Integer sourceId;

	public Food() {
	}

	public Integer getFoodId() {
		return this.foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public float getBoost() {
		return this.boost;
	}

	public void setBoost(float boost) {
		this.boost = boost;
	}

	public Float getFoodCalories() {
		return this.foodCalories;
	}

	public void setFoodCalories(Float foodCalories) {
		this.foodCalories = foodCalories;
	}

	public Float getFoodCarbohydrate() {
		return this.foodCarbohydrate;
	}

	public void setFoodCarbohydrate(Float foodCarbohydrate) {
		this.foodCarbohydrate = foodCarbohydrate;
	}

	public Integer getFoodCategoryId() {
		return this.foodCategoryId;
	}

	public void setFoodCategoryId(Integer foodCategoryId) {
		this.foodCategoryId = foodCategoryId;
	}

	public String getFoodDescription() {
		return this.foodDescription;
	}

	public void setFoodDescription(String foodDescription) {
		this.foodDescription = foodDescription;
	}

	public Float getFoodFat() {
		return this.foodFat;
	}

	public void setFoodFat(Float foodFat) {
		this.foodFat = foodFat;
	}

	public Float getFoodFiber() {
		return this.foodFiber;
	}

	public void setFoodFiber(Float foodFiber) {
		this.foodFiber = foodFiber;
	}

	public String getFoodKeywords() {
		return this.foodKeywords;
	}

	public void setFoodKeywords(String foodKeywords) {
		this.foodKeywords = foodKeywords;
	}

	public String getFoodName() {
		return this.foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public Float getFoodProtein() {
		return this.foodProtein;
	}

	public void setFoodProtein(Float foodProtein) {
		this.foodProtein = foodProtein;
	}

	public Float getFoodSaturatedFat() {
		return this.foodSaturatedFat;
	}

	public void setFoodSaturatedFat(Float foodSaturatedFat) {
		this.foodSaturatedFat = foodSaturatedFat;
	}

	public Float getFoodSodium() {
		return this.foodSodium;
	}

	public void setFoodSodium(Float foodSodium) {
		this.foodSodium = foodSodium;
	}

	public Float getFoodSugar() {
		return this.foodSugar;
	}

	public void setFoodSugar(Float foodSugar) {
		this.foodSugar = foodSugar;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Boolean getRecommend() {
		return this.recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	public String getSourceFoodId() {
		return this.sourceFoodId;
	}

	public void setSourceFoodId(String sourceFoodId) {
		this.sourceFoodId = sourceFoodId;
	}

	public Integer getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

}