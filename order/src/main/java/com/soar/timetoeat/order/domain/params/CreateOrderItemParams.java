package com.soar.timetoeat.order.domain.params;

import java.io.Serializable;
import java.util.Objects;

public class CreateOrderItemParams implements Serializable {

    private String name;
    private int quantity;
    private double unitPrice;

    private CreateOrderItemParams() {
    }

    private CreateOrderItemParams(final CreateOrderItemParamsBuilder builder) {
        this.name = builder.name;
        this.quantity = builder.quantity;
        this.unitPrice = builder.unitPrice;
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
        if (!(o instanceof CreateOrderItemParams)) return false;
        final CreateOrderItemParams that = (CreateOrderItemParams) o;
        return getQuantity() == that.getQuantity() &&
                Double.compare(that.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getQuantity(), getUnitPrice());
    }


    public static final class CreateOrderItemParamsBuilder {
        private String name;
        private int quantity;
        private double unitPrice;

        private CreateOrderItemParamsBuilder() {
        }

        public static CreateOrderItemParamsBuilder aCreateOrderItemParams() {
            return new CreateOrderItemParamsBuilder();
        }

        public CreateOrderItemParamsBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreateOrderItemParamsBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public CreateOrderItemParamsBuilder withUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public CreateOrderItemParams build() {
            return new CreateOrderItemParams(this);
        }
    }
}
