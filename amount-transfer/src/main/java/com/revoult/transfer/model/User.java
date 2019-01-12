/**
 * 
 */
package com.revoult.transfer.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Dheeraj Lalwani
 * This is model class to map the database result to the User object.
 */
public class User {
	
	private Long userId;
	private Set<Account> accounts;
	private String userName;
	private String emailAddress;
	@JsonIgnore
	private Boolean isActive;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Set<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
