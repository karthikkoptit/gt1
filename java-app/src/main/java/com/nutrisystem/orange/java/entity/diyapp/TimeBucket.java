package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the time_bucket database table.
 * 
 */
@Entity
@Table(name="time_bucket")
@NamedQuery(name="TimeBucket.findAll", query="SELECT t FROM TimeBucket t")
public class TimeBucket implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="time_bucket_id", unique=true, nullable=false)
	private Integer timeBucketId;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(name="time_bucket", nullable=false, length=2147483647)
	private String timeBucket;

	@Column(name="time_bucket_end", nullable=false, length=2147483647)
	private String timeBucketEnd;

	@Column(name="time_bucket_start", nullable=false, length=2147483647)
	private String timeBucketStart;

	public TimeBucket() {
	}

	public Integer getTimeBucketId() {
		return this.timeBucketId;
	}

	public void setTimeBucketId(Integer timeBucketId) {
		this.timeBucketId = timeBucketId;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getTimeBucket() {
		return this.timeBucket;
	}

	public void setTimeBucket(String timeBucket) {
		this.timeBucket = timeBucket;
	}

	public String getTimeBucketEnd() {
		return this.timeBucketEnd;
	}

	public void setTimeBucketEnd(String timeBucketEnd) {
		this.timeBucketEnd = timeBucketEnd;
	}

	public String getTimeBucketStart() {
		return this.timeBucketStart;
	}

	public void setTimeBucketStart(String timeBucketStart) {
		this.timeBucketStart = timeBucketStart;
	}

}