package io.tharka.samvada.core.validation.validator;

import io.tharka.samvada.core.validation.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}|;:',.<>?]).{8,32}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if(password == null) {
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }
}
