package com.soar.timetoeat.restaurant.portal.config;

import com.soar.timetoeat.restaurant.portal.handler.FeignErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ErrorDecoder cbsErrorDecoder() {
        return new FeignErrorDecoder();
    }
}
