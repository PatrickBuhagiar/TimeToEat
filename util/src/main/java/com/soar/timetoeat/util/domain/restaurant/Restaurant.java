package com.soar.timetoeat.util.domain.restaurant;

import java.io.Serializable;
import java.util.Objects;

public class Restaurant implements Serializable {

    private long id;
    private String name;
    private String address;

    public Restaurant() {
    }

    public Restaurant(final long id, final String name, final String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant)) return false;
        final Restaurant that = (Restaurant) o;
        return getId() == that.getId() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getAddress());
    }
}
