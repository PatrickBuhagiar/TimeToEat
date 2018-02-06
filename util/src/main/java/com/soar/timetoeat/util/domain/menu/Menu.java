package com.soar.timetoeat.util.domain.menu;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * This is a simplified version of Menu without the IDs.
 */
public class Menu implements Serializable {

    private Set<Item> items;

    public Menu() {
    }

    public Menu(final Set<Item> items) {
        this.items = items;
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        final Menu menu = (Menu) o;
        return Objects.equals(getItems(), menu.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItems());
    }
}
