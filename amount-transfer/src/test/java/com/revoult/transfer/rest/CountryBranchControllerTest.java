package com.revoult.transfer.rest;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.Country;

/**
 * @author Dheeraj Lalwani
 * This is the jersey test class for the CountryBranchController jersey rest endpoints.
 */
public class CountryBranchControllerTest extends JerseyTest{
	
	@Override
	protected Application configure() {
		return new ResourceConfig(CountryBranchController.class);
	}
	@Override
	public void setUp() throws Exception {
		AmountTransferUtil.init("application-test.properties");
		ConnectionManagerFactory.populateJpa();
		MasterDataCache.cacheMasterData();
		super.setUp();
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetCountriesAndBranches() {
		Response output = target("/countriesbranches/list").request().get();
        assertEquals("Response code should be 200", 200, output.getStatus());
        List<Country> countries = output.readEntity(ArrayList.class);
        Assert.assertFalse(countries.isEmpty());
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
