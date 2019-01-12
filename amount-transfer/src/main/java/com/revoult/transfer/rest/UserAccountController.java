package com.revoult.transfer.rest;
import static com.revoult.transfer.common.ResponseMessages.SUCCESS_ACCOUNT_CREATE;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revoult.transfer.api.UserAccount;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ServiceFactory;
import com.revoult.transfer.model.User;
import com.revoult.transfer.service.UserAccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Dheeraj Lalwani
 * This is the controller class to perform the user and associated account related activities.
 */
@Path("/user")
@Api(value = "/user")
public class UserAccountController {
	
	private UserAccountService userAccountService = ((ServiceFactory) AppFactoryProvider.create("Service")).getAccountService();
	
	 /** 
	  * This is the rest endpoint, which gets the user and associated accounts for the supplied user.
	 * @param userId
	 * @return
	 */
	@GET
	@Path("/accounts/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Find user and associated accounts by userid", notes = "Returns User and associated accounts.", response = User.class)
	public User getUserAccounts(@PathParam("userid") Integer userId) {
		return userAccountService.getUserAccounts(userId);
	}
	
	/**
	 * This is the rest endpoint, which gets all the users and associated accounts.
	 * @return
	 */
	@GET
	@Path("/list")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find all users and their accounts", notes = "Returns List of users and their accounts.")
	public List<User> getUsers() {
		return userAccountService.getUsers();
	}
	/**
	 * This is rest endpoint to create user account.
	 * @param userAccount
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/account/create")
	@ApiOperation(value = "Create the new user account", notes = "Returns the success response along with IBAN, if account created successfully.")
	public Response createAccount(@Valid UserAccount userAccount) throws CustomException  {
		String iban = userAccountService.createAccount(userAccount);
		return Response.status(Response.Status.OK).entity(SUCCESS_ACCOUNT_CREATE.responseMessage()+iban).type(MediaType.TEXT_PLAIN).build();
		
	}
}
