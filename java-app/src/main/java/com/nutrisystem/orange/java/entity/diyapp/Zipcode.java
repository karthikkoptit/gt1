package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the zipcode database table.
 * 
 */
@Entity
@Table(name="zipcode")
@NamedQuery(name="Zipcode.findAll", query="SELECT z FROM Zipcode z")
public class Zipcode implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false, length=2147483647)
	private String zipcode;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	private float latitude;

	@Column(nullable=false)
	private float longitude;

	@Column(length=2147483647)
	private String timezone;

	public Zipcode() {
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}