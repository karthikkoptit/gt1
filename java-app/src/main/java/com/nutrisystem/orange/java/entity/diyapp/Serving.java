package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the serving database table.
 * 
 */
@Entity
@Table(name="serving")
@NamedQuery(name="Serving.findAll", query="SELECT s FROM Serving s")
public class Serving implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="serving_id", unique=true, nullable=false)
	private Integer servingId;

	@Column(nullable=false)
	private float coefficient;

	@Column(name="food_id", nullable=false)
	private Integer foodId;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(name="serving_default", nullable=false)
	private Boolean servingDefault;

	@Column(name="serving_seq", nullable=false)
	private Integer servingSeq;

	@Column(name="serving_type", nullable=false, length=2147483647)
	private String servingType;

	@Column(name="serving_type_description", nullable=false, length=2147483647)
	private String servingTypeDescription;

	public Serving() {
	}

	public Integer getServingId() {
		return this.servingId;
	}

	public void setServingId(Integer servingId) {
		this.servingId = servingId;
	}

	public float getCoefficient() {
		return this.coefficient;
	}

	public void setCoefficient(float coefficient) {
		this.coefficient = coefficient;
	}

	public Integer getFoodId() {
		return this.foodId;
	}

	public void setFoodId(Integer foodId) {
		this.foodId = foodId;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Boolean getServingDefault() {
		return this.servingDefault;
	}

	public void setServingDefault(Boolean servingDefault) {
		this.servingDefault = servingDefault;
	}

	public Integer getServingSeq() {
		return this.servingSeq;
	}

	public void setServingSeq(Integer servingSeq) {
		this.servingSeq = servingSeq;
	}

	public String getServingType() {
		return this.servingType;
	}

	public void setServingType(String servingType) {
		this.servingType = servingType;
	}

	public String getServingTypeDescription() {
		return this.servingTypeDescription;
	}

	public void setServingTypeDescription(String servingTypeDescription) {
		this.servingTypeDescription = servingTypeDescription;
	}

}