package com.soar.timetoeat.restaurant.utils;

import com.soar.timetoeat.util.domain.restaurant.RestaurantWithMenu;
import com.soar.timetoeat.util.domain.menu.Menu;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams;
import com.soar.timetoeat.restaurant.domain.Restaurant;

/**
 * A utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateRestaurantParams} to {@link Restaurant}
     * @param params the create restaurant params
     * @param username The owner of the restaurant
     * @return the converted restaurant
     */
    public static Restaurant convert(final CreateRestaurantParams params,
                                     final String username) {
        return Restaurant.RestaurantBuilder.aRestaurant()
                .withName(params.getName())
                .withAddress(params.getAddress())
                .withOwner(username)
                .build();
    }

    /**
     * This converts a restaurant and a menu into a RestaurantWithMenu class
     * @param restaurant the restaurant
     * @param menu the menu
     * @return a restaurant with a menu
     */
    public static RestaurantWithMenu convert(final Restaurant restaurant,
                                               final Menu menu) {
        return new RestaurantWithMenu(restaurant.getName(), restaurant.getAddress(), menu);
    }
}
