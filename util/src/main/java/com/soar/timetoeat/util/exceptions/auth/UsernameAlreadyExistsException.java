package com.soar.timetoeat.util.exceptions.auth;

public class UsernameAlreadyExistsException extends Exception {

    private String username;

    public UsernameAlreadyExistsException(final String message, final String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return "Username: " + username;
    }
}
