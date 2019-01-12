/**
 * 
 */
package com.revoult.transfer.dao;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.factory.DaoFactory;
import com.revoult.transfer.model.User;

/**
 * @author Dheeraj Lalwani
 * This is the test class for the data access object class UserAccountDao.
 */
public class UserAccountDaoTest {
	
	private UserAccountDao userAccountDao = ((DaoFactory) AppFactoryProvider.create("Dao")).getAccountDao();
	
	@BeforeClass
	public static void setup() throws IOException {
		AmountTransferUtil.init("application-test.properties");
		ConnectionManagerFactory.populateJpa();
		MasterDataCache.cacheMasterData();
	
	}
	@Test
	public void testGetUserAccounts() {
		User user = userAccountDao.getUserAccounts(1);
		Assert.assertEquals("saunders.jon@gmail.com", user.getEmailAddress());
	}
	
	@Test
	public void testGetUsers() throws CustomException {
		Assert.assertFalse(userAccountDao.getUsers().isEmpty());
	}
	
	@Test
	public void testCreateAccount() throws CustomException {
		UserAccount userAccount = new UserAccount("Dheeraj Lalwani",
				"lalwani.dheeraj@gmail.com","Savings","Switzerland","Zurich","EUR");
		Assert.assertNotNull(userAccountDao.createAccount(userAccount));
	}
	
	@Test(expected=CustomException.class)
	public void testCreateUserAccountForExistingAccount() throws CustomException {
		UserAccount userAccount = new UserAccount("jon saunders",
				"saunders.jon@gmail.com","Current","Switzerland","Zurich","EUR");
		userAccountDao.createAccount(userAccount);
	}
	
	@AfterClass
	public static void tearDown() {
		
	}
}
