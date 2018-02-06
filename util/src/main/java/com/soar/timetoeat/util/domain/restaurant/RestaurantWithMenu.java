package com.soar.timetoeat.util.domain.restaurant;

import com.soar.timetoeat.util.domain.menu.Menu;

public class RestaurantWithMenu {
    private String name;
    private String address;
    private Menu menu;

    private RestaurantWithMenu() {
    }

    public RestaurantWithMenu(final String name, final String address, final Menu menu) {
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


}
