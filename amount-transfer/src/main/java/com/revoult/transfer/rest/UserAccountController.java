/**
 * 
 */
package com.revoult.transfer.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ServiceFactory;
import com.revoult.transfer.model.User;
import com.revoult.transfer.service.UserAccountService;

/**
 * @author Dheeraj Lalwani
 * This is the controller class to perform the user and associated account related activities.
 */
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserAccountController {
	
	private UserAccountService userAccountService = ((ServiceFactory) AppFactoryProvider.create("Service")).getAccountService();
	
	/**
	 * This is the rest endpoint, which gets the user and associated accounts for the supplied user.
	 * @param userId
	 * @return
	 */
	@GET
	@Path("/accounts/{userid}")
	public User getUserAccounts(@PathParam("userid") Integer userId) {
		return userAccountService.getUserAccounts(userId);
	}
	
	/**
	 * This is the rest endpoint, which gets all the users and associated accounts.
	 * @return
	 */
	@GET
	@Path("/list")
	public List<User> getUsers() {
		return userAccountService.getUsers();
	}
}
