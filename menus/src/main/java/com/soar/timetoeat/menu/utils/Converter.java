package com.soar.timetoeat.menu.utils;

import com.soar.timetoeat.menu.domain.Item;
import com.soar.timetoeat.menu.domain.Menu;
import com.soar.timetoeat.menu.domain.Menu.MenuBuilder;
import com.soar.timetoeat.util.params.menu.CreateItemParams;
import com.soar.timetoeat.util.params.menu.CreateMenuParams;

import java.util.stream.Collectors;

/**
 * A utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateMenuParams} to {@link Menu}
     *
     * @param params       the create menu parameters
     * @param restaurantId the restaurant idValue. Each menu item is linked to one restaurant
     * @return the converted menu
     */
    public static Menu convert(final CreateMenuParams params, final long restaurantId) {
        return MenuBuilder.aMenu()
                .withRestaurantId(restaurantId)
                .withItems(params.getItems()
                        .stream()
                        .map(Converter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Convert from {@link CreateItemParams} to {@link Item}
     *
     * @param params the create Item params
     * @return the converted item
     */
    private static Item convert(final CreateItemParams params) {
        return Item.ItemBuilder.anItem()
                .withName(params.getName())
                .withUnitPrice(params.getUnitPrice())
                .withDescription(params.getDescription())
                .build();
    }

}
