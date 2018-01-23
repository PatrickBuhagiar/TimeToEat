package com.soar.timetoeat.order.utils;

import com.soar.timetoeat.order.domain.Order;
import com.soar.timetoeat.order.domain.OrderItem;
import com.soar.timetoeat.order.domain.params.CreateOrderItemParams;
import com.soar.timetoeat.order.domain.params.CreateOrderParams;

import java.util.stream.Collectors;

/**
 * A utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateOrderParams} to {@link Order}
     * @param params the create order params
     * @return the converted Order
     */
    public static Order convert(final long restaurantId, final CreateOrderParams params) {
        return Order.OrderBuilder.anOrder()
                .withDeliveryAddress(params.getDeliveryAddress())
                .withRestaurantId(restaurantId)
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
