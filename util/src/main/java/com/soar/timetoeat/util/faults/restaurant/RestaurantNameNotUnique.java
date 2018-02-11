package com.soar.timetoeat.util.faults.restaurant;

public class RestaurantNameNotUnique extends Exception {

    private String restaurantName;

    public RestaurantNameNotUnique(final String restaurantName) {
        super("Restaurant not unique");
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
}
