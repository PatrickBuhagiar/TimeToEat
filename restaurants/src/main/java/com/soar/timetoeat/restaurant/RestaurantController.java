package com.soar.timetoeat.restaurant;

import com.soar.timetoeat.restaurant.dao.MenuClient;
import com.soar.timetoeat.restaurant.dao.RestaurantRepository;
import com.soar.timetoeat.restaurant.domain.Restaurant;
import com.soar.timetoeat.util.domain.menu.Menu;
import com.soar.timetoeat.util.domain.restaurant.RestaurantWithMenu;
import com.soar.timetoeat.util.faults.auth.UnAuthenticatedException;
import com.soar.timetoeat.util.faults.restaurant.RestaurantAddressNotDefinedException;
import com.soar.timetoeat.util.faults.restaurant.RestaurantNameNotDefinedException;
import com.soar.timetoeat.util.faults.restaurant.RestaurantNotFoundException;
import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.soar.timetoeat.restaurant.utils.Converter.convert;
import static com.soar.timetoeat.util.security.Authorisation.getLoggedInUsername;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
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

    /**
     * Get Restaurant by name. Authentication is not required for this particular call.
     * Unlike {@link RestaurantController#getRestaurantByOwner()}, this also returns
     * the menu of that restaurant. Naturally this call will be slower.
     *
     * @param restaurantName the name of the restaurant
     * @return a response entity with the fetched restaurant
     */
    @RequestMapping(value = "restaurants/{restaurantName}", method = GET)
    public @ResponseBody
    ResponseEntity<RestaurantWithMenu> getRestaurant(@PathVariable final String restaurantName) throws RestaurantNotFoundException {
        final Restaurant restaurant = repository.findByName(restaurantName);
        if (Objects.isNull(restaurant)) {
            throw new RestaurantNotFoundException(restaurantName);
        }
        final Menu menu = menuClient.getMenu(restaurant.getId());
        return ResponseEntity.status(OK).body(convert(restaurant, menu));
    }

    /**
     * Create a new Restaurant. The logged in restaurant username is persisted with
     * the restaurant as the owner
     *
     * @param params the restaurant creation parameters
     * @return a response entity with the created Restaurant
     */
    @RequestMapping(value = "restaurants", method = POST)
    public @ResponseBody
    ResponseEntity<Restaurant> createRestaurant(@RequestBody final CreateRestaurantParams params) throws UnAuthenticatedException, RestaurantNameNotDefinedException, RestaurantAddressNotDefinedException {
        validate(params);
        return getLoggedInUsername()
                .map(username -> ResponseEntity.status(HttpStatus.CREATED).body(repository.save(convert(params, username))))
                .orElseThrow(UnAuthenticatedException::new);
    }

    private void validate(final CreateRestaurantParams params) throws RestaurantNameNotDefinedException, RestaurantAddressNotDefinedException {
        if (Objects.isNull(params.getName()) || params.getName().isEmpty()) {
            throw new RestaurantNameNotDefinedException();
        }

        if (Objects.isNull(params.getAddress()) || params.getAddress().isEmpty()) {
            throw new RestaurantAddressNotDefinedException();
        }
    }

    /**
     * Get All restaurants. This call does not need authorisation.
     *
     * @return a set of restaurants
     */
    @RequestMapping(value = "restaurants", method = GET)
    public @ResponseBody
    Set<Restaurant> getAllRestaurants() {
        return repository.findAll();
    }

    /**
     * Retrieve a restaurant by the current logged in restaurant user. Unlike
     * {@link RestaurantController#getRestaurant(String)}, this only returns the
     * restaurant details, not the menu
     *
     * @return the fetched restaurant
     */
    @RequestMapping(value = "restaurant", method = GET)
    public @ResponseBody
    Restaurant getRestaurantByOwner() throws UnAuthenticatedException {
        final Optional<String> loggedInUsername = getLoggedInUsername();
        if (loggedInUsername.isPresent()) {
                return repository.findByOwner(loggedInUsername.get());
        } else {
            throw new UnAuthenticatedException();
        }
    }
}
