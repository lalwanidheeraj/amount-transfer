/**
 * 
 */
package com.revoult.transfer.dao;

import com.revoult.transfer.api.DepositWIthdrawal;
import com.revoult.transfer.api.MoneyTransfer;
import com.revoult.transfer.exception.CustomException;

/**
 * @author Dheeraj Lalwani
 *
 */
public interface TransactionDao {
	public Boolean transfer(MoneyTransfer transaction) throws CustomException;

	public Boolean deposit(DepositWIthdrawal transaction) throws CustomException;

	public Boolean withdraw(DepositWIthdrawal transaction) throws CustomException;
}
