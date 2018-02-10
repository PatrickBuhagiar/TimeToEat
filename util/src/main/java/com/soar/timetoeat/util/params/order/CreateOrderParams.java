package com.soar.timetoeat.util.params.order;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CreateOrderParams implements Serializable {
    private String deliveryAddress;
    private Set<CreateOrderItemParams> items = new HashSet<>();
    private Long cardNumber;
    private int cvv;


    public CreateOrderParams() {
    }

    public CreateOrderParams(final CreateOrderParamsBuilder builder) {
        this.deliveryAddress = builder.deliveryAddress;
        this.items = builder.items;
        this.cardNumber = builder.cardNumber;
        this.cvv = builder.cvv;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Set<CreateOrderItemParams> getItems() {
        return items;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateOrderParams)) return false;
        final CreateOrderParams that = (CreateOrderParams) o;
        return cvv == that.cvv &&
                Objects.equals(getDeliveryAddress(), that.getDeliveryAddress()) &&
                Objects.equals(getItems(), that.getItems()) &&
                Objects.equals(cardNumber, that.cardNumber);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDeliveryAddress(), getItems(), cardNumber, cvv);
    }

    public static final class CreateOrderParamsBuilder {
        private String deliveryAddress;
        private Set<CreateOrderItemParams> items = new HashSet<>();
        public Long cardNumber;
        public int cvv;

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

        public CreateOrderParamsBuilder withCardNumber(final long cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public CreateOrderParamsBuilder withCvv(final int cvv) {
            this.cvv = cvv;
            return this;
        }

        public CreateOrderParams build() {
            return new CreateOrderParams(this);
        }
    }
}
