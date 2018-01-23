package com.soar.timetoeat.order.dao;

import com.soar.timetoeat.order.domain.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findByRestaurantId(final long restaurantId);
}
