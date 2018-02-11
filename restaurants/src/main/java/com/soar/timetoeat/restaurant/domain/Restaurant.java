package com.soar.timetoeat.restaurant.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * The restaurant class
 */
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String address;
    private String owner;

    private Restaurant() {}

    private Restaurant(final RestaurantBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.address = builder.address;
        this.owner = builder.owner;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getOwner() {
        return owner;
    }

    public static final class RestaurantBuilder {
        private long id;
        private String name;
        private String address;
        private String owner;

        private RestaurantBuilder() {
        }

        public static RestaurantBuilder aRestaurant() {
            return new RestaurantBuilder();
        }

        public RestaurantBuilder withId(long id) {
            this.id = id;
            return this;
        }

        public RestaurantBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RestaurantBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public RestaurantBuilder withOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}