package com.soar.timetoeat.order;

import com.soar.timetoeat.order.dao.OrderRepository;
import com.soar.timetoeat.order.domain.RestaurantOrder;
import com.soar.timetoeat.util.domain.order.OrderState;
import com.soar.timetoeat.util.params.order.CreateOrderParams;
import com.soar.timetoeat.util.params.order.UpdateOrderParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.soar.timetoeat.order.utils.Converter.convert;
import static com.soar.timetoeat.util.security.Authorisation.getLoggedInUsername;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class OrderController {

    private final OrderRepository repository;

    @Autowired
    public OrderController(final OrderRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "restaurants/{restaurantId}/checkout", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<RestaurantOrder> createOrder(@PathVariable("restaurantId") final Long restaurantId,
                                                @RequestBody final CreateOrderParams params) {
        return getLoggedInUsername()
                .map(username -> ResponseEntity.status(CREATED).body(repository.save(convert(restaurantId, params, username))))
                .orElseGet(() -> ResponseEntity.status(UNAUTHORIZED).body(null));
    }

    @RequestMapping(value = "restaurants/{restaurantId}", method = GET)
    public @ResponseBody
    List<RestaurantOrder> getRestaurantOrders() {
        return repository.findByRestaurantUsername(getLoggedInUsername().get());
    }

    @RequestMapping(value = "orders/{orderId}", method = POST)
    public @ResponseBody
    ResponseEntity<RestaurantOrder> updateOrder(@PathVariable("orderId") final Long orderId,
                                                @RequestBody final UpdateOrderParams params) {
        final RestaurantOrder order = repository.findOne(orderId);
        if (Objects.isNull(order)) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        final String loggedInRestaurantUsername = getLoggedInUsername().get();
        //Case when order gets approved for the first time, we set the restaurant username
        if (order.getState().equals(OrderState.W) && !params.getState().equals(OrderState.D)) {
            order.setRestaurantUsername(loggedInRestaurantUsername);
        }
        if (!Objects.isNull(order.getRestaurantUsername())
                && !order.getRestaurantUsername().equals(loggedInRestaurantUsername)) {
            return ResponseEntity.status(UNAUTHORIZED).body(null);
        }
        if (!Objects.isNull(order.getExpectedDeliveryTime())) {
            order.setExpectedDeliveryTime(params.getExpectedDeliveryTime());
        }
        order.setState(params.getState());
        return ResponseEntity.status(HttpStatus.OK).body(repository.save(order));
    }

    @RequestMapping(value = "orders/{orderId}", method = GET)
    public @ResponseBody
    ResponseEntity<RestaurantOrder> getOrder(@PathVariable("orderId") final Long orderId) {

        final RestaurantOrder order = repository.findOne(orderId);
        if (Objects.isNull(order)) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }

        final String loggedInUsername = getLoggedInUsername().get();
        if (loggedInUsername.equals(order.getClientUsername())
                || loggedInUsername.equals(order.getRestaurantUsername())) {
            return ResponseEntity.status(OK).body(order);
        } else {
            return ResponseEntity.status(UNAUTHORIZED).body(null);
        }
    }
}
