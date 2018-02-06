package com.soar.timetoeat.menu.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The Menu class
 */
@Entity
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long restaurantId;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

    private Menu() {
    }

    private Menu(final MenuBuilder builder) {
        this.restaurantId = builder.restaurantId;
        this.items = builder.items;
    }

    public long getId() {
        return id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        final Menu menu = (Menu) o;
        return getId() == menu.getId() &&
                getRestaurantId() == menu.getRestaurantId() &&
                Objects.equals(getItems(), menu.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRestaurantId(), getItems());
    }

    public static final class MenuBuilder {
        private long restaurantId;
        private Set<Item> items = new HashSet<>();

        private MenuBuilder() {
        }

        public static MenuBuilder aMenu() {
            return new MenuBuilder();
        }

        public MenuBuilder withRestaurantId(long restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public MenuBuilder withItems(Set<Item> items) {
            this.items = items;
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}


