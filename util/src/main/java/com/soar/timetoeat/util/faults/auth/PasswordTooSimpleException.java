package com.soar.timetoeat.util.faults.auth;

public class PasswordTooSimpleException extends Exception {

    public PasswordTooSimpleException(final String message) {
        super(message);
    }
}
