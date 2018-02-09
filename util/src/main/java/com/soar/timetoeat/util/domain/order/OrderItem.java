package com.soar.timetoeat.util.domain.order;

import java.io.Serializable;
import java.util.Objects;

public class OrderItem implements Serializable {
    private String name;
    private int quantity;
    private double unitPrice;

    public OrderItem() {
    }

    public OrderItem(final String name, final int quantity, final double unitPrice) {
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem)) return false;
        final OrderItem orderItem = (OrderItem) o;
        return getQuantity() == orderItem.getQuantity() &&
                Double.compare(orderItem.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getName(), orderItem.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getQuantity(), getUnitPrice());
    }

    @Override
    public String toString() {
        return  name + " x " + quantity + ", ";
    }
}
