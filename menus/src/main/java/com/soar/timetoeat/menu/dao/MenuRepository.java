package com.soar.timetoeat.menu.dao;

import com.soar.timetoeat.menu.domain.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

public interface MenuRepository extends CrudRepository<Menu, Long> {

    Menu findByRestaurantId(final long restaurantId);
}
