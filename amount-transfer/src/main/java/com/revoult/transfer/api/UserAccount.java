/**
 * 
 */
package com.revoult.transfer.api;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

/**
 * @author Dheeraj Lalwani
 *
 */
public class UserAccount {
	
	public UserAccount() {}
	
	public UserAccount(String name, String emailAddress, String accountType, String country, String branch,
			String currency) {
		super();
		this.name = name;
		this.emailAddress = emailAddress;
		this.accountType = accountType;
		this.country = country;
		this.branch = branch;
		this.currency = currency;
	}
	@NotNull(message="User name cannot be blank.")
	private String name;
	@NotNull(message="Email address cannot be blank.")
	@Email(message="Please supply email address in proper format. ")
	private String emailAddress;
	@NotNull(message="Account Type cannot be blank.")
	private String accountType;
	@NotNull(message="Country cannot be blank.")
	private String country;
	@NotNull(message="Branch cannot be blank.")
	private String branch;
	private String currency="EUR";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	@Override
	public String toString() {
		return "UserAccount [name=" + name + ", emailAddress=" + emailAddress + ", accountType=" + accountType
				+ ", country=" + country + ", branch=" + branch + ", currency=" + currency + "]";
	}
}
