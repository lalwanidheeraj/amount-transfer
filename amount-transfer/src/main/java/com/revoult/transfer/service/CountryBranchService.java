/**
 * 
 */
package com.revoult.transfer.service;

import java.util.List;

import com.revoult.transfer.model.Country;

/**
 * @author Dheeraj Lalwani
 *
 */
public interface CountryBranchService {
	
	List<Country> getCountriesAndBranches();
}
