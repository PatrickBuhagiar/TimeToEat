package com.soar.timetoeat.restaurant.dao;

import com.soar.timetoeat.restaurant.domain.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource(path = "restaurants")
public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    Restaurant findByName(final String name);

    Set<Restaurant> findAll();
}
