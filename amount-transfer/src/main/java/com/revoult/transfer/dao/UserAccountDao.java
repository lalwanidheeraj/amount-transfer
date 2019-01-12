/**
 * 
 */
package com.revoult.transfer.dao;

import java.util.List;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.model.User;

/**
 * @author Dheeraj Lalwani
 *
 */
public interface UserAccountDao {
	User getUserAccounts(Integer userId);

	List<User> getUsers();

	String createAccount(UserAccount userAccount) throws CustomException;
}
