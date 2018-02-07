package com.soar.timetoeat.client.portal.dao;

import com.soar.timetoeat.util.domain.restaurant.Restaurant;
import com.soar.timetoeat.util.domain.restaurant.RestaurantWithMenu;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("RESTAURANT-SERVICE")
public interface RestaurantClient {

    @RequestMapping(value = "/restaurant/{restaurantName}", method = GET)
    @ResponseBody
    ResponseEntity<RestaurantWithMenu> getRestaurant(@PathVariable("restaurantName") final String restaurantName);

    @RequestMapping(value = "restaurants", method = GET)
    @ResponseBody
    Set<Restaurant> getAllRestaurants();
}
