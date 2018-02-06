package com.soar.timetoeat.restaurant.utils;

import com.soar.timetoeat.util.params.CreateRestaurantParams;
import com.soar.timetoeat.restaurant.domain.Restaurant;

/**
 * A utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateRestaurantParams} to {@link Restaurant}
     * @param params the create restaurant params
     * @return the converted restaurant
     */
    public static Restaurant convert(final CreateRestaurantParams params) {
        return Restaurant.RestaurantBuilder.aRestaurant()
                .withName(params.getName())
                .withAddress(params.getAddress())
                .build();
    }
}
