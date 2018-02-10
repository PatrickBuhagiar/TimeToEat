package com.soar.timetoeat.auth;

import com.soar.timetoeat.auth.dao.ApplicationUserRepository;
import com.soar.timetoeat.util.params.auth.CreateUserParams;
import com.soar.timetoeat.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
     * @param params the user creation parameters
     * @return the created user
     */
    @RequestMapping(name = "users/register", method = POST)
    public @ResponseBody
    User registerUser(@RequestBody final CreateUserParams params) {
        final User newUser = User.UserBuilder.aUser()
                .withRole(params.getRole())
                .withEmail(params.getEmail())
                .withUsername(params.getUsername())
                .withPassword(bCryptPasswordEncoder.encode(params.getPassword()))
                .build();
        return applicationUserRepository.save(newUser);
    }
}
