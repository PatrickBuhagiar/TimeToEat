package com.soar.timetoeat.util.faults;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class ExceptionResponse implements Serializable {

    private String description;
    private Object fault;

    public ExceptionResponse(final String description, final Object fault) {
        this.description = description;
        this.fault = fault;
    }

    public String getDescription() {
        return description;
    }

    public Object getFault() {
        return fault;
    }
}
