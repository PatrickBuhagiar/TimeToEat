package com.soar.timetoeat.util.faults.menu;

public class MenuNotFoundException extends Exception {

    private long restaurantId;

    public MenuNotFoundException(final String message, final long restaurantId) {
        super(message);
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return "RestaurantId: " + restaurantId;
    }
}
