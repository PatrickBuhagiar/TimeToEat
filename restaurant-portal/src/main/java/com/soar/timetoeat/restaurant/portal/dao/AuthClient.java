package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.util.faults.ClientException;
import com.soar.timetoeat.util.params.auth.CreateUserParams;
import com.soar.timetoeat.util.params.auth.LoginRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("AUTH-SERVICE")
public interface AuthClient {

    @RequestMapping(value = "login", method = POST)
    ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest) throws ClientException;

    @RequestMapping(value = "users/register", method = POST)
    ResponseEntity<Void> register(@RequestBody final CreateUserParams loginRequest) throws ClientException;
}
