package io.tharka.samvada.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import java.util.regex.Pattern;

class IdentityValidator implements ConstraintValidator<ValidIdentity, String> {

    private final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z][a-z0-9_]*$");

    private final EmailValidator emailValidator = new EmailValidator();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return false;

        if(USERNAME_PATTERN.matcher(value).matches())
            return true;
        return emailValidator.isValid(value, context);
    }
}
