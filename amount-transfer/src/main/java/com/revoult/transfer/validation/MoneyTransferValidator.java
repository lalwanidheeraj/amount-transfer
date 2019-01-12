/**
 * 
 */
package com.revoult.transfer.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.revoult.transfer.api.MoneyTransfer;

/**
 * @author Dheeraj Lalwani
 * This is validation class, to check if source and destination IBAN are not same.
 */
public class MoneyTransferValidator implements ConstraintValidator<ValidIban, MoneyTransfer>{

	@Override
	public void initialize(ValidIban constraintAnnotation) {
	}

	@Override
	public boolean isValid(MoneyTransfer moneyTranfer, ConstraintValidatorContext context) {
		if(moneyTranfer.getSourceIban()==null || moneyTranfer.getDestIban()==null) {
			return true;
		}
		return !moneyTranfer.getSourceIban().trim().equalsIgnoreCase(moneyTranfer.getDestIban().trim());
	}
	
}
