/**
 * 
 */
package com.revoult.transfer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.Account;
import com.revoult.transfer.model.AccountType;
import com.revoult.transfer.model.User;

/**
 * @author Dheeraj Lalwani
 * This is the dao class to communicate with database for the user and associated account related activities.
 */
public class UserAccountDaoImpl implements UserAccountDao {

	private final static String USER_ACCOUNT_QUERY = "SELECT * FROM USER u JOIN ACCOUNT a ON "
			+ "a.UserID = u.UserID JOIN AccountType at ON at.AccountTypeID = a.AccountTypeID "
			+ "and a.IsActive=1 and u.IsActive=1 WHERE u.UserID = ?";
	
	private final static String USERS_QUERY = "SELECT * FROM USER u JOIN ACCOUNT a ON "
			+ "a.UserID = u.UserID JOIN AccountType at ON at.AccountTypeID = a.AccountTypeID "
			+ "and a.IsActive=1 and u.IsActive=1";
	
	/** 
	 * This is the dao class method, which fetches the supplied user and associated accounts from database.
	 */
	public User getUserAccounts(Integer userId) {
		User user = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password);
				PreparedStatement statement = connection.prepareStatement(USER_ACCOUNT_QUERY)) {
			statement.setLong(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					if(user==null) {
						user = new User();
						user.setUserId(resultSet.getLong("UserID"));
						user.setEmailAddress(resultSet.getString("EmailAddress"));
						user.setIsActive(resultSet.getBoolean("IsActive"));
						user.setUserName(resultSet.getString("UserName"));
						Set<Account> accounts = new HashSet<Account>();
						user.setAccounts(accounts);
					}
					Account account = new Account();
					account.setAccountId(resultSet.getLong("AccountID"));
					account.setBalance(resultSet.getBigDecimal("Balance"));
					account.setCurrency(resultSet.getString("Currency"));
					AccountType accountType = new AccountType();
					accountType.setAccountTypeId(resultSet.getInt("AccountTypeID"));
					accountType.setAccountType(resultSet.getString("AccountTypeName"));
					account.setAccountType(accountType);
					user.getAccounts().add(account);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return user;
	}
	
	/** 
	 * This is the dao class method, which fetches all the users and associated accounts from database.
	 */
	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password);
				PreparedStatement statement = connection.prepareStatement(USERS_QUERY)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Long userId = resultSet.getLong("UserID");
					User user = users.stream() 
							  .filter(currentUser -> currentUser.getUserId().equals(userId))
							  .findAny()
							  .orElse(null);
					if(user==null) {
						user = new User();
						user.setUserId(resultSet.getLong("UserID"));
						user.setEmailAddress(resultSet.getString("EmailAddress"));
						user.setIsActive(resultSet.getBoolean("IsActive"));
						user.setUserName(resultSet.getString("UserName"));
						Set<Account> accounts = new HashSet<Account>();
						user.setAccounts(accounts);
						users.add(user);
					}
					Account account = new Account();
					account.setAccountId(resultSet.getLong("AccountID"));
					account.setBalance(resultSet.getBigDecimal("Balance"));
					account.setCurrency(resultSet.getString("Currency"));
					AccountType accountType = new AccountType();
					accountType.setAccountTypeId(resultSet.getInt("AccountTypeID"));
					accountType.setAccountType(resultSet.getString("AccountTypeName"));
					account.setAccountType(accountType);
					user.getAccounts().add(account);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return users;
	}
}
