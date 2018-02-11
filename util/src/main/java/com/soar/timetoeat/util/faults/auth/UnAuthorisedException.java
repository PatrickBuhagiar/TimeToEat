package com.soar.timetoeat.util.faults.auth;

public class UnAuthorisedException extends Exception {
    public UnAuthorisedException() {
        super("An un-authorised call was made");
    }
}
