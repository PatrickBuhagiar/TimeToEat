package com.soar.timetoeat.order;

import com.google.common.collect.ImmutableList;
import com.soar.timetoeat.order.domain.params.CreateOrderParams;
import com.soar.timetoeat.order.utils.Generator;
import com.soar.timetoeat.util.Unique;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rocks.bastion.Bastion;
import rocks.bastion.core.GeneralRequest;
import rocks.bastion.core.json.JsonRequest;
import rocks.bastion.core.json.JsonResponseAssertions;

import java.util.HashSet;

import static com.soar.timetoeat.order.utils.Converter.convert;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void test_CreateOrder() {
        final long restaurantId = Unique.idValue();
        final CreateOrderParams createOrderParams = Generator.generateOrderParams(2);

        Bastion.request(JsonRequest
                .postFromModel("http://localhost:" + port + "/orders" + restaurantId + "/checkout",
                        createOrderParams))
                .withAssertions(JsonResponseAssertions.fromModel(200, convert(restaurantId, createOrderParams)));
    }

    @Test
    public void test_getOrders() {
        final long restaurantId = Unique.idValue();
        final CreateOrderParams createOrderParams = Generator.generateOrderParams(2);
        final CreateOrderParams createOrder2Params = Generator.generateOrderParams(3);

        Bastion.request(JsonRequest
                .postFromModel("http://localhost:" + port + "/orders/" + restaurantId + "/checkout",
                        createOrderParams))
                .withAssertions(JsonResponseAssertions.fromModel(200, convert(restaurantId, createOrderParams)));

        Bastion.request(JsonRequest
                .postFromModel("http://localhost:" + port + "/orders/" + restaurantId + "/checkout",
                        createOrderParams))
                .withAssertions(JsonResponseAssertions.fromModel(200, convert(restaurantId, createOrder2Params)));

        Bastion.request(GeneralRequest
                .get("http://localhost:" + port + "/orders/" + restaurantId))
                .withAssertions(JsonResponseAssertions.fromModel(200,
                        ImmutableList.of(convert(restaurantId, createOrderParams),
                                convert(restaurantId, createOrder2Params))));
    }
}
