package com.revoult.transfer.rest;

import static com.revoult.transfer.common.ResponseMessages.SUCCESS_AMOUNT_TRANSFER;
import static com.revoult.transfer.common.ResponseMessages.SUCCESS_DEPOSIT;
import static com.revoult.transfer.common.ResponseMessages.SUCCESS_WITHDRAWAL;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.revoult.transfer.api.DepositWIthdrawal;
import com.revoult.transfer.api.MoneyTransfer;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ServiceFactory;
import com.revoult.transfer.service.TransactionService;
import com.revoult.transfer.validation.ValidIban;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Dheeraj Lalwani
 * This is the controller class which contains rest apis to perform the account related transactions.
 */
@Path("/money")
@Api(value = "/money")
public class TransactionController {
	
	private TransactionService transactionService = ((ServiceFactory) AppFactoryProvider.create("Service")).getTransactionService();
	
	/**
	 * This is the rest interface to transfer specified money from one account to another.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/transfer")
	@ApiOperation(value = "Transfer money from source account to target account.", notes = "Returns success message, if transaction is successful.")
	public Response transfer(@Valid @ValidIban MoneyTransfer transaction) throws CustomException  {
		transactionService.transfer(transaction);
		return Response.status(Response.Status.OK).entity(SUCCESS_AMOUNT_TRANSFER.responseMessage()).build();
	}
	/**
	 * This is the rest interface to deposit the given amount to the supplied account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/deposit")
	@ApiOperation(value = "Deposit money to the given account.", notes = "Returns success message, if deposit is successful.")
	public Response deposit(@Valid DepositWIthdrawal transaction) throws CustomException  {
		transactionService.deposit(transaction);
		return Response.status(Response.Status.OK).entity(SUCCESS_DEPOSIT.responseMessage()).build();
	}
	/**
	 * This is the rest interface to withdraw the supplied amount from the given account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/withdraw")
	@ApiOperation(value = "Withdraw money from the given account.", notes = "Returns success message, if withdrawal is successful.")
	public Response withdraw(@Valid DepositWIthdrawal transaction) throws CustomException  {
		transactionService.withdraw(transaction);
		return Response.status(Response.Status.OK).entity(SUCCESS_WITHDRAWAL.responseMessage()).build();
	}
}
