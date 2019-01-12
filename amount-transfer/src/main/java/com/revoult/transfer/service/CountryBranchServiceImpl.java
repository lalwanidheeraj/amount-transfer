/**
 * 
 */
package com.revoult.transfer.service;

import java.util.List;

import com.revoult.transfer.dao.CountryBranchDao;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.DaoFactory;
import com.revoult.transfer.model.Country;

/**
 * @author Dheeraj Lalwani
 * This is the service class to perform the operation related to country and branches within the country.
 */
public class CountryBranchServiceImpl implements CountryBranchService{
	
	private CountryBranchDao countryBranchDao = ((DaoFactory) AppFactoryProvider.create("Dao")).getCountryBranchDao();
	
	/**
	 * This is the service method, which returns the countries and all the branches within that country.
	 */
	@Override
	public List<Country> getCountriesAndBranches() {
		return countryBranchDao.getCountriesAndBranches();
	}

}
