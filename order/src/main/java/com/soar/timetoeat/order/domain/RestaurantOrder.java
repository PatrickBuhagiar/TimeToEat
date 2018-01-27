package com.soar.timetoeat.order.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class RestaurantOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long restaurantId;
    private OrderState state;
    private String deliveryAddress;
    private Long expectedDeliveryTime;
    private double totalPrice;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();
    //TODO add user id


    private RestaurantOrder() {
    }

    private RestaurantOrder(final OrderBuilder builder) {
        this.items = builder.items;
        this.restaurantId = builder.restaurantId;
        this.state = builder.state;
        this.expectedDeliveryTime = builder.expectedDeliveryTime;
        this.totalPrice = calculateTotalPrice(this.items);
        this.deliveryAddress = builder.deliveryAddress;
    }

    private double calculateTotalPrice(final Set<OrderItem> items)  {
        return items.stream()
                .map(orderItem -> orderItem.getQuantity() * orderItem.getUnitPrice())
                .mapToDouble(Double::doubleValue)
                .sum();
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

    public void setState(final OrderState state) {
        this.state = state;
    }

    public void setExpectedDeliveryTime(final Long expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantOrder)) return false;
        final RestaurantOrder order = (RestaurantOrder) o;
        return getId() == order.getId() &&
                getRestaurantId() == order.getRestaurantId() &&
                Double.compare(order.getTotalPrice(), getTotalPrice()) == 0 &&
                getState() == order.getState() &&
                Objects.equals(getDeliveryAddress(), order.getDeliveryAddress()) &&
                Objects.equals(getExpectedDeliveryTime(), order.getExpectedDeliveryTime()) &&
                Objects.equals(getItems(), order.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRestaurantId(), getState(), getDeliveryAddress(), getExpectedDeliveryTime(), getTotalPrice(), getItems());
    }

    public static final class OrderBuilder {
        private long restaurantId;
        private OrderState state;
        private String deliveryAddress;
        private Long expectedDeliveryTime;
        private Set<OrderItem> items = new HashSet<>();

        private OrderBuilder() {
        }

        public static OrderBuilder anOrder() {
            return new OrderBuilder();
        }

        public OrderBuilder withRestaurantId(long restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public OrderBuilder withState(OrderState state) {
            this.state = state;
            return this;
        }

        public OrderBuilder withDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public OrderBuilder withExpectedDeliveryTime(Long expectedDeliveryTime) {
            this.expectedDeliveryTime = expectedDeliveryTime;
            return this;
        }

        public OrderBuilder withItems(Set<OrderItem> items) {
            this.items = items;
            return this;
        }

        public RestaurantOrder build() {
            return new RestaurantOrder(this);
        }
    }
}
