package com.soar.timetoeat.order.dao;

import com.soar.timetoeat.order.domain.RestaurantOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface OrderRepository extends CrudRepository<RestaurantOrder, Long> {

    List<RestaurantOrder> findByRestaurantUsername(final String restaurantUsername);
}
