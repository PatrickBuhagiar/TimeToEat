package com.soar.timetoeat.restaurant.domain;

import com.soar.timetoeat.restaurant.dao.MenuClient;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * This is a simplified version of Menu without the IDs that will be
 * populated in the Feign dao {@link MenuClient}.
 */
public class Menu implements Serializable {

    private Set<Section> sections;

    public Menu() {
    }

    public Menu(final Set<Section> sections) {
        this.sections = sections;
    }

    public Set<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        final Menu menu = (Menu) o;
        return Objects.equals(getSections(), menu.getSections());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getSections());
    }
}
