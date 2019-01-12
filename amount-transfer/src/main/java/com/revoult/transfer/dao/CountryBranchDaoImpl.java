package com.revoult.transfer.dao;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.Branch;
import com.revoult.transfer.model.Country;

/**
 * @author Dheeraj Lalwani
 * This is the dao class to perform operations related to countries and branches within the country.
 */
public class CountryBranchDaoImpl implements CountryBranchDao{
	
	Logger _logger = Logger.getLogger(CountryBranchDaoImpl.class.getName());
	
	private static final String COUNTRIES_BRANCHES_QUERY = "SELECT * FROM Country c JOIN Branch b ON b.CountryID=c.CountryID";
	/**
	 * This is the rest endpoint, which returns the countries and all the branches within that country.
	 */
	@Override
	public List<Country> getCountriesAndBranches() {
		List<Country> countries = new ArrayList<Country>();
		try (Connection connection = DriverManager.getConnection(ConnectionManagerFactory.connectionUrl,
				ConnectionManagerFactory.user, ConnectionManagerFactory.password);
				PreparedStatement statement = connection.prepareStatement(COUNTRIES_BRANCHES_QUERY)) {
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					Integer countryId = resultSet.getInt(COUNTRY_ID.label());
					Country country = countries.stream() 
							  .filter(currentCountry -> currentCountry.getCountryId().equals(countryId))
							  .findAny()
							  .orElse(null);
					if(country==null) {
						country = new Country();
						country.setCountryId(countryId);
						country.setCountryCode(resultSet.getString(COUNTRY_CODE.label()));
						country.setCountryName(resultSet.getString(COUNTRY_NAME.label()));
						Set<Branch> branches = new HashSet<Branch>();
						country.setBranches(branches);
						countries.add(country);
					}
					Branch branch = new Branch();
					branch.setBranch(resultSet.getString(BRANCH.label()));
					branch.setBranchCode(resultSet.getInt(BRANCH_CODE.label()));
					branch.setBranchId(resultSet.getInt(BRANCH_ID.label()));
					country.getBranches().add(branch);
				}
			}
		} catch (SQLException e) {
			_logger.warning("An error occured while getting the country and all branches. " + e);
		} 
		return countries;
	}

}
