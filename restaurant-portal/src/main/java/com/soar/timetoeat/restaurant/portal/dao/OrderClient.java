package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.util.domain.order.RestaurantOrder;
import com.soar.timetoeat.util.params.order.UpdateOrderParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("ORDER-SERVICE")
public interface OrderClient {

    @RequestMapping(value = "orders/restaurant", method = GET)
    @ResponseBody
    Set<RestaurantOrder> getRestaurantOrders(@RequestHeader("Authorization") String token);

    @RequestMapping(value = "orders/{orderId}", method = POST)
    @ResponseBody
    ResponseEntity<RestaurantOrder> updateOrder(@RequestHeader("Authorization") String token,
                                                @PathVariable("orderId") final Long orderId,
                                                @RequestBody final UpdateOrderParams params);
}
