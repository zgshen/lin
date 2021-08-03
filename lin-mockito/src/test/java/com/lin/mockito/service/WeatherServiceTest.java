package com.lin.mockito.service;

import com.lin.mockito.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@Slf4j
@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
    }

    @Test
    void remoteWeatherResquest() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://free-api.heweather" +
                ".com/v5/weather?key=0e551ab59fa34785af066024763673bd&city=guangzhou", String.class);
        String body = forEntity.getBody();
        Assert.assertNotNull(body);
        log.info(body);
    }

}