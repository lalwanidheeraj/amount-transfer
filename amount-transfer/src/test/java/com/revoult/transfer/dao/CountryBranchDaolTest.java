package com.revoult.transfer.dao;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.dao.CountryBranchDao;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.factory.DaoFactory;

/**
 * @author Dheeraj Lalwani
 * This is the test class for the data access object class CountryBranchDao. 
 */
public class CountryBranchDaolTest {
	
	private CountryBranchDao countryBranchDao = ((DaoFactory) AppFactoryProvider.create("Dao")).getCountryBranchDao();
	
	@BeforeClass
	public static void setup() throws IOException {
		AmountTransferUtil.init("application-test.properties");
		ConnectionManagerFactory.populateJpa();
		MasterDataCache.cacheMasterData();
	
	}
	
	@Test
	public void testGetCountriesAndBranches() {
		Assert.assertFalse(countryBranchDao.getCountriesAndBranches().isEmpty());
	}
	
	@AfterClass
	public static void tearDown() {
		
	}
}
