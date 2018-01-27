package com.soar.timetoeat.order;

import com.soar.timetoeat.order.dao.OrderRepository;
import com.soar.timetoeat.order.domain.RestaurantOrder;
import com.soar.timetoeat.order.domain.params.CreateOrderParams;
import com.soar.timetoeat.order.domain.params.UpdateOrderParams;
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

    @PostMapping(value = "restaurants/{restaurantId}/checkout")
    public @ResponseBody
    RestaurantOrder createOrder(@PathVariable("restaurantId") final Long restaurantId,
                                @RequestBody final CreateOrderParams params) {
        return repository.save(convert(restaurantId, params));
    }

    @GetMapping(value = "restaurants/{restaurantId}")
    public @ResponseBody
    List<RestaurantOrder> getRestaurantOrders(@PathVariable("restaurantId") final Long restaurantId) {
        return repository.findByRestaurantId(restaurantId);
    }

    @PostMapping(value = "orders/{orderId}")
    public @ResponseBody
    RestaurantOrder updateOrder(@PathVariable("orderId") final Long orderId,
                                @RequestBody final UpdateOrderParams params) {
        final RestaurantOrder order = repository.findOne(orderId);
        order.setExpectedDeliveryTime(params.getExpectedDeliveryTime());
        order.setState(params.getState());
        return repository.save(order);
    }


}