package com.revoult.transfer.common;

/**
 * @author Dheeraj Lalwani
 *
 */
public enum ColumnLabel {
	
	ACCOUNT_ID("AccountID"),
	BALANCE("Balance"),
	CURRENCY("Currency"),
	COUNTRY_ID("CountryID"),
	COUNTRY_CODE("CountryCode"),
	COUNTRY_NAME("CountryName"),
	BRANCH("Branch"),
	BRANCH_CODE("BranchCode"),
	BRANCH_ID("BranchID"),
	IBAN("IBAN"),
	ISACTIVE("IsActive"),
	USER_ID("UserID"),
	EMAIL_ADDRESS("EmailAddress"),
	USER_NAME("UserName"),
	ACCOUNT_TYPE_ID("AccountTypeID"),
	ACCOUNT_TYPE_NAME("AccountTypeName");
	
	private String label;
	
	ColumnLabel(String label) {
		this.label=label;
	}
	
	public String label() {
		return label;
	}
	
}
