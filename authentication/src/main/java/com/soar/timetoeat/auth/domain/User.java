package com.soar.timetoeat.auth.domain;

import com.soar.timetoeat.util.domain.auth.UserRole;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private UserRole role;

    public User() {
    }

    public User(final UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.role = builder.role;
    }

    public long getId() {
        return id;
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

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User user = (User) o;
        return getId() == user.getId() &&
                Objects.equals(fullName, user.fullName) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                getRole() == user.getRole();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), fullName, getUsername(), getPassword(), getEmail(), getRole());
    }

    public static final class UserBuilder {
        private String username;
        private String password;
        private String email;
        private UserRole role;
        public String fullName;

        private UserBuilder() {
        }

        public static UserBuilder aUser() {
            return new UserBuilder();
        }

        public UserBuilder withUsername(final String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(final String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEmail(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withRole(final UserRole role) {
            this.role = role;
            return this;
        }

        public UserBuilder withFullName(final String fullName) {
            this.fullName = fullName;
            return this;

        }

        public User build() {
            return new User(this);
        }
    }
}
