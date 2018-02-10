package com.soar.timetoeat.util.domain.order;

public enum OrderState {

    AWAIT_APPROVAL("awaiting approval"),
    ACCEPTED("accepted"),
    DECLINED("declined"),
    PREPARING("being prepared"),
    ON_THE_WAY("on the way"),
    DELIVERED("delivered");

    private final String description;

    OrderState(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
