package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.util.domain.menu.Menu;
import com.soar.timetoeat.util.params.menu.CreateMenuParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("MENU-SERVICE")
public interface MenuClient {

    @RequestMapping(value = "restaurants/{restaurantId}/menu", method = GET)
    @ResponseBody
    Menu getMenu(@PathVariable("restaurantId") final Long restaurantId);

    @RequestMapping(value = "restaurants/{restaurantId}/menu", method = POST)
    @ResponseBody
    Menu createMenu(@RequestHeader("Authorization") String token,
                    @PathVariable("restaurantId") final long restaurantId,
                    @RequestBody final CreateMenuParams params);
}
