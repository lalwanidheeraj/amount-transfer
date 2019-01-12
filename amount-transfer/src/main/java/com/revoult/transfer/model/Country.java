/**
 * 
 */
package com.revoult.transfer.model;

import java.util.Set;

/**
 * @author Dheeraj Lalwani
 *
 */
public class Country {
	
	private Integer countryId;
	private String countryCode;
	private String countryName;
	private Set<Branch> branches;
	
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Set<Branch> getBranches() {
		return branches;
	}
	public void setBranches(Set<Branch> branches) {
		this.branches = branches;
	}
}
