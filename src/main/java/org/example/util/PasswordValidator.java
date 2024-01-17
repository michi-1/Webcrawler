package org.example.util;

import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    private final Pattern pattern;

    public PasswordValidator() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }
    public boolean isValidPassword(String password) {
        return pattern.matcher(password).matches();
    }
}
