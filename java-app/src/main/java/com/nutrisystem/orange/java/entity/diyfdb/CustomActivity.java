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
 * The persistent class for the custom_activities database table.
 * 
 */
@Entity
@Table(name="custom_activities")
@NamedQuery(name="CustomActivity.findAll", query="SELECT c FROM CustomActivity c")
public class CustomActivity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private Integer id;

	@Column(nullable=false)
	private Boolean approved;

	@Column(name="created_at")
	private Timestamp createdAt;

	@Column(nullable=false, length=255)
	private String description;

	@Column(nullable=false, precision=131089)
	private BigDecimal mets;

	@Column(name="updated_at")
	private Timestamp updatedAt;

	@Column(name="user_id")
	private Integer userId;

	public CustomActivity() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getApproved() {
		return this.approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
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

	public BigDecimal getMets() {
		return this.mets;
	}

	public void setMets(BigDecimal mets) {
		this.mets = mets;
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

}