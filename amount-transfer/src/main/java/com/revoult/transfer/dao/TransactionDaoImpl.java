/**
 * 
 */
package com.revoult.transfer.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revoult.transfer.api.MoneyTransaction;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.Account;

/**
 * @author Dheeraj Lalwani
 * This is the dao class to perform the account related transaction to/from database.
 */
public class TransactionDaoImpl implements TransactionDao {
	
	private static final String ACCOUNT_SQL_FOR_UPDATE = "SELECT * FROM Account WHERE AccountID=? AND IsActive=true";
	private final static String UPDATE_ACCOUNT_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountID = ? ";
	
	/**
	 * This is the dao class method to transfer specified money from one account to another.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public void transfer(MoneyTransaction transaction) throws CustomException {
		Account sourceAccount = null;
		Account destAccount = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			try(PreparedStatement statement = connection.prepareStatement(ACCOUNT_SQL_FOR_UPDATE);
					PreparedStatement accUpdateStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
				statement.setLong(1, transaction.getSourceAccountId());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						sourceAccount = new Account();
						sourceAccount.setAccountId(resultSet.getLong("AccountID"));
						sourceAccount.setBalance(resultSet.getBigDecimal("Balance"));
						sourceAccount.setCurrency(resultSet.getString("Currency"));
					}
				}
				statement.setLong(1, transaction.getDestAccountId());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						destAccount = new Account();
						destAccount.setAccountId(resultSet.getLong("AccountID"));
						destAccount.setBalance(resultSet.getBigDecimal("Balance"));
						destAccount.setCurrency(resultSet.getString("Currency"));
					}
				}
				if(sourceAccount==null || destAccount==null || sourceAccount.getCurrency()==null || destAccount.getCurrency()==null) {
					throw new CustomException("Please enter valid source and destination account numbers. ");
				}
				if(!sourceAccount.getCurrency().equalsIgnoreCase(destAccount.getCurrency())) {
					throw new CustomException("Please make sure that currency of source and destination account is same. ");
				}
				BigDecimal finalSourceAccountBalance = sourceAccount.getBalance().subtract(transaction.getAmount());
				if(finalSourceAccountBalance.compareTo(BigDecimal.ZERO) == -1) {
					throw new CustomException("Not enough funds in source account. ");
				}
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
					throw new CustomException("An error occured. Please try again later or contact service desk. ");
				}
				connection.commit();
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				throw new CustomException("An error occured. Please try again later or contact service desk. " , sqle);
			}
		} catch (SQLException sqle1) {
			sqle1.printStackTrace();
			throw new CustomException("An error occured. Please try again later or contact service desk. " , sqle1);
		} 
	}
	
	/**
	 * This is the rest interface to deposit supplied money to the given account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public void deposit(MoneyTransaction transaction) throws CustomException {
		Account account = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			try(PreparedStatement statement = connection.prepareStatement(ACCOUNT_SQL_FOR_UPDATE);
					PreparedStatement accUpdateStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
				statement.setLong(1, transaction.getAccountId());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						account = new Account();
						account.setAccountId(resultSet.getLong("AccountID"));
						account.setBalance(resultSet.getBigDecimal("Balance"));
						account.setCurrency(resultSet.getString("Currency"));
					}
				}
				if(account==null) {
					throw new CustomException("Please enter valid deposit account number. ");
				}
				if(!account.getCurrency().equalsIgnoreCase(transaction.getCurrency())) {
					throw new CustomException("Please make sure that currency of given amount account is same. ");
				}
				BigDecimal finalAccountBalance = account.getBalance().add(transaction.getAmount());
				
				accUpdateStatement.setBigDecimal(1, finalAccountBalance);
				accUpdateStatement.setLong(2, account.getAccountId());
				int row = accUpdateStatement.executeUpdate();
				
				if(row!=1) {
					connection.rollback();
					throw new CustomException("An error occured. Please try again later or contact service desk. ");
				}
				connection.commit();
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				throw new CustomException("An error occured. Please try again later or contact service desk. " , sqle);
			}
		} catch (SQLException sqle1) {
			sqle1.printStackTrace();
			throw new CustomException("An error occured. Please try again later or contact service desk. " , sqle1);
		} 
	}
	
	/**
	 * This is the rest interface to withdraw supplied money from the given account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@Override
	public void withdraw(MoneyTransaction transaction) throws CustomException {
		Account account = null;
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password)) 
		{
			connection.setAutoCommit(false);
			try(PreparedStatement statement = connection.prepareStatement(ACCOUNT_SQL_FOR_UPDATE);
					PreparedStatement accUpdateStatement = connection.prepareStatement(UPDATE_ACCOUNT_BALANCE)) {
				statement.setLong(1, transaction.getAccountId());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						account = new Account();
						account.setAccountId(resultSet.getLong("AccountID"));
						account.setBalance(resultSet.getBigDecimal("Balance"));
						account.setCurrency(resultSet.getString("Currency"));
					}
				}
				if(account==null) {
					throw new CustomException("Please enter valid withdrawl account number. ");
				}
				
				BigDecimal finalAccountBalance = account.getBalance().subtract(transaction.getAmount());
				
				if(finalAccountBalance.compareTo(BigDecimal.ZERO) == -1) {
					throw new CustomException("Not enough funds in source account. ");
				}
				
				accUpdateStatement.setBigDecimal(1, finalAccountBalance);
				accUpdateStatement.setLong(2, account.getAccountId());
				int row = accUpdateStatement.executeUpdate();
				
				if(row!=1) {
					connection.rollback();
					throw new CustomException("An error occured. Please try again later or contact service desk. ");
				}
				connection.commit();
			}catch (SQLException sqle) {
				try {
					if(connection!=null) {
						connection.rollback();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				throw new CustomException("An error occured. Please try again later or contact service desk. " , sqle);
			}
		} catch (SQLException sqle1) {
			sqle1.printStackTrace();
			throw new CustomException("An error occured. Please try again later or contact service desk. " , sqle1);
		} 
	}
}
