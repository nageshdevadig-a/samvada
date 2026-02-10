package io.tharka.samvada.core.validation;

import jakarta.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityValidator.class)
public @interface ValidIdentity {
    String message() default "Invalid username or email format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
