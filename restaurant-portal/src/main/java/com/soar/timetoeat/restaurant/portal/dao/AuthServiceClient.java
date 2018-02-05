package com.soar.timetoeat.restaurant.portal.dao;

import com.soar.timetoeat.restaurant.portal.domain.LoginRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="auth-service", url="http://localhost:3004")
public interface AuthServiceClient {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> login(@RequestBody final LoginRequest loginRequest);
}
