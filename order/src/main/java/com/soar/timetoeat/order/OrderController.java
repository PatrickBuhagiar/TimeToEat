package com.soar.timetoeat.order;

import com.soar.timetoeat.order.dao.OrderRepository;
import com.soar.timetoeat.order.domain.RestaurantOrder;
import com.soar.timetoeat.order.domain.params.CreateOrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.soar.timetoeat.order.utils.Converter.convert;

@RestController
public class OrderController {

    private final OrderRepository repository;

    @Autowired
    public OrderController(final OrderRepository repository) {
        this.repository = repository;
    }

    @PostMapping(value = "orders/{restaurantId}/checkout")
    public @ResponseBody
    RestaurantOrder createOrder(@PathVariable("restaurantId") final Long restaurantId,
                                @RequestBody final CreateOrderParams params) {
        return repository.save(convert(restaurantId, params));
    }

    @GetMapping(value = "orders/{restaurantId}")
    public @ResponseBody
    List<RestaurantOrder> getRestaurantOrders(@PathVariable("restaurantId") final Long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }


}
