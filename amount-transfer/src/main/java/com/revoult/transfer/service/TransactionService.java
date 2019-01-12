/**
 * 
 */
package com.revoult.transfer.service;

import com.revoult.transfer.api.DepositWIthdrawal;
import com.revoult.transfer.api.MoneyTransfer;
import com.revoult.transfer.exception.CustomException;

/**
 * @author Dheeraj Lalwani
 *
 */
public interface TransactionService {
	public void transfer(MoneyTransfer transaction) throws CustomException;

	public void deposit(DepositWIthdrawal transaction) throws CustomException;

	public void withdraw(DepositWIthdrawal transaction) throws CustomException;
}
