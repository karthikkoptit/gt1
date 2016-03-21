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
 * The persistent class for the activities database table.
 * 
 */
@Entity
@Table(name="activities")
@NamedQuery(name="Activity.findAll", query="SELECT a FROM Activity a")
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(name="approved_by_id")
	private Integer approvedById;

	@Column(nullable=false, length=255)
	private String category;

	@Column(length=255)
	private String code;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(nullable=false, length=255)
	private String details;

	@Column(length=2147483647)
	private String durations;

	@Column(length=2147483647)
	private String environments;

	@Column(name="exercise_types", length=2147483647)
	private String exerciseTypes;

	@Column(name="group_sizes", length=2147483647)
	private String groupSizes;

	@Column(nullable=false, length=255)
	private String header;

	@Column(name="icon_key", nullable=false, length=255)
	private String iconKey;

	@Column(nullable=false, precision=131089)
	private BigDecimal mets;

	@Column(nullable=false)
	private Boolean recommendable;

	@Column(nullable=false, length=255)
	private String source;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	public Activity() {
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

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getDetails() {
		return this.details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDurations() {
		return this.durations;
	}

	public void setDurations(String durations) {
		this.durations = durations;
	}

	public String getEnvironments() {
		return this.environments;
	}

	public void setEnvironments(String environments) {
		this.environments = environments;
	}

	public String getExerciseTypes() {
		return this.exerciseTypes;
	}

	public void setExerciseTypes(String exerciseTypes) {
		this.exerciseTypes = exerciseTypes;
	}

	public String getGroupSizes() {
		return this.groupSizes;
	}

	public void setGroupSizes(String groupSizes) {
		this.groupSizes = groupSizes;
	}

	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getIconKey() {
		return this.iconKey;
	}

	public void setIconKey(String iconKey) {
		this.iconKey = iconKey;
	}

	public BigDecimal getMets() {
		return this.mets;
	}

	public void setMets(BigDecimal mets) {
		this.mets = mets;
	}

	public Boolean getRecommendable() {
		return this.recommendable;
	}

	public void setRecommendable(Boolean recommendable) {
		this.recommendable = recommendable;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Timestamp getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}