/**
 * 
 */
package com.revoult.transfer.dao;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.revoult.transfer.api.DepositWIthdrawal;
import com.revoult.transfer.api.MoneyTransfer;
import com.revoult.transfer.cache.MasterDataCache;
import com.revoult.transfer.common.AmountTransferUtil;
import com.revoult.transfer.exception.CustomException;
import com.revoult.transfer.factory.AppFactoryProvider;
import com.revoult.transfer.factory.ConnectionManagerFactory;
import com.revoult.transfer.factory.DaoFactory;

/**
 * @author Dheeraj Lalwani
 * This is the test class for the data access object class TransactionDao.
 */
public class TransactionDaoTest {
	
	private TransactionDao transactionDao = ((DaoFactory) AppFactoryProvider.create("Dao")).getTransactionDao();
	
	@BeforeClass
	public static void setup() throws IOException {
		AmountTransferUtil.init("application-test.properties");
		ConnectionManagerFactory.populateJpa();
		MasterDataCache.cacheMasterData();
	
	}
	
	@Test(expected=CustomException.class)
	public void testTransferForInsufficientFunds() throws CustomException {
		MoneyTransfer transfer = new MoneyTransfer("CH103255000000000004","CH103211000000000002",new BigDecimal(50000));
		transactionDao.transfer(transfer);
	}
	
	@Test
	public void testTransfer() throws CustomException {
		MoneyTransfer transfer = new MoneyTransfer("CH103255000000000004","CH103211000000000002",new BigDecimal(500));
		Assert.assertTrue(transactionDao.transfer(transfer));
	}
	
	@Test
	public void testDeposit() throws CustomException {
		DepositWIthdrawal transaction = new DepositWIthdrawal(new BigDecimal(500),"EUR","CH103255000000000004");
		Assert.assertTrue(transactionDao.deposit(transaction));
	}
	
	@Test(expected=CustomException.class)
	public void testWithdrawForInsufficientFunds() throws CustomException {
		DepositWIthdrawal transaction = new DepositWIthdrawal(new BigDecimal(500000),"EUR","CH103255000000000004");
		Assert.assertTrue(transactionDao.withdraw(transaction));
	}
	
	@Test
	public void testWithdraw() throws CustomException {
		DepositWIthdrawal transaction = new DepositWIthdrawal(new BigDecimal(500),"EUR","CH103255000000000004");
		Assert.assertTrue(transactionDao.withdraw(transaction));
	}
	
	@AfterClass
	public static void tearDown() {
		
	}
}
