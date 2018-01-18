package com.soar.timetoeat.menu.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The Section class
 */
@Entity
public class Section implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

    private Section() {
    }

    private Section(final SectionBuilder builder) {
        this.name = builder.name;
        this.items = builder.items;
    }

    public long getId() {
        return id;
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
        return getId() == section.getId() &&
                Objects.equals(getName(), section.getName()) &&
                Objects.equals(getItems(), section.getItems());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getItems());
    }

    public static final class SectionBuilder {
        private String name;
        private Set<Item> items;

        private SectionBuilder() {
            items = new HashSet<>();
        }

        public static SectionBuilder aSection() {
            return new SectionBuilder();
        }

        public SectionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SectionBuilder withItems(Set<Item> items) {
            this.items = items;
            return this;
        }

        public SectionBuilder addItem(final Item item) {
            this.items.add(item);
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }
}
