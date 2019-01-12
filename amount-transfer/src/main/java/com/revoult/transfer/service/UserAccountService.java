/**
 * 
 */
package com.revoult.transfer.service;

import java.util.List;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.model.User;

/**
 * @author Dheeraj Lalwani
 * This is the interface for the UserAccount class, to perform user and related account activities.
 */
public interface UserAccountService {
	
	User getUserAccounts(Integer userId);

	List<User> getUsers();

	String createAccount(UserAccount userAccount) throws CustomException;
}
