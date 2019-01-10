/**
 * 
 */
package com.revoult.transfer.exception;

/**
 * @author Dheeraj Lalwani
 * This is custom exception for the amount-transfer application.
 */
public class CustomException extends Exception{
	
	private static final long serialVersionUID = 6435279725791915842L;

	public CustomException(String errorText) {
		super(errorText);
	}
	
	public CustomException(String errorText,Throwable error) {
		super(errorText,error);
	}
}
