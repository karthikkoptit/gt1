package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the activity_log database table.
 * 
 */
@Entity
@Table(name="activity_log")
@NamedQuery(name="ActivityLog.findAll", query="SELECT a FROM ActivityLog a")
public class ActivityLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="activity_log_id", unique=true, nullable=false)
	private Long activityLogId;

	@Column(name="activity_id", nullable=false)
	private Integer activityId;

	@Column(name="activity_log_date", nullable=false, length=10)
	private String activityLogDate;

	private Float calories;

	@Column(nullable=false)
	private Boolean custom;

	@Column(nullable=false)
	private Boolean device;

	private Integer duration;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(name="log_time_bucket_id", nullable=false)
	private Integer logTimeBucketId;

	@Column(name="time_bucket_id", nullable=false)
	private Integer timeBucketId;

	@Column(name="user_id", nullable=false)
	private Integer userId;

	public ActivityLog() {
	}

	public Long getActivityLogId() {
		return this.activityLogId;
	}

	public void setActivityLogId(Long activityLogId) {
		this.activityLogId = activityLogId;
	}

	public Integer getActivityId() {
		return this.activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getActivityLogDate() {
		return this.activityLogDate;
	}

	public void setActivityLogDate(String activityLogDate) {
		this.activityLogDate = activityLogDate;
	}

	public Float getCalories() {
		return this.calories;
	}

	public void setCalories(Float calories) {
		this.calories = calories;
	}

	public Boolean getCustom() {
		return this.custom;
	}

	public void setCustom(Boolean custom) {
		this.custom = custom;
	}

	public Boolean getDevice() {
		return this.device;
	}

	public void setDevice(Boolean device) {
		this.device = device;
	}

	public Integer getDuration() {
		return this.duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getLogTimeBucketId() {
		return this.logTimeBucketId;
	}

	public void setLogTimeBucketId(Integer logTimeBucketId) {
		this.logTimeBucketId = logTimeBucketId;
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