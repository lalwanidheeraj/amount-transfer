package com.revoult.transfer.rest;

import static com.revoult.transfer.common.ResponseMessages.SUCCESS_AMOUNT_TRANSFER;
import static com.revoult.transfer.common.ResponseMessages.SUCCESS_DEPOSIT;
import static com.revoult.transfer.common.ResponseMessages.SUCCESS_WITHDRAWAL;

import java.math.BigDecimal;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import com.revoult.transfer.api.DepositWIthdrawal;
import com.revoult.transfer.api.MoneyTransfer;
import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.factory.ConnectionManagerFactory;

/**
 * @author Dheeraj Lalwani
 * This is the jersey test class for the TransactionController jersey rest endpoints.
 */
public class TransactionControllerTest extends JerseyTest{
	
	@Override
	protected Application configure() {
		return new ResourceConfig(TransactionController.class);
	}
	@Override
	public void setUp() throws Exception {
		AmountTransferUtil.init("application-test.properties");
		ConnectionManagerFactory.populateJpa();
		MasterDataCache.cacheMasterData();
		super.setUp();
	}
	
	@Test
	public void testTransfer() {
		MoneyTransfer transfer = new MoneyTransfer("CH103255000000000004","CH103211000000000002",new BigDecimal(500));
		String response = target("/money/transfer").request(MediaType.APPLICATION_JSON).post(Entity.json(transfer),String.class);
        Assert.assertTrue(response.startsWith(SUCCESS_AMOUNT_TRANSFER.responseMessage()));
	}
	
	@Test
	public void testDeposit() {
		DepositWIthdrawal transaction = new DepositWIthdrawal(new BigDecimal(500),"EUR","CH103255000000000004");
		String response = target("/money/deposit").request(MediaType.APPLICATION_JSON).post(Entity.json(transaction),String.class);
        Assert.assertTrue(response.startsWith(SUCCESS_DEPOSIT.responseMessage()));
	}
	
	@Test
	public void testWithdraw() {
		DepositWIthdrawal transaction = new DepositWIthdrawal(new BigDecimal(500),"EUR","CH103255000000000004");
		String response = target("/money/withdraw").request(MediaType.APPLICATION_JSON).post(Entity.json(transaction),String.class);
        Assert.assertTrue(response.startsWith(SUCCESS_WITHDRAWAL.responseMessage()));
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
