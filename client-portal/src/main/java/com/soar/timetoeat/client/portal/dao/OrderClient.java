package com.soar.timetoeat.client.portal.dao;

import com.soar.timetoeat.util.domain.order.RestaurantOrder;
import com.soar.timetoeat.util.params.order.CreateOrderParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("ORDER-SERVICE")
public interface OrderClient {

    @RequestMapping(value = "orders/{orderId}", method = GET)
    @ResponseBody
    ResponseEntity<RestaurantOrder> getOrder(@RequestHeader("Authorization") String token,
                                             @PathVariable("orderId") final Long orderId);

    @RequestMapping(value = "restaurants/{restaurantId}/checkout", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<RestaurantOrder> createOrder(@RequestHeader("Authorization") String token,
                                                @PathVariable("restaurantId") final Long restaurantId,
                                                @RequestBody final CreateOrderParams params);
}
