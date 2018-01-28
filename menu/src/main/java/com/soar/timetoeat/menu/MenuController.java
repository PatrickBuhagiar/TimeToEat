package com.soar.timetoeat.menu;

import com.soar.timetoeat.menu.dao.MenuRepository;
import com.soar.timetoeat.menu.domain.Menu;
import com.soar.timetoeat.menu.domain.params.CreateMenuParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.soar.timetoeat.menu.utils.Converter.convert;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MenuController {

    private final MenuRepository repository;

    @Autowired
    public MenuController(final MenuRepository repository) {
        this.repository = repository;
    }

    /**
     * Endpoint for fetching a {@link Menu} for a particular restaurant
     *
     * @param restaurantId the restaurant's ID
     * @return the retrieved {@link Menu}
     */
    @RequestMapping(value = "restaurants/{restaurantId}/menu", method = GET)
    public @ResponseBody
    Menu getMenu(@PathVariable("restaurantId") final Long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    /**
     * Endpoint for creating a {@link Menu} for a given restaurant
     *
     * @param restaurantId the restaurant's ID
     * @param params the creation parameters
     * @return the created {@link Menu}
     */
    @RequestMapping(value = "restaurants/{restaurantId}/menu", method = POST)
    public @ResponseBody
    Menu createMenu(@PathVariable final long restaurantId, @RequestBody final CreateMenuParams params) {
        return repository.save(convert(params, restaurantId));
    }
}
