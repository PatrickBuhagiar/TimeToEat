package com.soar.timetoeat.menu.domain.params;

import com.soar.timetoeat.menu.domain.Section;

import java.util.Objects;
import java.util.Set;

/**
 * Parameters for creating a {@link Section}
 */
public class CreateSectionParams {

    private String name;
    private Set<CreateItemParams> items;

    public String getName() {
        return name;
    }

    public Set<CreateItemParams> getItems() {
        return items;
    }

    private CreateSectionParams() {
    }

    private CreateSectionParams(final CreateSectionParamsBuilder builder) {
        this.name = builder.name;
        this.items = builder.items;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateSectionParams)) return false;
        final CreateSectionParams that = (CreateSectionParams) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getItems(), that.getItems());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getName(), getItems());
    }


    public static final class CreateSectionParamsBuilder {
        private String name;
        private Set<CreateItemParams> items;

        private CreateSectionParamsBuilder() {
        }

        public static CreateSectionParamsBuilder aCreateSectionParams() {
            return new CreateSectionParamsBuilder();
        }

        public CreateSectionParamsBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public CreateSectionParamsBuilder withItems(Set<CreateItemParams> items) {
            this.items = items;
            return this;
        }

        public CreateSectionParams build() {
            return new CreateSectionParams(this);
        }
    }
}
