package com.revoult.transfer.cache;
import static com.revoult.transfer.common.ColumnLabel.ACCOUNT_TYPE_ID;
import static com.revoult.transfer.common.ColumnLabel.ACCOUNT_TYPE_NAME;
import static com.revoult.transfer.common.ColumnLabel.BRANCH;
import static com.revoult.transfer.common.ColumnLabel.BRANCH_CODE;
import static com.revoult.transfer.common.ColumnLabel.BRANCH_ID;
import static com.revoult.transfer.common.ColumnLabel.COUNTRY_CODE;
import static com.revoult.transfer.common.ColumnLabel.COUNTRY_ID;
import static com.revoult.transfer.common.ColumnLabel.COUNTRY_NAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.AccountType;
import com.revoult.transfer.model.Branch;
import com.revoult.transfer.model.Country;

/**
 * @author Dheearj Lalwani
 * This is the class, which cache the master data required for account creation.
 */
public class MasterDataCache {
	
	private static Logger _logger = Logger.getLogger(MasterDataCache.class.getName());
	
	public static Map<String,Country> countryMap= new HashMap<String,Country>();
	public static Map<String,AccountType> accountTypeMap= new HashMap<String,AccountType>();
	public static Map<String,Branch> branchMap= new HashMap<String,Branch>();
	
	private static final String ACCOUNT_TYPE_QUERY = "SELECT * FROM AccountType";
	private static final String BRANCHES_QUERY = "SELECT * FROM Branch";
	private static final String COUNTRIES_QUERY = "SELECT * FROM Country";
	
	/**
	 * This method cache the master data required for account creation.
	 */
	public static void cacheMasterData() {
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password);
				PreparedStatement accountTypeStatement = connection.prepareStatement(ACCOUNT_TYPE_QUERY);
				PreparedStatement branchesStatement = connection.prepareStatement(BRANCHES_QUERY);
				PreparedStatement countriesStatement = connection.prepareStatement(COUNTRIES_QUERY);) {
			try (ResultSet resultSet = accountTypeStatement.executeQuery()) {
				while (resultSet.next()) {
					AccountType accountType = new AccountType();
					accountType.setAccountType(resultSet.getString(ACCOUNT_TYPE_NAME.label()));
					accountType.setAccountTypeId(resultSet.getInt(ACCOUNT_TYPE_ID.label()));
					accountTypeMap.put(resultSet.getString(ACCOUNT_TYPE_NAME.label()), accountType);
				}
			}
			try (ResultSet resultSet = branchesStatement.executeQuery()) {
				while (resultSet.next()) {
					Branch branch = new Branch();
					branch.setBranch(resultSet.getString(BRANCH.label()));
					branch.setBranchCode(resultSet.getInt(BRANCH_CODE.label()));
					branch.setBranchId(resultSet.getInt(BRANCH_ID.label()));
					branch.setCountryId(resultSet.getInt(COUNTRY_ID.label()));
					branchMap.put(resultSet.getString(BRANCH.label()), branch);
				}
			}
			try (ResultSet resultSet = countriesStatement.executeQuery()) {
				while (resultSet.next()) {
					Country country = new Country();
					country.setCountryCode(resultSet.getString(COUNTRY_CODE.label()));
					country.setCountryId(resultSet.getInt(COUNTRY_ID.label()));
					country.setCountryName(resultSet.getString(COUNTRY_NAME.label()));
					countryMap.put(resultSet.getString(COUNTRY_NAME.label()), country);
				}
			}
		} catch (SQLException e) {
			_logger.severe("An error occured while loading the master data in cache. " + e);
		} 
	}
}
