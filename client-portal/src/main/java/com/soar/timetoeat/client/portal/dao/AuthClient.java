package com.soar.timetoeat.client.portal.dao;

import com.soar.timetoeat.util.params.auth.LoginRequest;
import com.soar.timetoeat.util.params.auth.CreateUserParams;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("AUTH-SERVICE")
public interface AuthClient {

    @RequestMapping(value = "login", method = POST)
    ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest);

    @RequestMapping(value = "users/register", method = POST)
    ResponseEntity<Void> register(@RequestBody final CreateUserParams loginRequest);
}
