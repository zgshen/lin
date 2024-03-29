package com.lin.mockito.controller;

import com.lin.mockito.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @RequestMapping("/getWeatherInfo")
    public String getWeatherInfo() {
        return weatherService.RemoteWeatherResquest();
    }

}
