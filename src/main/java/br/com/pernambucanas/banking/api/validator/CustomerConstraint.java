package br.com.pernambucanas.banking.api.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomerValidator.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomerConstraint {

    String message() default "Invalid customer";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
