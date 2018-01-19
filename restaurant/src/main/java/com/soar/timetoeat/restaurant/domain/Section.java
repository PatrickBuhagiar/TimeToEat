package com.soar.timetoeat.restaurant.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Section implements Serializable {

    private String name;
    private Set<Item> items;

    public Section() {
    }

    public Section(final String name, final Set<Item> items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public Set<Item> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        final Section section = (Section) o;
        return Objects.equals(getName(), section.getName()) &&
                Objects.equals(getItems(), section.getItems());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getItems());
    }
}
