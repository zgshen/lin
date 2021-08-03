package com.lin.mockito.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SystemConfig {

    @Bean
    RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

}
