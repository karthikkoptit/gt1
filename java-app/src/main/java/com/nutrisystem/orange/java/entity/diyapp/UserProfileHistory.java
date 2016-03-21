package com.nutrisystem.orange.java.entity.diyapp;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the user_profile_history database table.
 * 
 */
@Entity
@Table(name="user_profile_history")
@NamedQuery(name="UserProfileHistory.findAll", query="SELECT u FROM UserProfileHistory u")
public class UserProfileHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_profile_history_id", unique=true, nullable=false)
	private Long userProfileHistoryId;

	@Column(nullable=false, length=255)
	private String address;

	@Column(nullable=false, length=255)
	private String birthdate;

	@Column(nullable=false)
	private Boolean breastfeeding;

	@Column(nullable=false, length=255)
	private String city;

	@Column(nullable=false, length=255)
	private String country;

	@Column(name="current_activity_level", nullable=false)
	private Integer currentActivityLevel;

	@Column(name="current_lbs", nullable=false)
	private Float currentLbs;

	@Column(name="dietary_preferences", nullable=false, length=2147483647)
	private String dietaryPreferences;

	@Column(name="effective_date", nullable=false, length=2147483647)
	private String effectiveDate;

	@Column(name="first_name", nullable=false, length=255)
	private String firstName;

	@Column(nullable=false, length=255)
	private String gender;

	@Column(name="goal_lbs", nullable=false)
	private Float goalLbs;

	@Column(nullable=false)
	private Float height;

	@Column(name="ineffective_date", nullable=false, length=2147483647)
	private String ineffectiveDate;

	@Column(name="last_name", nullable=false, length=255)
	private String lastName;

	@Column(name="last_update_time", nullable=false)
	private Timestamp lastUpdateTime;

	@Column(name="maintaining_weight", nullable=false)
	private Boolean maintainingWeight;

	@Column(length=255)
	private String nickname;

	@Column(name="phone_number", length=255)
	private String phoneNumber;

	@Column(nullable=false, length=2147483647)
	private String role;

	@Column(name="starting_lbs", nullable=false)
	private Float startingLbs;

	@Column(nullable=false, length=255)
	private String state;

	@Column(nullable=false, length=255)
	private String status;

	@Column(nullable=false, length=255)
	private String timezone;

	@Column(name="user_id", nullable=false)
	private Integer userId;

	@Column(name="weekday_eating_pattern", nullable=false, length=255)
	private String weekdayEatingPattern;

	@Column(name="weekend_eating_pattern", nullable=false, length=255)
	private String weekendEatingPattern;

	@Column(name="zip_code", nullable=false, length=255)
	private String zipCode;

	public UserProfileHistory() {
	}

	public Long getUserProfileHistoryId() {
		return this.userProfileHistoryId;
	}

	public void setUserProfileHistoryId(Long userProfileHistoryId) {
		this.userProfileHistoryId = userProfileHistoryId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public Boolean getBreastfeeding() {
		return this.breastfeeding;
	}

	public void setBreastfeeding(Boolean breastfeeding) {
		this.breastfeeding = breastfeeding;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getCurrentActivityLevel() {
		return this.currentActivityLevel;
	}

	public void setCurrentActivityLevel(Integer currentActivityLevel) {
		this.currentActivityLevel = currentActivityLevel;
	}

	public Float getCurrentLbs() {
		return this.currentLbs;
	}

	public void setCurrentLbs(Float currentLbs) {
		this.currentLbs = currentLbs;
	}

	public String getDietaryPreferences() {
		return this.dietaryPreferences;
	}

	public void setDietaryPreferences(String dietaryPreferences) {
		this.dietaryPreferences = dietaryPreferences;
	}

	public String getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Float getGoalLbs() {
		return this.goalLbs;
	}

	public void setGoalLbs(Float goalLbs) {
		this.goalLbs = goalLbs;
	}

	public Float getHeight() {
		return this.height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}

	public String getIneffectiveDate() {
		return this.ineffectiveDate;
	}

	public void setIneffectiveDate(String ineffectiveDate) {
		this.ineffectiveDate = ineffectiveDate;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Boolean getMaintainingWeight() {
		return this.maintainingWeight;
	}

	public void setMaintainingWeight(Boolean maintainingWeight) {
		this.maintainingWeight = maintainingWeight;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Float getStartingLbs() {
		return this.startingLbs;
	}

	public void setStartingLbs(Float startingLbs) {
		this.startingLbs = startingLbs;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getWeekdayEatingPattern() {
		return this.weekdayEatingPattern;
	}

	public void setWeekdayEatingPattern(String weekdayEatingPattern) {
		this.weekdayEatingPattern = weekdayEatingPattern;
	}

	public String getWeekendEatingPattern() {
		return this.weekendEatingPattern;
	}

	public void setWeekendEatingPattern(String weekendEatingPattern) {
		this.weekendEatingPattern = weekendEatingPattern;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}