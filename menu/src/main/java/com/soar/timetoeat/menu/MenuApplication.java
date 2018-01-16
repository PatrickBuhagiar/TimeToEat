package com.soar.timetoeat.menu;

import com.soar.timetoeat.menu.dao.MenuRepository;
import com.soar.timetoeat.menu.domain.Item;
import com.soar.timetoeat.menu.domain.Menu;
import com.soar.timetoeat.menu.domain.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MenuApplication {

    @Autowired
    private MenuRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(MenuApplication.class, args);
    }

    @PostConstruct
    public void init() {
        repository.save(
                Menu.MenuBuilder.aMenu()
                        .withRestaurantId(123)
                        .addSection(
                                Section.SectionBuilder.aSection()
                                        .withName("Starters")
                                        .addItem(
                                                Item.ItemBuilder.anItem()
                                                        .withName("something")
                                                        .withDescription("whatever")
                                                        .withUnitPrice(1.2)
                                                        .build()
                                        ).build()
                        ).build()
        );
    }

}
