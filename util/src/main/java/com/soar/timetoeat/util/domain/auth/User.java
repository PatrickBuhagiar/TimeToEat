package com.soar.timetoeat.util.domain.auth;

import java.io.Serializable;
import java.util.Objects;

/**
 * Yes, yes, this is a duplication of the User class
 * in Auth-Service. But this class is used in every
 * service for JWT authentication and authorisation
 */
public class User implements Serializable {

    private long id;
    private String username;
    private String fullName;
    private String password;
    private String email;
    private UserRole role;

    public User() {}

    public User(final long id,
                final String username,
                final String fullName,
                final String password,
                final String email,
                final UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.fullName = fullName;
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
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getFullName(), user.getFullName()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                getRole() == user.getRole();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getUsername(), getFullName(), getPassword(), getEmail(), getRole());
    }
}
