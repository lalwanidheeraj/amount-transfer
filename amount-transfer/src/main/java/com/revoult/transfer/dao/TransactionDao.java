/**
 * 
 */
package com.revoult.transfer.dao;

import com.revoult.transfer.api.MoneyTransaction;
import com.revoult.transfer.exception.CustomException;

/**
 * @author Dheeraj Lalwani
 *
 */
public interface TransactionDao {
	public void transfer(MoneyTransaction transaction) throws CustomException;

	public void deposit(MoneyTransaction transaction) throws CustomException;

	public void withdraw(MoneyTransaction transaction) throws CustomException;
}
