package com.soar.timetoeat.util.faults.restaurant;

public class RestaurantNotFoundException extends Exception {

    private String restaurantName;

    public RestaurantNotFoundException(final String restaurantName) {
        super("Restaurant not found");
        this.restaurantName = restaurantName;
    }

    public String getRestaurantName() {
        return "Restaurant name: " + restaurantName;
    }
}
