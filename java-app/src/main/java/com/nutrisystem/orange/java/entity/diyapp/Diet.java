package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the diet database table.
 * 
 */
@Entity
@Table(name="diet")
@NamedQuery(name="Diet.findAll", query="SELECT d FROM Diet d")
public class Diet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="diet_id", unique=true, nullable=false)
	private Integer dietId;

	@Column(nullable=false, length=2147483647)
	private String diet;

	@Column(name="diet_description", nullable=false, length=2147483647)
	private String dietDescription;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	public Diet() {
	}

	public Integer getDietId() {
		return this.dietId;
	}

	public void setDietId(Integer dietId) {
		this.dietId = dietId;
	}

	public String getDiet() {
		return this.diet;
	}

	public void setDiet(String diet) {
		this.diet = diet;
	}

	public String getDietDescription() {
		return this.dietDescription;
	}

	public void setDietDescription(String dietDescription) {
		this.dietDescription = dietDescription;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}