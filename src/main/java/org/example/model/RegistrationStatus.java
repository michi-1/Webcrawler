package org.example.model;

public class RegistrationStatus {
    private final boolean success;
    private final String message;

    public RegistrationStatus(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static RegistrationStatus success() {
        return new RegistrationStatus(true, "Registration successful.");
    }

    public static RegistrationStatus emailExists() {
        return new RegistrationStatus(false, "Email already exists.");
    }

    public static RegistrationStatus invalidPassword() {
        return new RegistrationStatus(false, "Invalid password.");
    }
}

