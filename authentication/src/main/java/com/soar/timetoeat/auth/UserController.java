package com.soar.timetoeat.auth;

import com.soar.timetoeat.auth.dao.ApplicationUserRepository;
import com.soar.timetoeat.util.faults.auth.*;
import com.soar.timetoeat.util.params.auth.CreateUserParams;
import com.soar.timetoeat.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.regex.Pattern;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserController(final ApplicationUserRepository applicationUserRepository,
                          final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Register a new User.
     *
     * @param params the user creation parameters
     * @return the created user
     */
    @RequestMapping(name = "users/register", method = POST)
    public @ResponseBody
    User registerUser(@RequestBody final CreateUserParams params) throws PasswordTooSimpleException, IncorrectEmailFormatException, UsernameNotUniqueException, EmailNotUniqueException, UsernameNotDefinedException {

        validateRegisterParams(params);
        final User newUser = User.UserBuilder.aUser()
                .withRole(params.getRole())
                .withEmail(params.getEmail())
                .withUsername(params.getUsername())
                .withFullName(params.getFullName())
                .withPassword(bCryptPasswordEncoder.encode(params.getPassword()))
                .build();
        return applicationUserRepository.save(newUser);
    }

    private void validateRegisterParams(final CreateUserParams params) throws IncorrectEmailFormatException, PasswordTooSimpleException, UsernameNotUniqueException, EmailNotUniqueException, UsernameNotDefinedException {
        Pattern emailPattern = Pattern.compile("\\b[a-z0-9._%-]+@[a-z0-9.-]+\\.[a-z]{2,4}\\b");
        Pattern passwordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");

        if (params.getUsername().isEmpty()) {
            throw new UsernameNotDefinedException();
        }

        if (!emailPattern.matcher(params.getEmail()).matches()) {
            throw new IncorrectEmailFormatException("Not a valid Email");
        }

        if (!Objects.isNull(applicationUserRepository.findByEmail(params.getEmail()))) {
            throw new EmailNotUniqueException("Email already exists", params.getEmail());
        }

        if (!passwordPattern.matcher(params.getPassword()).matches()) {
            throw new PasswordTooSimpleException("Password must contain at least 1 small letter, \n1 capital letter, 1 number and at least 6 to 20 characters long");
        }

        if (!Objects.isNull(applicationUserRepository.findByUsername(params.getUsername()))) {
            throw new UsernameNotUniqueException("Username already exists", params.getUsername());
        }

    }
}
