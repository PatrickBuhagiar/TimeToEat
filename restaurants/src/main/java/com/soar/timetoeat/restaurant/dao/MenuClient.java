package com.soar.timetoeat.restaurant.dao;

import com.soar.timetoeat.util.domain.menu.Menu;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * There are three main advantages of using a Feign Client for calling
 * another service.
 * 1) You have better control over what attributes you receive. For example,
 *    in the {@link Menu} class, we do not need the menu and restaurant IDs
 *    in the case of calling the menu service from the restaurant service.
 * 2) Rather than having a stub will all the possible calls (which in itself
 *    is an advantage from a developer's perspective) and generated
 *    implemented codebase in our project library, Feign takes a declarative
 *    approach and only requires this interfaces with the calls it actually
 *    needs. The beauty of feign is that the implementation for making this
 *    API call is implemented on runtime.
 * 3) Integrates well with Service discovery. MENU-SERVICE is the service name
 *    of the menu service, as it is registered on the eureka server. This is
 *    translated to the actual IP address and port number at runtime. No need
 *    to remember port numbers :) 
 */
@FeignClient("MENU-SERVICE")
public interface MenuClient {

    @RequestMapping(value = "/restaurants/{restaurantId}/menu", method = RequestMethod.GET, consumes = "application/json")
    Menu getMenu(@PathVariable("restaurantId") final Long restaurantId);
}
