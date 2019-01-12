/**
 * 
 */
package com.revoult.transfer.service;

import java.util.List;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.dao.UserAccountDao;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.DaoFactory;
import com.revoult.transfer.model.User;

/**
 * @author Dheeraj Lalwani
 * This is the service class to perform the user and associated account related tasks.
 */
public class UserAccountServiceImpl implements UserAccountService {
	
	private UserAccountDao accountDao = ((DaoFactory) AppFactoryProvider.create("Dao")).getAccountDao();
	
	/** 
	 * This is the service class method, which gets the user and associated accounts for the supplied user.
	 */
	public User getUserAccounts(Integer userId) {
		return accountDao.getUserAccounts(userId);
		
	}

	/** 
	 * This is the service class method, which gets all the users and associated accounts.
	 */
	@Override
	public List<User> getUsers() {
		return accountDao.getUsers();
	}

	/** 
	 * This is service class method to create user account.
	 */
	@Override
	public String createAccount(UserAccount userAccount) throws CustomException {
		return accountDao.createAccount(userAccount);
	}
}
