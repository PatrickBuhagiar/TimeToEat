package com.soar.timetoeat.order.utils;

import com.soar.timetoeat.order.domain.OrderItem;
import com.soar.timetoeat.order.domain.RestaurantOrder;
import com.soar.timetoeat.util.domain.order.OrderState;
import com.soar.timetoeat.util.params.order.CreateOrderItemParams;
import com.soar.timetoeat.util.params.order.CreateOrderParams;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateOrderParams} to {@link RestaurantOrder}
     *
     * @param params         the create order params
     * @param clientUsername the client's username
     * @return the converted RestaurantOrder
     */
    public static RestaurantOrder convert(final String restaurantName,
                                          final CreateOrderParams params,
                                          final String clientUsername) {
        return RestaurantOrder.OrderBuilder.anOrder()
                .withDeliveryAddress(params.getDeliveryAddress())
                .withRestaurantName(restaurantName)
                .withState(OrderState.AWAIT_APPROVAL)
                .withClientUsername(clientUsername)
                .withCardNumber(params.getCardNumber())
                .withCvv(params.getCvv())
                .withItems(params.getItems()
                        .stream()
                        .map(Converter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Convert from {@link CreateOrderItemParams} to {@link OrderItem}
     *
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

    /**
     * Convert between the restaurant domain and the util domain for Restaurant Order.
     * This is needed for messaging.
     *
     * @param order order to be converted
     * @return converted order
     */
    public static com.soar.timetoeat.util.domain.order.RestaurantOrder convertForMessage(final RestaurantOrder order) {
        return new com.soar.timetoeat.util.domain.order.RestaurantOrder(order.getId(),
                order.getRestaurantName(),
                order.getState(),
                order.getDeliveryAddress(),
                order.getExpectedDeliveryTime(),
                order.getTotalPrice(),
                convert(order.getItems()));
    }

    private static Set<com.soar.timetoeat.util.domain.order.OrderItem> convert(final Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new com.soar.timetoeat.util.domain.order.OrderItem(orderItem.getName(), orderItem.getQuantity(), orderItem.getUnitPrice()))
                .collect(Collectors.toSet());
    }

}
