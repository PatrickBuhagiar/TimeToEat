package com.soar.timetoeat.util.params.order;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CreateOrderParams implements Serializable {
    private String deliveryAddress;
    private Set<CreateOrderItemParams> items = new HashSet<>();

    public CreateOrderParams() {
    }

    public CreateOrderParams(final CreateOrderParamsBuilder builder) {
        this.deliveryAddress = builder.deliveryAddress;
        this.items = builder.items;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Set<CreateOrderItemParams> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateOrderParams)) return false;
        final CreateOrderParams that = (CreateOrderParams) o;
        return Objects.equals(getDeliveryAddress(), that.getDeliveryAddress()) &&
                Objects.equals(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeliveryAddress(), getItems());
    }


    public static final class CreateOrderParamsBuilder {
        private String deliveryAddress;
        private Set<CreateOrderItemParams> items = new HashSet<>();

        private CreateOrderParamsBuilder() {
        }

        public static CreateOrderParamsBuilder aCreateOrderParams() {
            return new CreateOrderParamsBuilder();
        }

        public CreateOrderParamsBuilder withDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public CreateOrderParamsBuilder withItems(Set<CreateOrderItemParams> items) {
            this.items = items;
            return this;
        }

        public CreateOrderParams build() {
            return new CreateOrderParams(this);
        }
    }
}
