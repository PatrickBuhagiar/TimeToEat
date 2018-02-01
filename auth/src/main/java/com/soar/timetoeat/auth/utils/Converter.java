package com.soar.timetoeat.auth.utils;

import com.soar.timetoeat.auth.domain.CreateUserParams;
import com.soar.timetoeat.auth.domain.User;
import com.soar.timetoeat.auth.domain.User.UserBuilder;

/**
 * A utility class for performing conversions between classes
 */
public class Converter {

    /**
     * Convert from {@link CreateUserParams} to {@link User}
     *
     * @param params the creation params
     * @return the created user
     */
    public static User convert(final CreateUserParams params) {
        return UserBuilder.aUser()
                .withEmail(params.getEmail())
                .withPassword(params.getPassword())
                .withUsername(params.getUsername())
                .withRole(params.getRole())
                .build();
    }
}
