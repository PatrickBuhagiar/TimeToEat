package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.util.domain.Restaurant;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient("RESTAURANT-SERVICE")
public interface RestaurantClient {

    @RequestMapping(value = "/restaurant", method = GET)
    public @ResponseBody
    Restaurant getRestaurantByOwner(@RequestHeader("Authorization") String token);

}
