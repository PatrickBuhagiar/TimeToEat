package com.soar.timetoeat.restaurant;

import com.soar.timetoeat.restaurant.dao.MenuClient;
import com.soar.timetoeat.restaurant.dao.RestaurantRepository;
import com.soar.timetoeat.restaurant.domain.CreateRestaurantParams;
import com.soar.timetoeat.restaurant.domain.Menu;
import com.soar.timetoeat.restaurant.domain.Restaurant;
import com.soar.timetoeat.restaurant.domain.RestaurantWithMenu;
import com.soar.timetoeat.restaurant.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RestaurantController {

    private final RestaurantRepository repository;
    private final MenuClient menuClient;

    @Autowired
    public RestaurantController(final RestaurantRepository repository,
                                final MenuClient menuClient) {
        this.repository = repository;
        this.menuClient = menuClient;
    }

    @RequestMapping(value = "restaurants/{restaurantName}", method = GET)
    public @ResponseBody
    RestaurantWithMenu getRestaurant(@PathVariable final String restaurantName) {
        final Restaurant restaurant = repository.findByName(restaurantName);
        final Menu menu = menuClient.getMenu(restaurant.getId());
        return RestaurantWithMenu.buildFrom(restaurant, menu);
    }

    @RequestMapping(value = "restaurants", method = POST)
    public @ResponseBody
    Restaurant createRestaurant(@RequestBody final CreateRestaurantParams params) {
        return repository.save(Converter.convert(params));
    }

    @RequestMapping(value = "restaurants", method = GET)
    public @ResponseBody
    Set<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }
}
