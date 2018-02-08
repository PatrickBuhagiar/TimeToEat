package com.soar.timetoeat.util.domain.order;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RestaurantOrder implements Serializable {
    private long id;
    private long restaurantId;
    private OrderState state;
    private String deliveryAddress;
    private Long expectedDeliveryTime;
    private double totalPrice;
    private Set<OrderItem> items = new HashSet<>();

    public RestaurantOrder() {
    }

    public RestaurantOrder(final long id,
                           final long restaurantId,
                           final OrderState state,
                           final String deliveryAddress,
                           final Long expectedDeliveryTime,
                           final double totalPrice,
                           final Set<OrderItem> items) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.state = state;
        this.deliveryAddress = deliveryAddress;
        this.expectedDeliveryTime = expectedDeliveryTime;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public OrderState getState() {
        return state;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public Long getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantOrder)) return false;
        final RestaurantOrder that = (RestaurantOrder) o;
        return getId() == that.getId() &&
                getRestaurantId() == that.getRestaurantId() &&
                Double.compare(that.getTotalPrice(), getTotalPrice()) == 0 &&
                getState() == that.getState() &&
                Objects.equals(getDeliveryAddress(), that.getDeliveryAddress()) &&
                Objects.equals(getExpectedDeliveryTime(), that.getExpectedDeliveryTime()) &&
                Objects.equals(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRestaurantId(), getState(), getDeliveryAddress(), getExpectedDeliveryTime(), getTotalPrice(), getItems());
    }
}
