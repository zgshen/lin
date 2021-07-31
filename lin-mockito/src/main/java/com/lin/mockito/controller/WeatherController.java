package com.lin.mockito.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@RestController
public class WeatherController {

    RestTemplate restTemplate;

    @PostConstruct
    void construct() {
        restTemplate = new RestTemplate();
    }

    @RequestMapping("/getWeatherInfo")
    public String getWeatherInfo() {
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://free-api.heweather" +
                ".com/v5/weather?key=0e551ab59fa34785af066024763673bd&city=guangzhou", String.class);
        String body = forEntity.getBody();
        return body;
    }

}
