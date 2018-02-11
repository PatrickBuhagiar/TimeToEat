package com.soar.timetoeat.util.exceptions;

public class ClientException extends Exception {
    private ExceptionResponse exceptionResponse;

    public ClientException(final ExceptionResponse exceptionResponse) {
        this.exceptionResponse = exceptionResponse;
    }

    public ExceptionResponse getExceptionResponse() {
        return exceptionResponse;
    }
}
