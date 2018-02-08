package com.soar.timetoeat.util.domain.restaurant;

import com.soar.timetoeat.util.domain.menu.Menu;

public class RestaurantWithMenu {
    private Long id;
    private String name;
    private String address;
    private Menu menu;

    private RestaurantWithMenu() {
    }

    public RestaurantWithMenu(final Long id, final String name, final String address, final Menu menu) {
        this.id = id;
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

    public Long getId() {
        return id;
    }
}
