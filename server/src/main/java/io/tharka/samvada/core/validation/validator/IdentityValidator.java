package io.tharka.samvada.core.validation.validator;

import io.tharka.samvada.core.validation.annotation.ValidIdentity;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import java.util.regex.Pattern;

public class IdentityValidator implements ConstraintValidator<ValidIdentity, String> {

    private final Pattern userNamePattern = Pattern.compile("^[a-z][a-z0-9_]*$");

    private final EmailValidator emailValidator = new EmailValidator();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return false;

        if(userNamePattern.matcher(value).matches())
            return true;
        return emailValidator.isValid(value, context);
    }
}
