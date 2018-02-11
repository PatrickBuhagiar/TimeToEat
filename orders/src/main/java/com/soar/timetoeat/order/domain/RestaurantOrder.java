package com.soar.timetoeat.order.domain;

import com.soar.timetoeat.util.domain.order.OrderState;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class RestaurantOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String restaurantName;
    private OrderState state;
    private String deliveryAddress;
    private Long expectedDeliveryTime;
    private double totalPrice;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<OrderItem> items = new HashSet<>();
    private String clientUsername;
    private String restaurantUsername;
    private Long cardNumber;
    private int cvv;

    private RestaurantOrder() {
    }

    private RestaurantOrder(final OrderBuilder builder) {
        this.items = builder.items;
        this.restaurantName = builder.restaurantName;
        this.state = builder.state;
        this.expectedDeliveryTime = builder.expectedDeliveryTime;
        this.totalPrice = calculateTotalPrice(this.items);
        this.deliveryAddress = builder.deliveryAddress;
        this.clientUsername = builder.clientUsername;
        this.cardNumber = builder.cardNumber;
        this.cvv = builder.cvv;
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

    public String getClientUsername() {
        return clientUsername;
    }

    public String getRestaurantUsername() {
        return restaurantUsername;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setState(final OrderState state) {
        this.state = state;
    }

    public void setRestaurantUsername(final String restaurantUsername) {
        this.restaurantUsername = restaurantUsername;
    }

    public void setExpectedDeliveryTime(final Long expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }

    public String getItemsAsString() {
        final StringBuilder stringBuilder = new StringBuilder();
        items.forEach(item -> stringBuilder.append(item.toString()));
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RestaurantOrder)) return false;
        final RestaurantOrder that = (RestaurantOrder) o;
        return getId() == that.getId() &&
                Double.compare(that.getTotalPrice(), getTotalPrice()) == 0 &&
                getCvv() == that.getCvv() &&
                Objects.equals(getRestaurantName(), that.getRestaurantName()) &&
                getState() == that.getState() &&
                Objects.equals(getDeliveryAddress(), that.getDeliveryAddress()) &&
                Objects.equals(getExpectedDeliveryTime(), that.getExpectedDeliveryTime()) &&
                Objects.equals(getItems(), that.getItems()) &&
                Objects.equals(getClientUsername(), that.getClientUsername()) &&
                Objects.equals(getRestaurantUsername(), that.getRestaurantUsername()) &&
                Objects.equals(getCardNumber(), that.getCardNumber());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getRestaurantName(), getState(), getDeliveryAddress(), getExpectedDeliveryTime(), getTotalPrice(), getItems(), getClientUsername(), getRestaurantUsername(), getCardNumber(), getCvv());
    }

    public static final class OrderBuilder {
        private String restaurantName;
        private OrderState state;
        private String deliveryAddress;
        private Long expectedDeliveryTime;
        private Set<OrderItem> items = new HashSet<>();
        private String clientUsername;
        public int cvv;
        public Long cardNumber;

        private OrderBuilder() {
        }

        public static OrderBuilder anOrder() {
            return new OrderBuilder();
        }

        public OrderBuilder withRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
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

        public OrderBuilder withClientUsername(String clientUsername) {
            this.clientUsername = clientUsername;
            return this;
        }

        public OrderBuilder withCardNumber(final long cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public OrderBuilder withCvv(final int cvv) {
            this.cvv = cvv;
            return this;
        }

        public RestaurantOrder build() {
            return new RestaurantOrder(this);
        }
    }
}
