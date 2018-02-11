package com.soar.timetoeat.util.exceptions.menu;

public class MenuNotFound extends Exception {

    private long restaurantId;

    public MenuNotFound(final String message, final long restaurantId) {
        super(message);
        this.restaurantId = restaurantId;
    }

    public String getRestaurantId() {
        return "RestaurantId: " + restaurantId;
    }
}
