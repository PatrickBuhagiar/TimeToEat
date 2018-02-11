package com.soar.timetoeat.util.faults.auth;

public class EmailNotUniqueException extends Exception {

    private String email;

    public EmailNotUniqueException(final String message, final String email) {
        this.email = email;
    }

    public String getEmail() {
        return "Email: " + email;
    }
}
