/**
 * 
 */
package com.revoult.transfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Dheeraj Lalwani
 * This is model class to map the database result to the AccountType object.
 */
public class AccountType {
	
	@JsonIgnore
	private Integer accountTypeId;
	private String accountType;
	public Integer getAccountTypeId() {
		return accountTypeId;
	}
	public void setAccountTypeId(Integer accountTypeId) {
		this.accountTypeId = accountTypeId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
}
