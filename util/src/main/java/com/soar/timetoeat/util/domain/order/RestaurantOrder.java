package com.soar.timetoeat.util.domain.order;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RestaurantOrder implements Serializable {
    private long id;
    private String restaurantName;
    private OrderState state;
    private String deliveryAddress;
    private Long expectedDeliveryTime;
    private double totalPrice;
    private Set<OrderItem> items = new HashSet<>();

    public RestaurantOrder() {
    }

    public RestaurantOrder(final long id,
                           final String restaurantName,
                           final OrderState state,
                           final String deliveryAddress,
                           final Long expectedDeliveryTime,
                           final double totalPrice,
                           final Set<OrderItem> items) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.state = state;
        this.deliveryAddress = deliveryAddress;
        this.expectedDeliveryTime = expectedDeliveryTime;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public String getRestaurantName() {
        return restaurantName;
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

    public String getHumanizedExpectedDeliveryTime() {
        if (!Objects.isNull(expectedDeliveryTime)) {
            final Timestamp timestamp = new Timestamp(expectedDeliveryTime);
            Date expectedDate = new Date(timestamp.getTime());
            return expectedDate.toString();
        }
        return "";
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
                getRestaurantName() == that.getRestaurantName() &&
                Double.compare(that.getTotalPrice(), getTotalPrice()) == 0 &&
                getState() == that.getState() &&
                Objects.equals(getDeliveryAddress(), that.getDeliveryAddress()) &&
                Objects.equals(getExpectedDeliveryTime(), that.getExpectedDeliveryTime()) &&
                Objects.equals(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRestaurantName(), getState(), getDeliveryAddress(), getExpectedDeliveryTime(), getTotalPrice(), getItems());
    }

    public String itemsAsString() {
        final StringBuilder stringBuilder = new StringBuilder();
        items.forEach(item -> stringBuilder.append(item.toString()));
        return stringBuilder.toString();
    }
}
