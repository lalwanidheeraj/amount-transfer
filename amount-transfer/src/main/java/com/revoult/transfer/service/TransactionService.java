/**
 * 
 */
package com.revoult.transfer.service;

import com.revoult.transfer.api.MoneyTransaction;
import com.revoult.transfer.exception.CustomException;

/**
 * @author Dheeraj Lalwani
 *
 */
public interface TransactionService {
	public void transfer(MoneyTransaction transaction) throws CustomException;

	public void deposit(MoneyTransaction transaction) throws CustomException;

	public void withdraw(MoneyTransaction transaction) throws CustomException;
}
