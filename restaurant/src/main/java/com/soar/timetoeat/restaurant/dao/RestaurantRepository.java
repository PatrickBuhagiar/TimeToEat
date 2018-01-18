package com.soar.timetoeat.restaurant.dao;

import com.soar.timetoeat.restaurant.domain.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

@RestResource(path = "restaurants", rel="restaurants")
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    Restaurant findByName(final String name);

    Set<Restaurant> findAll();
}
