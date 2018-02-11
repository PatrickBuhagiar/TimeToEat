package com.soar.timetoeat.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int quantity;
    private double unitPrice;

    public OrderItem() {
    }

    private OrderItem(final OrderItemBuilder builder) {
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
    }

    public long getId() {
        return id;
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
        return getId() == orderItem.getId() &&
                getQuantity() == orderItem.getQuantity() &&
                Double.compare(orderItem.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getName(), orderItem.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getQuantity(), getUnitPrice());
    }

    @Override
    public String toString() {
        return  name + " x " + quantity + ", ";
    }

    public static final class OrderItemBuilder {
        private String name;
        private int quantity;
        private double unitPrice;

        private OrderItemBuilder() {
        }

        public static OrderItemBuilder anOrderItem() {
            return new OrderItemBuilder();
        }

        public OrderItemBuilder withName(String Name) {
            this.name = Name;
            return this;
        }

        public OrderItemBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder withUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
