package com.soar.timetoeat.menu;

import com.soar.timetoeat.menu.domain.params.CreateMenuParams;
import com.soar.timetoeat.menu.utils.Generator;
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

import static com.soar.timetoeat.menu.utils.Converter.convert;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuControllerTest {

    @LocalServerPort
    private int port;

    @Test
    public void test_createMenu() {
        final long restaurantId = Unique.idValue();
        final CreateMenuParams createMenuParams = Generator.generateMenuParams(2, 2);

        Bastion.request(JsonRequest
                .postFromModel("http://localhost:" + port + "/restaurants/" + restaurantId + "/menu",
                        createMenuParams))
        .withAssertions(JsonResponseAssertions.fromModel(201, convert(createMenuParams, restaurantId)));
    }

    @Test
    public void test_getMenu() {
        final long restaurantId = Unique.idValue();
        final CreateMenuParams createMenuParams = Generator.generateMenuParams(2, 2);

        Bastion.request(JsonRequest
                .postFromModel("http://localhost:" + port + "/restaurants/" + restaurantId + "/menu",
                        createMenuParams))
                .withAssertions(JsonResponseAssertions.fromModel(201, convert(createMenuParams, restaurantId)));

        Bastion.request(GeneralRequest.get("http://localhost:" + port + "/restaurants/" + restaurantId + "/menu"))
                .withAssertions(JsonResponseAssertions.fromModel(201, convert(createMenuParams, restaurantId)));

    }
}
