package com.soar.timetoeat.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
public class Section implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private List<Item> items;

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

    public List<Item> getItems() {
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
        private List<Item> items;

        private SectionBuilder() {
        }

        public static SectionBuilder aSection() {
            return new SectionBuilder();
        }

        public SectionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public SectionBuilder withItems(List<Item> items) {
            this.items = items;
            return this;
        }

        public Section build() {
            return new Section(this);
        }
    }
}
