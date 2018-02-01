package com.soar.timetoeat.util.domain;

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
    private String password;
    private String email;
    private UserRole role;

    public User() {}

    public User(final long id,
                final String username,
                final String password,
                final String email,
                final UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        final User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) &&
                role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, role);
    }
}
