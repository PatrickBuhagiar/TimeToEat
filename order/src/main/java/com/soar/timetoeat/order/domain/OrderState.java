package com.soar.timetoeat.order.domain;

public enum OrderState {

    W("Awaiting Approval"),
    A("Accepted"),
    D("Declined"),
    P("Under Preparation"),
    O("On the Way"),
    V("Delivered");

    private final String description;

    OrderState(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
