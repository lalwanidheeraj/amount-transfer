/**
 * 
 */
package com.revoult.transfer.common;

/**
 * @author Dheeraj Lalwani
 *
 */
public enum ResponseMessages {
	
	SUCCESS_ACCOUNT_CREATE("Congratulations your account has been setup. Your IBAN number is "),
	SUCCESS_AMOUNT_TRANSFER("Amount transafer is successfully completed."),
	SUCCESS_DEPOSIT("Amount has been credited to beneficiary account."),
	SUCCESS_WITHDRAWAL("Thanks for using the bank. Please collect your amount.");
	
	private String responseMessage;
	
	ResponseMessages(String responseMessage) {
		this.responseMessage=responseMessage;
	}
	
	public String responseMessage() {
		return responseMessage;
	}
	
}
