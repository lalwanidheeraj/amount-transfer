/**
 * 
 */
package com.revoult.transfer.service;

import com.revoult.transfer.api.MoneyTransaction;
import com.revoult.transfer.dao.TransactionDao;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.DaoFactory;

/**
 * @author Dheeraj Lalwani
 * This is the service class deals with the account related transactions.
 */
public class TransactionServiceImpl implements TransactionService{
	
	private TransactionDao transactionDao = ((DaoFactory) AppFactoryProvider.create("Dao")).getTransactionDao();

	/**
	 * This is the service class method to transfer specified money from one account to another.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public void transfer(MoneyTransaction transaction) throws CustomException {
		transactionDao.transfer(transaction);	
	}
	
	/**
	 * This is the rest interface to deposit supplied money to the given account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public void deposit(MoneyTransaction transaction) throws CustomException {
		transactionDao.deposit(transaction);
	}

	/**
	 * This is the rest interface to withdraw supplied money from the given account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public void withdraw(MoneyTransaction transaction) throws CustomException {
		transactionDao.withdraw(transaction);
	}
}
