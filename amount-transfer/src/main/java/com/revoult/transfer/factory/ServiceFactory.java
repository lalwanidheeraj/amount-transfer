package com.revoult.transfer.factory;

import com.revoult.transfer.service.TransactionService;
import com.revoult.transfer.service.TransactionServiceImpl;
import com.revoult.transfer.service.UserAccountService;
import com.revoult.transfer.service.UserAccountServiceImpl;

/**
 * @author Dheeraj Lalwani
 * This is the factory class, which returns instance of various service classes.
 */
public class ServiceFactory implements AbstractFactory{
	
	public UserAccountService getAccountService() {
		return new UserAccountServiceImpl();
	}
	
	public TransactionService getTransactionService() {
		return new TransactionServiceImpl();
	}
}
