/**
 * 
 */
package com.revoult.transfer.api;
import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Dheeraj Lalwani
 * Bean class to perform the amount transfer transaction.
 */
public class MoneyTransfer {
	
	public MoneyTransfer() {}
	
	
	public MoneyTransfer(String sourceIban, String destIban, BigDecimal amount) {
		super();
		this.sourceIban = sourceIban;
		this.destIban = destIban;
		this.amount = amount;
	}


	@NotNull(message="Source IBAN cannot be blank.")
	private String sourceIban;
	@NotNull(message="Target IBAN cannot be blank.")
	private String destIban;
	@NotNull(message="Amount cannot be blank.")
	@Min(value=1, message="Amount should be greater than 1.")
	@Digits(integer=50, fraction=2,message="Amount should be not be more than 2 decimal points.")
	private BigDecimal amount;
	
	public String getSourceIban() {
		return sourceIban;
	}
	public void setSourceIban(String sourceIban) {
		this.sourceIban = sourceIban;
	}
	public String getDestIban() {
		return destIban;
	}
	public void setDestIban(String destIban) {
		this.destIban = destIban;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "MoneyTransfer [sourceIban=" + sourceIban + ", destIban=" + destIban + ", amount=" + amount + "]";
	}
}
