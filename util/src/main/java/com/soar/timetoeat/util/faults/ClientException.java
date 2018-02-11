package com.soar.timetoeat.util.faults;


/**
 * A generic Exception that will encapsulate an Exception response.
 * the Exception Response class encapsulates all the custom defined faults.
 * By using the client exception in our client application, we can
 * standardise how error handling is done.
 */
public class ClientException extends Exception {
    private ExceptionResponse exceptionResponse;

    public ClientException(final ExceptionResponse exceptionResponse) {
        this.exceptionResponse = exceptionResponse;
    }

    public ExceptionResponse getExceptionResponse() {
        return exceptionResponse;
    }
}
