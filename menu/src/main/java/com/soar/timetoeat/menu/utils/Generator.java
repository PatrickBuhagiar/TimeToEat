package com.soar.timetoeat.menu.utils;

import com.soar.timetoeat.menu.domain.params.CreateItemParams;
import com.soar.timetoeat.menu.domain.params.CreateMenuParams;
import com.soar.timetoeat.menu.domain.params.CreateSectionParams;
import com.soar.timetoeat.util.Unique;

import java.util.HashSet;
import java.util.Set;

/**
 * A utility class for generating classes
 */
public class Generator {

    /**
     * Generate menu parameters
     *
     * @param n_items number of menu items
     * @param n_sections number of sections
     * @return generated parameters
     */
    public static CreateMenuParams generateMenuParams(final int n_items, final int n_sections) {
        return CreateMenuParams.CreateMenuParamsBuilder.aCreateMenuParams()
                .withSections(generateSectionParams(n_items, n_sections))
                .build();
    }

    /**
     * Generate section params
     * @param n_items number of menu items
     * @param n_sections number of sections
     * @return generated sections
     */
    private static Set<CreateSectionParams> generateSectionParams(final int n_items, final int n_sections) {
        final HashSet<CreateSectionParams> sections = new HashSet<>();
        for (int i = 0; i < n_sections; i++) {
            sections.add(CreateSectionParams.CreateSectionParamsBuilder.aCreateSectionParams()
                    .withName(Unique.stringValue())
                    .withItems(generateItemParams(n_items))
                    .build());
        }
        return sections;
    }

    /**
     * Generate menu items
     * @param n_items number of menu items
     * @return generated menu items
     */
    private static Set<CreateItemParams> generateItemParams(final int n_items) {
        final HashSet<CreateItemParams> items = new HashSet<>();
        for (int i = 0; i < n_items; i++) {
            items.add(CreateItemParams.CreateItemParamsBuilder.aCreateItemParams()
                    .withDescription(Unique.stringValue())
                    .withUnitPrice(Unique.doubleValue())
                    .withName(Unique.stringValue())
                    .build());
        }
        return items;
    }
}
