package com.soar.timetoeat.restaurant;

import com.soar.timetoeat.util.params.restaurant.CreateRestaurantParams;
import com.soar.timetoeat.restaurant.utils.Generator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rocks.bastion.Bastion;
import rocks.bastion.core.json.JsonRequest;
import rocks.bastion.core.json.JsonResponseAssertions;

import static com.soar.timetoeat.restaurant.utils.Converter.convert;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void test_createRestaurant() {
        final CreateRestaurantParams params = Generator.generateRestaurantParams();

        Bastion.request(JsonRequest
                .postFromModel("http://localhost:" + port + "/restaurants",
                        params))
                .withAssertions(JsonResponseAssertions.fromModel(200, convert(params, null)));

    }


}
