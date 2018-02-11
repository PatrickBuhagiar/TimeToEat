package com.soar.timetoeat.util.faults.auth;

public class UnAuthenticatedException extends Exception {

    public UnAuthenticatedException() {
        super("an un-authenticated call was made");
    }
}
