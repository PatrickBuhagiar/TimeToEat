package com.soar.timetoeat.menu.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long restaurantId;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    private Menu() {
    }

    private Menu(final MenuBuilder builder) {
        this.restaurantId = builder.restaurantId;
        this.sections = builder.sections;
    }

    public long getId() {
        return id;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public Set<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        final Menu menu = (Menu) o;
        return getId() == menu.getId() &&
                getRestaurantId() == menu.getRestaurantId() &&
                Objects.equals(getSections(), menu.getSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRestaurantId(), getSections());
    }

    public static final class MenuBuilder {
        private long restaurantId;
        private Set<Section> sections = new HashSet<>();

        private MenuBuilder() {
        }

        public static MenuBuilder aMenu() {
            return new MenuBuilder();
        }

        public MenuBuilder withRestaurantId(long restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public MenuBuilder withSections(Set<Section> sections) {
            this.sections = sections;
            return this;
        }

        public MenuBuilder addSection(final Section section) {
            this.sections.add(section);
            return this;
        }

        public Menu build() {
            return new Menu(this);
        }
    }
}


