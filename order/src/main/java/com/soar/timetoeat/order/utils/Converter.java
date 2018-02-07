package com.soar.timetoeat.order.utils;

import com.soar.timetoeat.util.domain.order.OrderState;
import com.soar.timetoeat.order.domain.RestaurantOrder;
import com.soar.timetoeat.order.domain.OrderItem;
import com.soar.timetoeat.util.params.order.CreateOrderItemParams;
import com.soar.timetoeat.util.params.order.CreateOrderParams;

import java.util.stream.Collectors;

/**
 * A utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateOrderParams} to {@link RestaurantOrder}
     * @param params the create order params
     * @return the converted RestaurantOrder
     */
    public static RestaurantOrder convert(final long restaurantId, final CreateOrderParams params) {
        return RestaurantOrder.OrderBuilder.anOrder()
                .withDeliveryAddress(params.getDeliveryAddress())
                .withRestaurantId(restaurantId)
                .withState(OrderState.W)
                .withItems(params.getItems()
                        .stream()
                        .map(Converter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Convert from {@link CreateOrderItemParams} to {@link OrderItem}
     * @param params the create order item params
     * @return the converted order item
     */
    private static OrderItem convert(final CreateOrderItemParams params) {
        return OrderItem.OrderItemBuilder.anOrderItem()
                .withName(params.getName())
                .withQuantity(params.getQuantity())
                .withUnitPrice(params.getUnitPrice())
                .build();
    }

}
