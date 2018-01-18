package com.soar.timetoeat.restaurant;

import com.soar.timetoeat.restaurant.dao.RestaurantRepository;
import com.soar.timetoeat.restaurant.domain.CreateRestaurantParams;
import com.soar.timetoeat.restaurant.domain.Restaurant;
import com.soar.timetoeat.restaurant.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class RestaurantController {

    private final RestaurantRepository repository;

    @Autowired
    public RestaurantController(final RestaurantRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "restaurants/{restaurantName}", method = RequestMethod.GET)
    public @ResponseBody
    Restaurant getRestaurant(@PathVariable final String restaurantName) {
        return repository.findByName(restaurantName);
    }

    @RequestMapping(value = "restaurants", method = RequestMethod.POST)
    public @ResponseBody
    Restaurant createRestaurant(@RequestBody final CreateRestaurantParams params) {
        return repository.save(Converter.convert(params));
    }

    @RequestMapping(value = "restaurants", method = RequestMethod.GET)
    public @ResponseBody
    Set<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }
}
