package com.soar.timetoeat.util.params.menu;

import java.util.Objects;

/**
 * Parameters for creating an Item
 */
public class CreateItemParams {
    private String name;
    private String description;
    private double unitPrice;

    private CreateItemParams() {
    }

    private CreateItemParams(final CreateItemParamsBuilder builder) {
        this.name = builder.name;
        this.description = builder.name;
        this.unitPrice = builder.unitPrice;
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
        if (!(o instanceof CreateItemParams)) return false;
        final CreateItemParams that = (CreateItemParams) o;
        return Double.compare(that.getUnitPrice(), getUnitPrice()) == 0 &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getDescription(), getUnitPrice());
    }


    public static final class CreateItemParamsBuilder {
        private String name;
        private String description;
        private double unitPrice;

        private CreateItemParamsBuilder() {
        }

        public static CreateItemParamsBuilder aCreateItemParams() {
            return new CreateItemParamsBuilder();
        }

        public CreateItemParamsBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreateItemParamsBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public CreateItemParamsBuilder withUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public CreateItemParams build() {
            CreateItemParams createItemParams = new CreateItemParams();
            createItemParams.unitPrice = this.unitPrice;
            createItemParams.description = this.description;
            createItemParams.name = this.name;
            return createItemParams;
        }
    }
}
