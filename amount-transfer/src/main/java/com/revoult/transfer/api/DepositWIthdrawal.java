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
 * Bean class to perform the deposit or withdrawal transaction.
 */
public class DepositWIthdrawal {
	
	public DepositWIthdrawal() {}
	
	public DepositWIthdrawal(BigDecimal amount, String currency, String iban) {
		super();
		this.amount = amount;
		this.currency = currency;
		this.iban = iban;
	}

	@NotNull(message="Amount cannot be blank.")
	@Min(value=1, message="Amount should be greater than 1.")
	@Digits(integer=50, fraction=2,message="Amount should be not be more than 2 decimal points.")
	private BigDecimal amount;
	@NotNull(message="Currency cannot be blank.")
	private String currency;
	@NotNull(message="IBAN cannot be blank.")
	private String iban;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIban() {
		return iban;
	}
	public void setIban(String iban) {
		this.iban = iban;
	}

	@Override
	public String toString() {
		return "DepositWIthdrawal [amount=" + amount + ", currency=" + currency + ", iban=" + iban + "]";
	}
}
