package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the radius database table.
 * 
 */
@Entity
@Table(name="radius")
@NamedQuery(name="Radius.findAll", query="SELECT r FROM Radius r")
public class Radius implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="radius_id", unique=true, nullable=false)
	private Integer radiusId;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(nullable=false, length=2147483647)
	private String radius;

	@Column(name="radius_description", nullable=false, length=2147483647)
	private String radiusDescription;

	@Column(name="radius_max", nullable=false)
	private float radiusMax;

	@Column(name="radius_min", nullable=false)
	private float radiusMin;

	public Radius() {
	}

	public Integer getRadiusId() {
		return this.radiusId;
	}

	public void setRadiusId(Integer radiusId) {
		this.radiusId = radiusId;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getRadius() {
		return this.radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getRadiusDescription() {
		return this.radiusDescription;
	}

	public void setRadiusDescription(String radiusDescription) {
		this.radiusDescription = radiusDescription;
	}

	public float getRadiusMax() {
		return this.radiusMax;
	}

	public void setRadiusMax(float radiusMax) {
		this.radiusMax = radiusMax;
	}

	public float getRadiusMin() {
		return this.radiusMin;
	}

	public void setRadiusMin(float radiusMin) {
		this.radiusMin = radiusMin;
	}

}