package com.soar.timetoeat.util.faults.auth;

public class UsernameNotDefinedException extends Exception {

    public UsernameNotDefinedException() {
        super("Username not defined");
    }
}
