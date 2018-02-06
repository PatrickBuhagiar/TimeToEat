package com.soar.timetoeat.restaurant.utils;

import com.soar.timetoeat.util.Unique;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams;

/**
 * A utility class for generating classes
 */
public class Generator {

    /**
     * Generate restaurant parameters
     * @return the generate restaurant parameters
     */
    public static CreateRestaurantParams generateRestaurantParams() {
        return CreateRestaurantParams.CreateRestaurantParamsBuilder.aCreateRestaurantParams()
                .withAddress(Unique.stringValue())
                .withName(Unique.stringValue())
                .build();
    }
}
