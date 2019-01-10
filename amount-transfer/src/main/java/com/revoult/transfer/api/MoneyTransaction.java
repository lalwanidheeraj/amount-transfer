/**
 * 
 */
package com.revoult.transfer.api;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

/**
 * @author Dheeraj Lalwani
 * Bean class to perform the account related transactions.
 */
public class MoneyTransaction {
	
	@NotNull
	private Long sourceAccountId;
	@NotNull
	private Long destAccountId;
	@NotNull
	private BigDecimal amount;
	private String currency;
	private Long accountId;
	
	public Long getSourceAccountId() {
		return sourceAccountId;
	}
	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}
	public Long getDestAccountId() {
		return destAccountId;
	}
	public void setDestAccountId(Long destAccountId) {
		this.destAccountId = destAccountId;
	}
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
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	
}
