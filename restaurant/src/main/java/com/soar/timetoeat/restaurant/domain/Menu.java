package com.soar.timetoeat.restaurant.domain;

import com.soar.timetoeat.restaurant.dao.MenuClient;

import java.util.Set;

/**
 * This is a simplified version of Menu without the IDs that will be
 * populated in the Feign client {@link MenuClient}.
 */
public class Menu {

    private Set<Section> sections;

    public Menu() {
    }

    public Menu(final Set<Section> sections) {
        this.sections = sections;
    }

    public Set<Section> getSections() {
        return sections;
    }

    private class Section {

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

        private class Item {

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
        }
    }
}
