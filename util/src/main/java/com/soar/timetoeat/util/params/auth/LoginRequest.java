package com.soar.timetoeat.util.params.auth;

import java.io.Serializable;
import java.util.Objects;

public class LoginRequest implements Serializable{
    private String username;
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginRequest)) return false;
        final LoginRequest that = (LoginRequest) o;
        return Objects.equals(getUsername(), that.getUsername()) &&
                Objects.equals(getPassword(), that.getPassword());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUsername(), getPassword());
    }
}
