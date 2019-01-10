/**
 * 
 */
package com.revoult.transfer.rest;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.revoult.transfer.api.MoneyTransaction;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ServiceFactory;
import com.revoult.transfer.service.TransactionService;

/**
 * @author Dheeraj Lalwani
 * This is the controller class which contains rest apis to perform the account related transactions.
 */
@Path("/money")
@Produces(MediaType.APPLICATION_JSON)
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
	public Response transfer(@Valid MoneyTransaction transaction) throws CustomException  {
		if(transaction.getAmount()==null || transaction.getSourceAccountId()==null || transaction.getDestAccountId()==null) {
			throw new CustomException("Please supply all the valid inputs. ");
		}
		if(transaction.getAmount().compareTo(BigDecimal.ZERO) <=0) {
			throw new CustomException("Please supply the valid transfer amount. ");
		}
		transactionService.transfer(transaction);
		return Response.status(Response.Status.OK).build();
	}
	/**
	 * This is the rest interface to deposit the given amount to the supplied account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/deposit")
	public String deposit(MoneyTransaction transaction) throws CustomException  {
		if(transaction.getAmount()==null || transaction.getAccountId()==null || transaction.getCurrency()==null) {
			throw new CustomException("Please supply all the valid inputs. ");
		}
		if(transaction.getAmount().compareTo(BigDecimal.ZERO) <=0) {
			throw new CustomException("Please supply the valid deposit amount. ");
		}
		transactionService.deposit(transaction);
		return "Your deposit request is successful.";
	}
	/**
	 * This is the rest interface to withdraw the supplied amount from the given account.
	 * @param transaction
	 * @return
	 * @throws CustomException
	 */
	@POST
	@Path("/withdraw")
	public String withdraw(MoneyTransaction transaction) throws CustomException  {
		if(transaction.getAmount()==null || transaction.getAccountId()==null) {
			throw new CustomException("Please supply all the valid inputs. ");
		}
		if(transaction.getAmount().compareTo(BigDecimal.ZERO) <=0) {
			throw new CustomException("Please supply the valid withdrawl amount. ");
		}
		transactionService.withdraw(transaction);
		return "Please collect your money.";
	}
}
