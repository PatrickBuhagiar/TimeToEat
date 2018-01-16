package com.soar.timetoeat.menu;

import com.soar.timetoeat.menu.dao.MenuRepository;
import com.soar.timetoeat.menu.domain.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class MenuController {

    private final MenuRepository repository;

    @Autowired
    public MenuController(final MenuRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "restaurants/{restaurantId}/menu", method = GET)
    public @ResponseBody
    Menu getMenu(@PathVariable final long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }
}
