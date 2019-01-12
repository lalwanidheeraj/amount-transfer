/**
 * 
 */
package com.revoult.transfer.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Dheeraj Lalwani
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
    ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MoneyTransferValidator.class)
@Documented
public @interface ValidIban {
	  String message () default "Source and Target IBAN cannot be same.";
	  Class<?>[] groups () default {};
	  Class<? extends Payload>[] payload () default {};
}
