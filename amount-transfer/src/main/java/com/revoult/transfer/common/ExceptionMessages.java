/**
 * 
 */
package com.revoult.transfer.common;

/**
 * @author Dheeraj Lalwani
 *
 */
public enum ExceptionMessages {
	
	GENERIC_ERROR("An error occured. Please try again later or contact service desk."),
	ACCOUNT_CREATE_ERROR("Unable to create account. Please contact service desk."),
	USER_CREATE_ERROR("Unable to create or activate user. Please contact service desk."),
	INVALID_TRANSER_ACCOUNTS("Please enter valid source and destination account numbers."),
	MISMATCH_TRANSFER_CURRENCY("Please make sure that currency of source and destination account is same."),
	INSUFFICIENT_BALANCE("Insufficient balance."),
	INVALID_DEPOSIT_ACCOUNT("Please enter valid deposit account number."),
	MISMATCH_DEPOSIT_CURRENCY("Please make sure that currency of given amount account is same."),
	INVALID_WITHDRAWAL_ACCOUNT("Please enter valid withdrawal account number.");
	
	private String exceptionText;
	
	ExceptionMessages(String exceptionText) {
		this.exceptionText=exceptionText;
	}
	
	public String exceptionText() {
		return exceptionText;
	}
	
}
