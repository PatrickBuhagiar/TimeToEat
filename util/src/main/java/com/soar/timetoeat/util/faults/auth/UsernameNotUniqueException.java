package com.soar.timetoeat.util.faults.auth;

public class UsernameNotUniqueException extends Exception {

    private String username;

    public UsernameNotUniqueException(final String message, final String username) {
        super(message);
        this.username = username;
    }

    public String getUsername() {
        return "Username: " + username;
    }
}
