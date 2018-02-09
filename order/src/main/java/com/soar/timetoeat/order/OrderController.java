package com.soar.timetoeat.order;

import com.soar.timetoeat.order.dao.OrderRepository;
import com.soar.timetoeat.order.domain.RestaurantOrder;
import com.soar.timetoeat.util.domain.order.OrderState;
import com.soar.timetoeat.util.params.order.CreateOrderParams;
import com.soar.timetoeat.util.params.order.UpdateOrderParams;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.soar.timetoeat.order.utils.Converter.convert;
import static com.soar.timetoeat.order.utils.Converter.convertForMessage;
import static com.soar.timetoeat.util.security.Authorisation.getLoggedInUsername;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class OrderController {

    private final OrderRepository repository;
    private final JmsTemplate jmsTemplate;
    private Session session;

    private HashMap<String, Queue> queueMap = new HashMap<>();

    @Autowired
    public OrderController(final OrderRepository repository,
                           final JmsTemplate jmsTemplate) throws JMSException {
        this.repository = repository;
        this.jmsTemplate = jmsTemplate;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:3000");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @RequestMapping(value = "restaurants/{restaurantName}/checkout", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<RestaurantOrder> createOrder(@PathVariable("restaurantName") final String restaurantName,
                                                @RequestBody final CreateOrderParams params) throws JMSException {
        //We are creating a queue per restaurant. Create a new one if not already created
        if (!queueMap.containsKey(restaurantName)) {
            queueMap.put(restaurantName, session.createQueue("res-" + restaurantName));
        }

        if (getLoggedInUsername().isPresent()) {
            final String username = getLoggedInUsername().get();
            final RestaurantOrder order = repository.save(convert(restaurantName, params, username));
            //Send Message to Restaurant Queue
            final MessageProducer producer = session.createProducer(queueMap.get(restaurantName));
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            final MapMessage message = session.createMapMessage();
            message.setLong("id", order.getId());
            message.setString("address", order.getDeliveryAddress());
            message.setDouble("total", order.getTotalPrice());
            producer.send(message);

            return ResponseEntity.status(CREATED).body(order);
        } else {
            return ResponseEntity.status(UNAUTHORIZED).body(null);
        }


    }

    @RequestMapping(value = "orders/restaurant", method = GET)
    public @ResponseBody
    Set<RestaurantOrder> getRestaurantOrders() {
        return repository.findByRestaurantUsername(getLoggedInUsername().get());
    }

    @RequestMapping(value = "orders/client", method = GET)
    public @ResponseBody
    Set<RestaurantOrder> getClientOrders() {
        return repository.findByClientUsername(getLoggedInUsername().get());
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
        if (order.getState().equals(OrderState.AWAIT_APPROVAL) && !params.getState().equals(OrderState.DECLINED)) {
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
