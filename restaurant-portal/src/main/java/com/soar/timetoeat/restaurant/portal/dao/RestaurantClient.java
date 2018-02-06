package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.domain.restaurant.RestaurantWithMenu;
import com.soar.timetoeat.util.params.CreateRestaurantParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("RESTAURANT-SERVICE")
public interface RestaurantClient {

    @RequestMapping(value = "/restaurant", method = GET)
    @ResponseBody
    ResponseEntity<Restaurant> getRestaurantByOwner(@RequestHeader("Authorization") String token);

    @RequestMapping(value = "/restaurant/{restaurantName}", method = GET)
    @ResponseBody
    ResponseEntity<RestaurantWithMenu> getRestaurant(@PathVariable final String restaurantName);

    @RequestMapping(value = "restaurants", method = POST)
    @ResponseBody
    ResponseEntity<Restaurant> createRestaurant(@RequestHeader("Authorization") String token,
                                @RequestBody final CreateRestaurantParams params);
}
