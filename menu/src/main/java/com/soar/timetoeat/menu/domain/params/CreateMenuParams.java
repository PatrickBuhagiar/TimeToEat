package com.soar.timetoeat.menu.domain.params;

import com.soar.timetoeat.menu.domain.Menu;

import java.util.Objects;
import java.util.Set;

/**
 * Parameters for creating a {@link Menu}
 */
public class CreateMenuParams {
    private Set<CreateSectionParams> sections;

    private CreateMenuParams() {
    }

    private CreateMenuParams(final CreateMenuParamsBuilder builder) {
        this.sections = builder.sections;
    }

    public Set<CreateSectionParams> getSections() {
        return sections;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateMenuParams)) return false;
        final CreateMenuParams that = (CreateMenuParams) o;
        return Objects.equals(sections, that.sections);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sections);
    }

    public static final class CreateMenuParamsBuilder {
        private Set<CreateSectionParams> sections;

        private CreateMenuParamsBuilder() {
        }

        public static CreateMenuParamsBuilder aCreateMenuParams() {
            return new CreateMenuParamsBuilder();
        }

        public CreateMenuParamsBuilder withSections(Set<CreateSectionParams> sections) {
            this.sections = sections;
            return this;
        }

        public CreateMenuParams build() {
            CreateMenuParams createMenuParams = new CreateMenuParams();
            createMenuParams.sections = this.sections;
            return createMenuParams;
        }
    }
}
