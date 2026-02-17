package io.tharka.samvada.core.validation.annotation;

import io.tharka.samvada.core.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface ValidPassword {
    String message() default "Password must be 8-32 characters long and include upper, lower, digit, and special characters.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
