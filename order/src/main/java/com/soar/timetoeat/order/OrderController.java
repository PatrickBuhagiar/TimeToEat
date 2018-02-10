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
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

import static com.soar.timetoeat.order.utils.Converter.convert;
import static com.soar.timetoeat.util.security.Authorisation.getLoggedInUsername;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class OrderController {

    private final OrderRepository repository;
    private Session session;

    private HashMap<String, Queue> queueMap = new HashMap<>();
    private HashMap<String, Topic> topicMap = new HashMap<>();

    @Autowired
    public OrderController(final OrderRepository repository) throws JMSException {
        this.repository = repository;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:3000");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * Create a new order.
     *
     * @param restaurantName the name of the restaurant
     * @param params         the order creation parameters
     * @return a response entity with the created Order
     * @throws JMSException a JMS exception
     */
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
            final RestaurantOrder createdOrder = repository.save(convert(restaurantName, params, username));
            //Send Message to Restaurant Queue
            publishToQueue(restaurantName, createdOrder);

            return ResponseEntity.status(CREATED).body(createdOrder);
        } else {
            return ResponseEntity.status(UNAUTHORIZED).body(null);
        }
    }

    /**
     * Send a message to the queue.
     *
     * @param restaurantName the name of the restaurant
     * @param createdOrder   the createdOrder
     * @throws JMSException a JMS Exception
     */
    private void publishToQueue(final @PathVariable("restaurantName") String restaurantName,
                                final RestaurantOrder createdOrder) throws JMSException {
        final MessageProducer producer = session.createProducer(queueMap.get(restaurantName));
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        final TextMessage message = session.createTextMessage();
        StringBuilder builder = new StringBuilder();
        message.setText(builder.append("New Order of:\n")
                .append(createdOrder.getItemsAsString())
                .append("\nto ")
                .append(createdOrder.getDeliveryAddress())
                .append("\nfor £")
                .append(createdOrder.getTotalPrice())
                .append(". Accept order?")
                .toString());
        message.setLongProperty("id", createdOrder.getId());
        producer.send(message);
    }

    /**
     * Get the orders for a particular restaurant. To prevent anyone from accessing
     * other restaurants' orders, orders are search by the current logged in restaurant
     * user.
     *
     * Orders that are declined by a restaurant do not include the restaurant's username.
     * This means that declined orders are not retrieved from this method call.
     *
     * @return The orders for the logged in restaurant.
     */
    @RequestMapping(value = "orders/restaurant", method = GET)
    public @ResponseBody
    Set<RestaurantOrder> getRestaurantOrders() {
        return repository.findByRestaurantUsername(getLoggedInUsername().get());
    }

    /**
     * Similar to {@link OrderController#getRestaurantOrders()}, where a client can retrieve
     * his or her orders, but by searching by the current logged in user. This prevents
     * other people from searching for other client's orders.
     *
     * @return the orders for the logged in client
     */
    @RequestMapping(value = "orders/client", method = GET)
    public @ResponseBody
    Set<RestaurantOrder> getClientOrders() {
        return repository.findByClientUsername(getLoggedInUsername().get());
    }

    /**
     * Update an order. The order must match with the logged in restaurant username in order
     * to succeed.
     *
     * @param orderId the order id
     * @param params  the update parameters
     * @return a response entity with the updated restaurant order
     * @throws JMSException a JMS exception
     */
    @RequestMapping(value = "orders/{orderId}", method = POST)
    public @ResponseBody
    ResponseEntity<RestaurantOrder> updateOrder(@PathVariable("orderId") final Long orderId,
                                                @RequestBody final UpdateOrderParams params) throws JMSException {
        //search for order
        final RestaurantOrder order = repository.findOne(orderId);
        if (Objects.isNull(order)) {
            return ResponseEntity.status(NOT_FOUND).body(null);
        }
        //get logged in user
        final String loggedInRestaurantUsername = getLoggedInUsername().get();
        //Case when order gets approved for the first time, we set the restaurant username
        if (order.getState().equals(OrderState.AWAIT_APPROVAL) && !params.getState().equals(OrderState.DECLINED)) {
            order.setRestaurantUsername(loggedInRestaurantUsername);
        }
        //AUTHORISATION: The logged in username must match the order's restaurant username
        if (!Objects.isNull(order.getRestaurantUsername())
                && !order.getRestaurantUsername().equals(loggedInRestaurantUsername)) {
            return ResponseEntity.status(UNAUTHORIZED).body(null);
        }
        //set expected delivery time if present in parameters
        if (!Objects.isNull(params.getExpectedDeliveryTime())) {
            order.setExpectedDeliveryTime(params.getExpectedDeliveryTime());
        }
        //update order status
        order.setState(params.getState());
        final RestaurantOrder updatedOrder = repository.save(order);

        //publish order update to topic
        publishToTopic(updatedOrder);

        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    /**
     * Publish the updated order to topic
     *
     * @param updatedOrder the updated order
     * @throws JMSException a JMS exception
     */
    private void publishToTopic(final RestaurantOrder updatedOrder) throws JMSException {
        if (!topicMap.containsKey(updatedOrder.getClientUsername())) {
            topicMap.put(updatedOrder.getClientUsername(), session.createTopic("cli-" + updatedOrder.getClientUsername()));
        }
        final MessageProducer producer = session.createProducer(topicMap.get(updatedOrder.getClientUsername()));
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        final TextMessage message = session.createTextMessage();
        message.setText(preparePushNotificationMessage(updatedOrder));
        producer.send(message);
    }

    /**
     * Prepare the message to be sent to the topic
     *
     * @param updatedOrder the updated order
     * @return the message to be sent
     */
    private String preparePushNotificationMessage(final RestaurantOrder updatedOrder) {
        final StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Your order of: \n")
                .append(updatedOrder.getItemsAsString())
                .append("\nfrom ")
                .append(updatedOrder.getRestaurantName())
                .append("\nis now ")
                .append(updatedOrder.getState().getDescription());
        if (!(updatedOrder.getState().equals(OrderState.DELIVERED) || updatedOrder.getState().equals(OrderState.DECLINED))) {
            messageBuilder.append("\nand will be delivered at \n")
                    .append(updatedOrder.getHumanizedExpectedDeliveryTime());
        }
        return messageBuilder.toString();
    }
}
