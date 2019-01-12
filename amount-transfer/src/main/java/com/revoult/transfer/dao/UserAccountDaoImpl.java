package com.revoult.transfer.dao;
import static com.revoult.transfer.common.ColumnLabel.ACCOUNT_ID;
import static com.revoult.transfer.common.ColumnLabel.ACCOUNT_TYPE_ID;
import static com.revoult.transfer.common.ColumnLabel.ACCOUNT_TYPE_NAME;
import static com.revoult.transfer.common.ColumnLabel.BALANCE;
import static com.revoult.transfer.common.ColumnLabel.CURRENCY;
import static com.revoult.transfer.common.ColumnLabel.EMAIL_ADDRESS;
import static com.revoult.transfer.common.ColumnLabel.IBAN;
import static com.revoult.transfer.common.ColumnLabel.ISACTIVE;
import static com.revoult.transfer.common.ColumnLabel.USER_ID;
import static com.revoult.transfer.common.ColumnLabel.USER_NAME;
import static com.revoult.transfer.common.ExceptionMessages.ACCOUNT_CREATE_ERROR;
import static com.revoult.transfer.common.ExceptionMessages.GENERIC_ERROR;
import static com.revoult.transfer.common.ExceptionMessages.USER_CREATE_ERROR;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.iban4j.CountryCode;
import org.iban4j.Iban;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.Account;
import com.revoult.transfer.model.AccountType;
import com.revoult.transfer.model.Branch;
import com.revoult.transfer.model.Country;
import com.revoult.transfer.model.User;

/**
 * @author Dheeraj Lalwani
 * This is the dao class to communicate with database for the user and associated account related activities.
 */
public class UserAccountDaoImpl implements UserAccountDao {
	
	Logger _logger = Logger.getLogger(UserAccountDaoImpl.class.getName());

	private final static String USER_ACCOUNT_QUERY_BY_USERID = "SELECT * FROM USER u JOIN ACCOUNT a ON "
			+ "a.UserID = u.UserID JOIN AccountType at ON at.AccountTypeID = a.AccountTypeID "
			+ "WHERE a.IsActive=1 AND u.IsActive=1 AND u.UserID = ?";
	
	private final static String ACTIVE_USERS_ACCOUNTS_QUERY = "SELECT * FROM USER u JOIN ACCOUNT a ON "
			+ "a.UserID = u.UserID JOIN AccountType at ON at.AccountTypeID = a.AccountTypeID "
			+ "WHERE a.IsActive=1 and u.IsActive=1";
	
	private final static String USERS_ACCOUNTS_QUERY_BY_NAME_EMAIL = "SELECT * FROM USER u JOIN ACCOUNT a ON "
			+ "a.UserID = u.UserID JOIN AccountType at ON at.AccountTypeID = a.AccountTypeID "
			+ " WHERE u.UserName=? AND u.EmailAddress=?";
	
	private final static String CREATE_USER = "INSERT INTO User (UserName, EmailAddress, IsActive) VALUES (?,?,true);";
	
	private final static String ACTIVATE_USER = "UPDATE User SET IsActive=true WHERE UserID = ?";
	
	private final static String CREATE_ACCOUNT = "INSERT INTO Account (AccountTypeID,Balance,Currency,UserID,IsActive,IBAN) VALUES (?,0.00,?,?,true,NULL)";
	
	private final static String UPDATE_IBAN = "UPDATE Account SET IBAN=? WHERE AccountID=?";
	
	private final static String ACTIVE_ACOUNT_MESSAGE = "User has already similar type of active account in the bank.";
	
	private final static String INACTIVE_ACOUNT_MESSAGE = "User has already similar type of inactive account in the bank. Please activate that account";
	
	/** 
	 * This is the dao class method, which fetches the supplied user and associated accounts from database.
	 */
	public User getUserAccounts(Integer userId) {
		User user = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password);
				PreparedStatement statement = connection.prepareStatement(USER_ACCOUNT_QUERY_BY_USERID)) {
			statement.setLong(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					boolean userExists = (user!=null);
					user = mapUserAndAccounts(resultSet,user,userExists);
				}
			}
		} catch (SQLException e) {
			_logger.warning("An error occured while getting the user and associated accounts. " + e);
			throw new RuntimeException(GENERIC_ERROR.exceptionText(),e);
		} 
		return user;
	}
	
	/**
	 * This dao method maps the user and associated accounts.
	 * @param resultSet
	 * @param user
	 * @param userExists 
	 * @param users
	 * @throws SQLException
	 */
	private User mapUserAndAccounts(ResultSet resultSet, User user, boolean userExists) throws SQLException {
		if(!userExists) {
			user = new User();
			Set<Account> accounts = new HashSet<Account>();
			user.setAccounts(accounts);
		}
		user.setUserId(resultSet.getLong(USER_ID.label()));
		user.setEmailAddress(resultSet.getString(EMAIL_ADDRESS.label()));
		user.setIsActive(resultSet.getBoolean(ISACTIVE.label()));
		user.setUserName(resultSet.getString(USER_NAME.label()));
		Account account = new Account();
		account.setAccountId(resultSet.getLong(ACCOUNT_ID.label()));
		account.setBalance(resultSet.getBigDecimal(BALANCE.label()));
		account.setCurrency(resultSet.getString(CURRENCY.label()));
		account.setIban(resultSet.getString(IBAN.label()));
		account.setIsActive(resultSet.getBoolean(ISACTIVE.label()));
		AccountType accountType = new AccountType();
		accountType.setAccountTypeId(resultSet.getInt(ACCOUNT_TYPE_ID.label()));
		accountType.setAccountType(resultSet.getString(ACCOUNT_TYPE_NAME.label()));
		account.setAccountType(accountType);
		user.getAccounts().add(account);
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
				PreparedStatement statement = connection.prepareStatement(ACTIVE_USERS_ACCOUNTS_QUERY)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					users = mapUsersAndAccounts(resultSet,users);
				}
			}
		} catch (SQLException e) {
			_logger.warning("An error occured while getting the users and their accounts. " + e);
			throw new RuntimeException(GENERIC_ERROR.exceptionText(),e);
		} 
		return users;
	}

	/**
	 * This dao method maps multiple users and their associated accounts.
	 * @param resultSet
	 * @param users
	 * @throws SQLException
	 */
	private List<User> mapUsersAndAccounts(ResultSet resultSet,List<User> users) throws SQLException {
		Long userId = resultSet.getLong(USER_ID.label());
		
		User user = users.stream() 
				  .filter(currentUser -> currentUser.getUserId().equals(userId))
				  .findAny()
				  .orElse(null);
		boolean userExists = (user!=null);
		
		user =  mapUserAndAccounts(resultSet, user, userExists);
		if(!userExists) {
			users.add(user);
		}
		return users;
	}

	/** 
	 * This is Dao method to create user account and update the iban.
	 */
	@Override
	public String createAccount(UserAccount userAccount) throws CustomException {
		String ibanNumber = null;
		Integer accountTypeId =  getAccountTypeIdFromCache(userAccount);
		String countryCode = getCountryCodeFromCache(userAccount);
		Integer branchCode = getBranchCodeFromCache(userAccount);
		User user = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			try(PreparedStatement statement = connection.prepareStatement(USERS_ACCOUNTS_QUERY_BY_NAME_EMAIL);
					PreparedStatement createUserStatement = connection.prepareStatement(CREATE_USER,Statement.RETURN_GENERATED_KEYS);
					PreparedStatement userActivateUpdateStatement = connection.prepareStatement(ACTIVATE_USER);
					PreparedStatement createAccountStatement = connection.prepareStatement(CREATE_ACCOUNT,Statement.RETURN_GENERATED_KEYS);
					PreparedStatement updateIbanStatement = connection.prepareStatement(UPDATE_IBAN)) {
				statement.setString(1, userAccount.getName());
				statement.setString(2, userAccount.getEmailAddress());
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						boolean userExists = (user!=null);
						user = mapUserAndAccounts(resultSet,user,userExists);
					}
				}
				validateUser(user,accountTypeId,connection);
				Long userId = activateOrCreateUser(userAccount,user,createUserStatement,userActivateUpdateStatement,connection);
				
				createAccountStatement.setInt(1, accountTypeId);
				createAccountStatement.setString(2, userAccount.getCurrency());
				createAccountStatement.setLong(3, userId);
				int accountRows = createAccountStatement.executeUpdate();
				Long accountId = 0L;
				try (ResultSet generatedKeys = createUserStatement.getGeneratedKeys()) {
		            if (generatedKeys.next()) {
		                accountId = generatedKeys.getLong(1);
		            }
		        }
				ibanNumber = generateIbanNumber(accountId,countryCode,branchCode);
				
				updateIbanStatement.setString(1, ibanNumber);
				updateIbanStatement.setLong(2, accountId);
				int accountUpdateRows = updateIbanStatement.executeUpdate();
				
				if(accountRows == 0 || accountUpdateRows ==0 || accountId==0) {
					connection.rollback();
					throw new CustomException(ACCOUNT_CREATE_ERROR.exceptionText());
				}
				
				connection.commit();
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					_logger.warning("An error occured while creating the user account and rolling back the transaction. " + e);
				}
				_logger.warning("An error occured while creating the user account. " + sqle);
				throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle);
			}
		} catch (SQLException sqle1) {
			_logger.warning("An error occured while creating the user account. " + sqle1);
			throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle1);
		} 
		return ibanNumber;
	}

	/**
	 * This is the method which generates the IBAN number based on accountid,countrycode and branchcode.
	 * @param accountId
	 * @param countryCode
	 * @param String.valueOf(b)branchCode
	 * @return
	 */
	private String generateIbanNumber(Long accountId, String countryCode, Integer branchCode) {
		Iban iban = new Iban.Builder()
                .countryCode(CountryCode.getByCode(countryCode))
                .bankCode(String.valueOf(branchCode))
                .accountNumber(String.format("%011d", accountId))
                .build();
		return iban.toString();
	}

	/**
	 * This is the dao method, which either create new user or activate existing user.
	 * @param userAccount
	 * @param user
	 * @param createUserStatement
	 * @param userActivateUpdateStatement
	 * @param connection
	 * @return
	 * @throws SQLException
	 * @throws CustomException 
	 */
	private Long activateOrCreateUser(UserAccount userAccount, User user, PreparedStatement createUserStatement, PreparedStatement userActivateUpdateStatement, Connection connection) throws SQLException, CustomException {
		Long userId = null;
		Integer rowsUpdated = 0;
		if(user == null) {
			createUserStatement.setString(1, userAccount.getName());
			createUserStatement.setString(2, userAccount.getEmailAddress());
			rowsUpdated = createUserStatement.executeUpdate();
			try (ResultSet generatedKeys = createUserStatement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                userId = generatedKeys.getLong(1);
	            }
	        }
		} else if(!user.getIsActive()) {
			userActivateUpdateStatement.setLong(1, user.getUserId());
			rowsUpdated = userActivateUpdateStatement.executeUpdate();
			userId = user.getUserId(); 
		} else {
			rowsUpdated=1;
			userId = user.getUserId();
		}
		if(rowsUpdated==0 || userId == null) {
			connection.rollback();
			throw new CustomException(USER_CREATE_ERROR.exceptionText());
		}
		return userId;
	}

	/**
	 * This is basic validation method for user account creation.
	 * @param user
	 * @param accountTypeId
	 * @param connection
	 * @throws CustomException
	 * @throws SQLException
	 */
	private void validateUser(User user,Integer accountTypeId, Connection connection) throws CustomException, SQLException {
		if(user!=null && user.getAccounts()!=null) {
			for(Account account : user.getAccounts()) {
				if(account.getAccountType().getAccountTypeId().equals(accountTypeId) ) {
					connection.rollback();
					throw new CustomException(account.getIsActive() ? ACTIVE_ACOUNT_MESSAGE : INACTIVE_ACOUNT_MESSAGE);
				} 
			}
		}
	}

	/**
	 * This method gets the branch code from cache for user account creation.
	 * @param userAccount
	 * @return
	 */
	private Integer getBranchCodeFromCache(UserAccount userAccount) {
		Branch branch = MasterDataCache.branchMap.get(userAccount.getBranch());
		return (branch == null) ?  MasterDataCache.branchMap.get("Zurich").getBranchCode() :  branch.getBranchCode();
	}
	/**
	 * This method gets the country code from cache for user account creation.
	 * @param userAccount
	 * @return
	 */

	private String getCountryCodeFromCache(UserAccount userAccount) {
		Country country = MasterDataCache.countryMap.get(userAccount.getCountry());
		return (country!=null) ? country.getCountryCode() : MasterDataCache.countryMap.get("Switzerland").getCountryCode();
	}
	/**
	 * This method gets the account type id from cache for user account creation.
	 * @param userAccount
	 * @return
	 */

	private Integer getAccountTypeIdFromCache(UserAccount userAccount) {
		AccountType accountType = MasterDataCache.accountTypeMap.get(userAccount.getAccountType());
		return (accountType!=null) ? accountType.getAccountTypeId() : MasterDataCache.accountTypeMap.get("Savings").getAccountTypeId();
	}
}
