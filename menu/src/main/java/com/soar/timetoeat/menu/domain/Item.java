package com.soar.timetoeat.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * The Item class
 */
@Entity
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private double unitPrice;

    private Item() {
    }

    private Item(ItemBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.unitPrice = builder.unitPrice;
    }

    public long getId() {
        return id;
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
        return getId() == item.getId() &&
                Double.compare(item.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getName(), item.getName()) &&
                Objects.equals(getDescription(), item.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getUnitPrice());
    }

    public static final class ItemBuilder {
        private String name;
        private String description;
        private double unitPrice;

        private ItemBuilder() {
        }

        public static ItemBuilder anItem() {
            return new ItemBuilder();
        }

        public ItemBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder withUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Item build() {
            return new Item(this);
        }
    }

}
