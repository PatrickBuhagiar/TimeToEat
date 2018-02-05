package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.restaurant.portal.domain.LoginRequest;
import com.soar.timetoeat.util.params.CreateUserParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "auth-service", url = "http://localhost:3004")
public interface AuthServiceClient {

    @RequestMapping(value = "/login", method = POST)
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest);

    @RequestMapping(value = "/users/register", method = POST)
    public ResponseEntity<Void> register(@RequestBody final CreateUserParams loginRequest);
}
