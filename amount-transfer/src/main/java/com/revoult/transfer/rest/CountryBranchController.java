package com.revoult.transfer.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ServiceFactory;
import com.revoult.transfer.model.Country;
import com.revoult.transfer.service.CountryBranchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Dheeraj Lalwani
 * Rest controller to contains country and branch related endpoints. 
 */
@Path("/countriesbranches")
@Api(value="countriesbranches")
public class CountryBranchController {
	
	private CountryBranchService countryBranchService = ((ServiceFactory) AppFactoryProvider.create("Service")).getCountryBranchService();
	
	/**
	 * This is the rest endpoint, which returns the countries and all the branches within that country.
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find all countries and branches", notes = "Returns List of countries and branches in those countries.")
	public List<Country> getCountriesAndBranches() {
		return countryBranchService.getCountriesAndBranches();
	}
}
