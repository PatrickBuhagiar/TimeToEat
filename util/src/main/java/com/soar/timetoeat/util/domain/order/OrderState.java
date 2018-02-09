package com.soar.timetoeat.util.domain.order;

public enum OrderState {

    AWAIT_APPROVAL("Awaiting Approval"),
    ACCEPTED("Accepted"),
    DECLINED("Declined"),
    ON_THE_WAY("On the Way"),
    DELIVERED("Delivered");

    private final String description;

    OrderState(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
