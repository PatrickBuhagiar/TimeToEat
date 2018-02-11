package com.soar.timetoeat.util.faults.order;

public class OrderDoesNotExistException extends Exception {
    private long orderId;

    public OrderDoesNotExistException(final long orderId) {
        super("Order does not exist");
        this.orderId = orderId;
    }

    public String getOrderId() {
        return "Order ID: " + orderId;
    }
}
