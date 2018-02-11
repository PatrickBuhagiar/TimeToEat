package com.soar.timetoeat.order.dao;

import com.soar.timetoeat.order.domain.RestaurantOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

public interface OrderRepository extends CrudRepository<RestaurantOrder, Long> {

    Set<RestaurantOrder> findByRestaurantUsername(final String restaurantUsername);

    Set<RestaurantOrder> findByClientUsername(final String clientUsername);
}
