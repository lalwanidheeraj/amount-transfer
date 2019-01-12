/**
 * 
 */
package com.revoult.transfer.dao;

import java.util.List;

import com.revoult.transfer.model.Country;

/**
 * @author HP
 *
 */
public interface CountryBranchDao {
	
	List<Country> getCountriesAndBranches();
}
