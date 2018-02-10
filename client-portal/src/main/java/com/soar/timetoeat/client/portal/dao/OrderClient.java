package com.soar.timetoeat.client.portal.dao;

import com.soar.timetoeat.util.domain.order.RestaurantOrder;
import com.soar.timetoeat.util.params.order.CreateOrderParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("ORDER-SERVICE")
public interface OrderClient {

    @RequestMapping(value = "restaurants/{restaurantName}/checkout", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<RestaurantOrder> createOrder(@RequestHeader("Authorization") final String token,
                                                @PathVariable("restaurantName") final String restaurantName,
                                                @RequestBody final CreateOrderParams params);

    @RequestMapping(value = "orders/client", method = GET)
    public @ResponseBody
    Set<RestaurantOrder> getClientOrders(@RequestHeader("Authorization") final String token);
}
