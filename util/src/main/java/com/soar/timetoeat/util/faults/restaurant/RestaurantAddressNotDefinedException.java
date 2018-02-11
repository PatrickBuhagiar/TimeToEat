package com.soar.timetoeat.util.faults.restaurant;

public class RestaurantAddressNotDefinedException extends Exception {
    public RestaurantAddressNotDefinedException() {
        super("Restaurant address not defined");
    }
}
