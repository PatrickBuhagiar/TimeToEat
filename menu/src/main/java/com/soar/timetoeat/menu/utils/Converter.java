package com.soar.timetoeat.menu.utils;

import com.soar.timetoeat.menu.domain.Item;
import com.soar.timetoeat.menu.domain.Menu;
import com.soar.timetoeat.menu.domain.Menu.MenuBuilder;
import com.soar.timetoeat.menu.domain.Section;
import com.soar.timetoeat.menu.domain.Section.SectionBuilder;
import com.soar.timetoeat.menu.domain.params.CreateItemParams;
import com.soar.timetoeat.menu.domain.params.CreateMenuParams;
import com.soar.timetoeat.menu.domain.params.CreateSectionParams;

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
     * @return the converted menu item
     */
    public static Menu convert(final CreateMenuParams params, final long restaurantId) {
        return MenuBuilder.aMenu()
                .withRestaurantId(restaurantId)
                .withSections(params.getSections()
                        .stream()
                        .map(Converter::convert)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Convert from {@link CreateSectionParams} to {@link Section}
     *
     * @param params the create Section params
     * @return the converted section
     */
    private static Section convert(final CreateSectionParams params) {
        return SectionBuilder.aSection()
                .withName(params.getName())
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
