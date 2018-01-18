package com.soar.timetoeat.restaurant.domain;

import java.util.Objects;

/**
 * Parameters for creating a {@link Restaurant}
 */
public class CreateRestaurantParams {
    private String name;
    private String address;

    private CreateRestaurantParams() {}

    private CreateRestaurantParams(final CreateRestaurantParamsBuilder builder) {
        this.name = builder.name;
        this.address = builder.address;
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
        if (!(o instanceof CreateRestaurantParams)) return false;
        final CreateRestaurantParams that = (CreateRestaurantParams) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAddress());
    }


    public static final class CreateRestaurantParamsBuilder {
        private String name;
        private String address;

        private CreateRestaurantParamsBuilder() {
        }

        public static CreateRestaurantParamsBuilder aCreateRestaurantParams() {
            return new CreateRestaurantParamsBuilder();
        }

        public CreateRestaurantParamsBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreateRestaurantParamsBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public CreateRestaurantParams build() {
            return new CreateRestaurantParams(this);
        }
    }
}
