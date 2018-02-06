package com.soar.timetoeat.util.domain.menu;

import java.io.Serializable;
import java.util.Objects;

public class Item implements Serializable {

    private String name;
    private String description;
    private double unitPrice;

    public Item() {
    }

    public Item(final String name, final String description, final double unitPrice) {
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        final Item item = (Item) o;
        return Double.compare(item.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getName(), item.getName()) &&
                Objects.equals(getDescription(), item.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getDescription(), getUnitPrice());
    }
}
