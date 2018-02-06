package com.soar.timetoeat.util.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class Authorisation {

    /**
     * This retrieves the currently logged in user's username. This is extracted from
     * the JWT being sent with each API calls.
     *
     * @return the username
     */
    public static Optional<String> getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.of(authentication.getName());
        } else return Optional.empty();
    }

}
