package com.soar.timetoeat.util.params.menu;

import java.util.Objects;
import java.util.Set;

/**
 * Parameters for creating a Menu
 */
public class CreateMenuParams {
    private Set<CreateItemParams> items;

    private CreateMenuParams() {
    }

    private CreateMenuParams(final CreateMenuParamsBuilder builder) {
        this.items = builder.items;
    }

    public Set<CreateItemParams> getItems() {
        return items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateMenuParams)) return false;
        final CreateMenuParams that = (CreateMenuParams) o;
        return Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {

        return Objects.hash(items);
    }

    public static final class CreateMenuParamsBuilder {
        private Set<CreateItemParams> items;

        private CreateMenuParamsBuilder() {
        }

        public static CreateMenuParamsBuilder aCreateMenuParams() {
            return new CreateMenuParamsBuilder();
        }

        public CreateMenuParamsBuilder withItems(Set<CreateItemParams> items) {
            this.items = items;
            return this;
        }

        public CreateMenuParams build() {
            return new CreateMenuParams(this);
        }
    }
}
