package com.revoult.transfer.rest;

import static org.junit.Assert.assertEquals;
import static com.revoult.transfer.common.ResponseMessages.*;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.model.User;
import com.revoult.transfer.rest.UserAccountController;

/**
 * @author Dheeraj Lalwani
 * This is the jersey test class for the UserAccountController jersey rest endpoints. 
 */
public class UserAccountControllerTest extends JerseyTest{
	
	@Override
	protected Application configure() {
		return new ResourceConfig(UserAccountController.class);
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
	public void testGetUsers() {
		Response output = target("/user/list").request().get();
        assertEquals("Response code should be 200", 200, output.getStatus());
        List<User> users = output.readEntity(ArrayList.class);
        Assert.assertFalse(users.isEmpty());
	}
	
	@Test
	public void testGetUserAccounts() {
		Response output = target("/user/accounts/1").request().get();
        assertEquals("Response code should be 200", 200, output.getStatus());
        User user = output.readEntity(User.class);
        assertEquals("saunders.jon@gmail.com", user.getEmailAddress());
	}
	
	@Test
	public void testCreateAccount() {
		UserAccount userAccount = new UserAccount("Dheeraj Lalwani",
				"lalwani.dheeraj@gmail.com","Savings","Switzerland","Zurich","EUR");
		String response = target("/user/account/create").request(MediaType.APPLICATION_JSON).post(Entity.json(userAccount),String.class);
        Assert.assertTrue(response.startsWith(SUCCESS_ACCOUNT_CREATE.responseMessage()));
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
