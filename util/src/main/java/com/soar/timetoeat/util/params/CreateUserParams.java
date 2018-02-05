package com.soar.timetoeat.util.params;

import com.soar.timetoeat.util.domain.UserRole;

import java.io.Serializable;
import java.util.Objects;

public class CreateUserParams implements Serializable {
    private String username;
    private String password;
    private String email;
    private UserRole role;

    public CreateUserParams(){}

    public CreateUserParams(final CreateUserParamsBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.role = builder.role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateUserParams)) return false;
        final CreateUserParams that = (CreateUserParams) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                getRole() == that.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getRole());
    }

    public static final class CreateUserParamsBuilder {
        private String username;
        private String password;
        private String email;
        private UserRole role;

        private CreateUserParamsBuilder() {
        }

        public static CreateUserParamsBuilder aCreateUserParams() {
            return new CreateUserParamsBuilder();
        }

        public CreateUserParamsBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public CreateUserParamsBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public CreateUserParamsBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public CreateUserParamsBuilder withRole(UserRole role) {
            this.role = role;
            return this;
        }

        public CreateUserParams build() {
            return new CreateUserParams(this);
        }
    }
}
