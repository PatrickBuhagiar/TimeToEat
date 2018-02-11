package com.soar.timetoeat.util.faults.restaurant;

public class RestaurantNameNotDefinedException extends Exception {

    public RestaurantNameNotDefinedException() {
        super("Restaurant name not defined");
    }
}
