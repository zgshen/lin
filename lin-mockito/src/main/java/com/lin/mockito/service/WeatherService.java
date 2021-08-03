package com.lin.mockito.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class WeatherService {

    @Autowired
    RestTemplate restTemplate;

    public String RemoteWeatherResquest() {
        ResponseEntity<String> entity = restTemplate.getForEntity("https://free-api.heweather" +
                ".com/v5/weather?key=0e551ab59fa34785af066024763673bd&city=guangzhou", String.class);
        return entity.getBody();
    }
}
