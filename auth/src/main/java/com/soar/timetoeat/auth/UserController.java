package com.soar.timetoeat.auth;

import com.soar.timetoeat.auth.dao.UserRepository;
import com.soar.timetoeat.auth.domain.CreateUserParams;
import com.soar.timetoeat.auth.domain.User;
import com.soar.timetoeat.auth.domain.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.soar.timetoeat.auth.utils.Converter.convert;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController(value = "users/")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(name = "restaurant", method = POST)
    public @ResponseBody
    User createRestaurantUser(@RequestBody final CreateUserParams params) {
        return userRepository.save(convert(params));
    }
}
