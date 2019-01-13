/**
 * 
 */
package com.revoult.transfer.dao;
import static com.revoult.transfer.common.ColumnLabel.ACCOUNT_ID;
import static com.revoult.transfer.common.ColumnLabel.BALANCE;
import static com.revoult.transfer.common.ColumnLabel.CURRENCY;
import static com.revoult.transfer.common.ExceptionMessages.GENERIC_ERROR;
import static com.revoult.transfer.common.ExceptionMessages.INSUFFICIENT_BALANCE;
import static com.revoult.transfer.common.ExceptionMessages.INVALID_DEPOSIT_ACCOUNT;
import static com.revoult.transfer.common.ExceptionMessages.INVALID_TRANSER_ACCOUNTS;
import static com.revoult.transfer.common.ExceptionMessages.INVALID_WITHDRAWAL_ACCOUNT;
import static com.revoult.transfer.common.ExceptionMessages.MISMATCH_DEPOSIT_CURRENCY;
import static com.revoult.transfer.common.ExceptionMessages.MISMATCH_TRANSFER_CURRENCY;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.revoult.transfer.api.DepositWIthdrawal;
import com.revoult.transfer.api.MoneyTransfer;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.Account;

/**
 * @author Dheeraj Lalwani
 * This is the dao class to perform the account related transaction to/from database.
 */
public class TransactionDaoImpl implements TransactionDao {
	
	Logger _logger = Logger.getLogger(TransactionDaoImpl.class.getName()); 
	
	private static final String ACCOUNT_SQL_FOR_UPDATE = "SELECT * FROM Account WHERE IBAN=? AND IsActive=true FOR UPDATE OF Account";
	private static final String UPDATE_ACCOUNT_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountID = ? ";
	
	private static final int ISOLATION_LEVEL = Connection.TRANSACTION_REPEATABLE_READ;
	/**
	 * This is the dao class method to transfer specified money from one account to another.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public synchronized Boolean transfer(MoneyTransfer transaction) throws CustomException {
		Boolean transferSuccessful = false;
		Account sourceAccount = null;
		Account destAccount = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(ISOLATION_LEVEL);
			try(PreparedStatement statement = connection.prepareStatement(ACCOUNT_SQL_FOR_UPDATE);
					PreparedStatement accUpdateStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
				statement.setString(1, transaction.getSourceIban().trim());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						sourceAccount = new Account();
						sourceAccount.setAccountId(resultSet.getLong(ACCOUNT_ID.label()));
						sourceAccount.setBalance(resultSet.getBigDecimal(BALANCE.label()));
						sourceAccount.setCurrency(resultSet.getString(CURRENCY.label()));
					}
				}
				statement.setString(1, transaction.getDestIban().trim());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						destAccount = new Account();
						destAccount.setAccountId(resultSet.getLong(ACCOUNT_ID.label()));
						destAccount.setBalance(resultSet.getBigDecimal(BALANCE.label()));
						destAccount.setCurrency(resultSet.getString(CURRENCY.label()));
					}
				}
				BigDecimal finalSourceAccountBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
				validateAccount(sourceAccount,destAccount,finalSourceAccountBalance);
				
				accUpdateStatement.setBigDecimal(1, finalSourceAccountBalance);
				accUpdateStatement.setLong(2, sourceAccount.getAccountId());
				accUpdateStatement.addBatch();
				BigDecimal finalDestAccountBalance = destAccount.getBalance().add(transaction.getAmount());
				accUpdateStatement.setBigDecimal(1, finalDestAccountBalance);
				accUpdateStatement.setLong(2, destAccount.getAccountId());
				accUpdateStatement.addBatch();
				int[] rows = accUpdateStatement.executeBatch();
				if(rows.length!=2) {
					connection.rollback();
					throw new CustomException(GENERIC_ERROR.exceptionText());
				}
				connection.commit();
				transferSuccessful=true;
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					_logger.warning("An error occured while performing the amount transfer and rolling back the transaction. " + e);
				}
				_logger.warning("An error occured while performing the amount transfer. " + sqle);
				throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle);
			}
		} catch (SQLException sqle1) {
			_logger.warning("An error occured while performing the amount transfer. " + sqle1);
			throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle1);
		} 
		return transferSuccessful;
	}
	
	private void validateAccount(Account sourceAccount, Account destAccount,BigDecimal finalSourceAccountBalance) throws CustomException {
		if(sourceAccount==null || destAccount==null) {
			throw new CustomException(INVALID_TRANSER_ACCOUNTS.exceptionText());
		}
		if(!sourceAccount.getCurrency().equalsIgnoreCase(destAccount.getCurrency())) {
			throw new CustomException(MISMATCH_TRANSFER_CURRENCY.exceptionText());
		}
		if(finalSourceAccountBalance.compareTo(BigDecimal.ZERO) == -1) {
			throw new CustomException(INSUFFICIENT_BALANCE.exceptionText());
		}
	}

	/**
	 * This is the rest interface to deposit supplied money to the given account.
	 * @param transaction
	 * @return 
	 * @return
	 * @throws CustomException
	 */
	@Override
	public Boolean deposit(DepositWIthdrawal transaction) throws CustomException {
		Boolean depositSuccessful = false;
		Account account = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			try(PreparedStatement statement = connection.prepareStatement(ACCOUNT_SQL_FOR_UPDATE);
					PreparedStatement accUpdateStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
				statement.setString(1, transaction.getIban());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						account = new Account();
						account.setAccountId(resultSet.getLong(ACCOUNT_ID.label()));
						account.setBalance(resultSet.getBigDecimal(BALANCE.label()));
						account.setCurrency(resultSet.getString(CURRENCY.label()));
					}
				}
				if(account==null) {
					throw new CustomException(INVALID_DEPOSIT_ACCOUNT.exceptionText());
				}
				if(!account.getCurrency().equalsIgnoreCase(transaction.getCurrency())) {
					throw new CustomException(MISMATCH_DEPOSIT_CURRENCY.exceptionText());
				}
				BigDecimal finalAccountBalance = account.getBalance().add(transaction.getAmount());
				
				accUpdateStatement.setBigDecimal(1, finalAccountBalance);
				accUpdateStatement.setLong(2, account.getAccountId());
				int row = accUpdateStatement.executeUpdate();
				
				if(row!=1) {
					connection.rollback();
					throw new CustomException(GENERIC_ERROR.exceptionText());
				}
				connection.commit();
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					_logger.warning("An error occured while performing the deposit and rolling back transaction. " + e);
				}
				_logger.warning("An error occured while performing the deposit. " + sqle);
				throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle);
			}
			depositSuccessful=true;
		} catch (SQLException sqle1) {
			_logger.warning("An error occured while performing the deposit. " + sqle1);
			throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle1);
		} 
		return depositSuccessful;
	}
	
	/**
	 * This is the rest interface to withdraw supplied money from the given account.
	 * @param transaction
	 * @return 
	 * @return
	 * @throws CustomException
	 */
	@Override
	public Boolean withdraw(DepositWIthdrawal transaction) throws CustomException {
		Boolean withdrawSuccessful = false;
		Account account = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			try(PreparedStatement statement = connection.prepareStatement(ACCOUNT_SQL_FOR_UPDATE);
					PreparedStatement accUpdateStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
				statement.setString(1, transaction.getIban());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						account = new Account();
						account.setAccountId(resultSet.getLong(ACCOUNT_ID.label()));
						account.setBalance(resultSet.getBigDecimal(BALANCE.label()));
						account.setCurrency(resultSet.getString(CURRENCY.label()));
					}
				}
				if(account==null) {
					throw new CustomException(INVALID_WITHDRAWAL_ACCOUNT.exceptionText());
				}
				
				BigDecimal finalAccountBalance = account.getBalance().subtract(transaction.getAmount());
				
				if(finalAccountBalance.compareTo(BigDecimal.ZERO) == -1) {
					throw new CustomException(INSUFFICIENT_BALANCE.exceptionText());
				}
				
				accUpdateStatement.setBigDecimal(1, finalAccountBalance);
				accUpdateStatement.setLong(2, account.getAccountId());
				int row = accUpdateStatement.executeUpdate();
				
				if(row!=1) {
					connection.rollback();
					throw new CustomException(GENERIC_ERROR.exceptionText());
				}
				connection.commit();
				withdrawSuccessful=true;
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					_logger.warning("An error occured while performing the withdrawal and rolling back the transaction. " + e);
				}
				_logger.warning("An error occured while performing the withdrawal. " + sqle);
				throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle);
			}
		} catch (SQLException sqle1) {
			_logger.warning("An error occured while performing the withdrawal. " + sqle1);
			throw new RuntimeException(GENERIC_ERROR.exceptionText() , sqle1);
		} 
		return withdrawSuccessful;
	}
}
