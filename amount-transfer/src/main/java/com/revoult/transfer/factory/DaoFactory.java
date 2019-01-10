package com.revoult.transfer.factory;

import com.revoult.transfer.dao.TransactionDao;
import com.revoult.transfer.dao.TransactionDaoImpl;
import com.revoult.transfer.dao.UserAccountDao;
import com.revoult.transfer.dao.UserAccountDaoImpl;

/**
 * @author Dheeraj Lalwani
 * This is factory class, which returns the object of various Data access object classes.
 */
public class DaoFactory implements AbstractFactory{
	
	public UserAccountDao getAccountDao() {
		return new UserAccountDaoImpl();
	}
	public TransactionDao getTransactionDao() {
		return new TransactionDaoImpl();
	}
}
