package com.nutrisystem.orange.java.entity.diyfdb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the custom_foods database table.
 * 
 */
@Entity
@Table(name="custom_foods")
@NamedQuery(name="CustomFood.findAll", query="SELECT c FROM CustomFood c")
public class CustomFood implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="approved_by_id")
	private Integer approvedById;

	@Column(precision=6, scale=2)
	private BigDecimal calcium;

	@Column(nullable=false, precision=6, scale=2)
	private BigDecimal calories;

	@Column(precision=6, scale=2)
	private BigDecimal cholesterol;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(length=255)
	private String description;

	@Column(precision=6, scale=2)
	private BigDecimal fiber;

	@Column(name="food_type", length=255)
	private String foodType;

	@Column(precision=6, scale=2)
	private BigDecimal iron;

	@Column(precision=6, scale=2)
	private BigDecimal magnesium;

	@Column(nullable=false, length=255)
	private String name;

	@Column(precision=6, scale=2)
	private BigDecimal potassium;

	@Column(precision=6, scale=2)
	private BigDecimal protein;

	@Column(name="saturated_fat", precision=6, scale=2)
	private BigDecimal saturatedFat;

	@Column(name="serving_size")
	private double servingSize;

	@Column(name="serving_size_unit", length=255)
	private String servingSizeUnit;

	@Column(name="servings_per_container")
	private double servingsPerContainer;

	@Column(precision=6, scale=2)
	private BigDecimal sodium;

	@Column(length=255)
	private String status;

	@Column(precision=6, scale=2)
	private BigDecimal sugar;

	@Column(name="total_carbohydrate", precision=6, scale=2)
	private BigDecimal totalCarbohydrate;

	@Column(name="total_fat", precision=6, scale=2)
	private BigDecimal totalFat;

	@Column(name="trans_fat", precision=6, scale=2)
	private BigDecimal transFat;

	private Long upc;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="user_id")
	private Integer userId;

	@Column(name="vitamin_a", precision=6, scale=2)
	private BigDecimal vitaminA;

	@Column(name="vitamin_b1", precision=6, scale=2)
	private BigDecimal vitaminB1;

	@Column(name="vitamin_b12", precision=6, scale=2)
	private BigDecimal vitaminB12;

	@Column(name="vitamin_b2", precision=6, scale=2)
	private BigDecimal vitaminB2;

	@Column(name="vitamin_b3", precision=6, scale=2)
	private BigDecimal vitaminB3;

	@Column(name="vitamin_b5", precision=6, scale=2)
	private BigDecimal vitaminB5;

	@Column(name="vitamin_b6", precision=6, scale=2)
	private BigDecimal vitaminB6;

	@Column(name="vitamin_b7", precision=6, scale=2)
	private BigDecimal vitaminB7;

	@Column(name="vitamin_b9", precision=6, scale=2)
	private BigDecimal vitaminB9;

	@Column(name="vitamin_c", precision=6, scale=2)
	private BigDecimal vitaminC;

	@Column(name="vitamin_d", precision=6, scale=2)
	private BigDecimal vitaminD;

	@Column(name="vitamin_e", precision=6, scale=2)
	private BigDecimal vitaminE;

	@Column(name="vitamin_k", precision=6, scale=2)
	private BigDecimal vitaminK;

	@Column(precision=6, scale=2)
	private BigDecimal zinc;

	public CustomFood() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedById() {
		return this.approvedById;
	}

	public void setApprovedById(Integer approvedById) {
		this.approvedById = approvedById;
	}

	public BigDecimal getCalcium() {
		return this.calcium;
	}

	public void setCalcium(BigDecimal calcium) {
		this.calcium = calcium;
	}

	public BigDecimal getCalories() {
		return this.calories;
	}

	public void setCalories(BigDecimal calories) {
		this.calories = calories;
	}

	public BigDecimal getCholesterol() {
		return this.cholesterol;
	}

	public void setCholesterol(BigDecimal cholesterol) {
		this.cholesterol = cholesterol;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getFiber() {
		return this.fiber;
	}

	public void setFiber(BigDecimal fiber) {
		this.fiber = fiber;
	}

	public String getFoodType() {
		return this.foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public BigDecimal getIron() {
		return this.iron;
	}

	public void setIron(BigDecimal iron) {
		this.iron = iron;
	}

	public BigDecimal getMagnesium() {
		return this.magnesium;
	}

	public void setMagnesium(BigDecimal magnesium) {
		this.magnesium = magnesium;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPotassium() {
		return this.potassium;
	}

	public void setPotassium(BigDecimal potassium) {
		this.potassium = potassium;
	}

	public BigDecimal getProtein() {
		return this.protein;
	}

	public void setProtein(BigDecimal protein) {
		this.protein = protein;
	}

	public BigDecimal getSaturatedFat() {
		return this.saturatedFat;
	}

	public void setSaturatedFat(BigDecimal saturatedFat) {
		this.saturatedFat = saturatedFat;
	}

	public double getServingSize() {
		return this.servingSize;
	}

	public void setServingSize(double servingSize) {
		this.servingSize = servingSize;
	}

	public String getServingSizeUnit() {
		return this.servingSizeUnit;
	}

	public void setServingSizeUnit(String servingSizeUnit) {
		this.servingSizeUnit = servingSizeUnit;
	}

	public double getServingsPerContainer() {
		return this.servingsPerContainer;
	}

	public void setServingsPerContainer(double servingsPerContainer) {
		this.servingsPerContainer = servingsPerContainer;
	}

	public BigDecimal getSodium() {
		return this.sodium;
	}

	public void setSodium(BigDecimal sodium) {
		this.sodium = sodium;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getSugar() {
		return this.sugar;
	}

	public void setSugar(BigDecimal sugar) {
		this.sugar = sugar;
	}

	public BigDecimal getTotalCarbohydrate() {
		return this.totalCarbohydrate;
	}

	public void setTotalCarbohydrate(BigDecimal totalCarbohydrate) {
		this.totalCarbohydrate = totalCarbohydrate;
	}

	public BigDecimal getTotalFat() {
		return this.totalFat;
	}

	public void setTotalFat(BigDecimal totalFat) {
		this.totalFat = totalFat;
	}

	public BigDecimal getTransFat() {
		return this.transFat;
	}

	public void setTransFat(BigDecimal transFat) {
		this.transFat = transFat;
	}

	public Long getUpc() {
		return this.upc;
	}

	public void setUpc(Long upc) {
		this.upc = upc;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getVitaminA() {
		return this.vitaminA;
	}

	public void setVitaminA(BigDecimal vitaminA) {
		this.vitaminA = vitaminA;
	}

	public BigDecimal getVitaminB1() {
		return this.vitaminB1;
	}

	public void setVitaminB1(BigDecimal vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}

	public BigDecimal getVitaminB12() {
		return this.vitaminB12;
	}

	public void setVitaminB12(BigDecimal vitaminB12) {
		this.vitaminB12 = vitaminB12;
	}

	public BigDecimal getVitaminB2() {
		return this.vitaminB2;
	}

	public void setVitaminB2(BigDecimal vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}

	public BigDecimal getVitaminB3() {
		return this.vitaminB3;
	}

	public void setVitaminB3(BigDecimal vitaminB3) {
		this.vitaminB3 = vitaminB3;
	}

	public BigDecimal getVitaminB5() {
		return this.vitaminB5;
	}

	public void setVitaminB5(BigDecimal vitaminB5) {
		this.vitaminB5 = vitaminB5;
	}

	public BigDecimal getVitaminB6() {
		return this.vitaminB6;
	}

	public void setVitaminB6(BigDecimal vitaminB6) {
		this.vitaminB6 = vitaminB6;
	}

	public BigDecimal getVitaminB7() {
		return this.vitaminB7;
	}

	public void setVitaminB7(BigDecimal vitaminB7) {
		this.vitaminB7 = vitaminB7;
	}

	public BigDecimal getVitaminB9() {
		return this.vitaminB9;
	}

	public void setVitaminB9(BigDecimal vitaminB9) {
		this.vitaminB9 = vitaminB9;
	}

	public BigDecimal getVitaminC() {
		return this.vitaminC;
	}

	public void setVitaminC(BigDecimal vitaminC) {
		this.vitaminC = vitaminC;
	}

	public BigDecimal getVitaminD() {
		return this.vitaminD;
	}

	public void setVitaminD(BigDecimal vitaminD) {
		this.vitaminD = vitaminD;
	}

	public BigDecimal getVitaminE() {
		return this.vitaminE;
	}

	public void setVitaminE(BigDecimal vitaminE) {
		this.vitaminE = vitaminE;
	}

	public BigDecimal getVitaminK() {
		return this.vitaminK;
	}

	public void setVitaminK(BigDecimal vitaminK) {
		this.vitaminK = vitaminK;
	}

	public BigDecimal getZinc() {
		return this.zinc;
	}

	public void setZinc(BigDecimal zinc) {
		this.zinc = zinc;
	}

}