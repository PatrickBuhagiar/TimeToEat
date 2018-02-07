package com.soar.timetoeat.util.params.order;

import com.soar.timetoeat.util.domain.order.OrderState;

public class UpdateOrderParams {
    private OrderState state;
    private Long expectedDeliveryTime;

    public UpdateOrderParams() {
    }

    public UpdateOrderParams(final UpdateOrderParamsBuilder builder) {
        this.state = builder.state;
        this.expectedDeliveryTime = builder.expectedDeliveryTime;
    }

    public OrderState getState() {
        return state;
    }

    public Long getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }


    public static final class UpdateOrderParamsBuilder {
        private OrderState state;
        private Long expectedDeliveryTime;

        private UpdateOrderParamsBuilder() {
        }

        public static UpdateOrderParamsBuilder anUpdateOrderParams() {
            return new UpdateOrderParamsBuilder();
        }

        public UpdateOrderParamsBuilder withState(OrderState state) {
            this.state = state;
            return this;
        }

        public UpdateOrderParamsBuilder withExpectedDeliveryTime(Long expectedDeliveryTime) {
            this.expectedDeliveryTime = expectedDeliveryTime;
            return this;
        }

        public UpdateOrderParams build() {
            return new UpdateOrderParams(this);
        }
    }
}
