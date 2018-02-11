package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.faults.ClientException;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("RESTAURANT-SERVICE")
public interface RestaurantClient {

    @RequestMapping(value = "/restaurant", method = GET)
    @ResponseBody
    Restaurant getRestaurantByOwner(@RequestHeader("Authorization") String token) throws ClientException;

    @RequestMapping(value = "restaurants", method = POST)
    @ResponseBody
    ResponseEntity<Restaurant> createRestaurant(@RequestHeader("Authorization") String token,
                                                @RequestBody final CreateRestaurantParams params) throws ClientException;
}
