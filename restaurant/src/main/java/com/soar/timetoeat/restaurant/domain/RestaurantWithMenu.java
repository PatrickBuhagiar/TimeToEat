package com.soar.timetoeat.restaurant.domain;

public class RestaurantWithMenu {
    private String name;
    private String address;
    private Menu menu;

    private RestaurantWithMenu() {
    }

    private RestaurantWithMenu(final String name, final String address, final Menu menu) {
        this.name = name;
        this.address = address;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Menu getMenu() {
        return menu;
    }

    public static RestaurantWithMenu buildFrom(final Restaurant restaurant,
                                               final Menu menu) {
        return new RestaurantWithMenu(restaurant.getName(), restaurant.getAddress(), menu);
    }
}
