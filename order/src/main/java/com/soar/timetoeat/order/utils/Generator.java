package com.soar.timetoeat.order.utils;

import com.soar.timetoeat.order.domain.params.CreateOrderItemParams;
import com.soar.timetoeat.order.domain.params.CreateOrderParams;
import com.soar.timetoeat.util.Unique;

import java.util.HashSet;
import java.util.Set;

/**
 * A utility class for generation classes
 */
public class Generator {

    public static CreateOrderParams generateOrderParams(final int n_items) {
        return CreateOrderParams.CreateOrderParamsBuilder.aCreateOrderParams()
                .withDeliveryAddress(Unique.stringValue())
                .withItems(generateOrderItemParams(n_items))
                .build();
    }

    private static Set<CreateOrderItemParams> generateOrderItemParams(final int n_items) {
        final HashSet<CreateOrderItemParams> items = new HashSet<>();
        for (int i = 0; i < n_items; i++) {
             items.add(CreateOrderItemParams.CreateOrderItemParamsBuilder.aCreateOrderItemParams()
                    .withName(Unique.stringValue())
                    .withQuantity(Unique.intValue())
                    .withUnitPrice(Unique.doubleValue())
                    .build());
        }
        return items;
    }
}
