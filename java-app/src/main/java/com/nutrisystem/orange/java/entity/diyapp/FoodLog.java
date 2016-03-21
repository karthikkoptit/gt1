package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the food_log database table.
 * 
 */
@Entity
@Table(name="food_log")
@NamedQuery(name="FoodLog.findAll", query="SELECT f FROM FoodLog f")
public class FoodLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="food_log_id", unique=true, nullable=false)
	private Long foodLogId;

	@Column(nullable=false)
	private Boolean custom;

	@Column(name="food_id", nullable=false)
	private Integer foodId;

	@Column(name="food_log_date", nullable=false, length=10)
	private String foodLogDate;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(nullable=false)
	private Boolean scanned;

	@Column(name="serving_id", nullable=false)
	private Integer servingId;

	@Column(name="serving_size", nullable=false, precision=10, scale=4)
	private BigDecimal servingSize;

	@Column(name="time_bucket_id", nullable=false)
	private Integer timeBucketId;

	@Column(name="user_id", nullable=false)
	private Integer userId;

	public FoodLog() {
	}

	public Long getFoodLogId() {
		return this.foodLogId;
	}

	public void setFoodLogId(Long foodLogId) {
		this.foodLogId = foodLogId;
	}

	public Boolean getCustom() {
		return this.custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
	}

	public Integer getFoodId() {
		return this.foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public String getFoodLogDate() {
		return this.foodLogDate;
	}

	public void setFoodLogDate(String foodLogDate) {
		this.foodLogDate = foodLogDate;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Boolean getScanned() {
		return this.scanned;
	}

	public void setScanned(Boolean scanned) {
		this.scanned = scanned;
	}

	public Integer getServingId() {
		return this.servingId;
	}

	public void setServingId(Integer servingId) {
		this.servingId = servingId;
	}

	public BigDecimal getServingSize() {
		return this.servingSize;
	}

	public void setServingSize(BigDecimal servingSize) {
		this.servingSize = servingSize;
	}

	public Integer getTimeBucketId() {
		return this.timeBucketId;
	}

	public void setTimeBucketId(Integer timeBucketId) {
		this.timeBucketId = timeBucketId;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}