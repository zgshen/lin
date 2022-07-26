package com.lin.config.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    private final Environment environment;

    public ConfigController(Environment environment) {
        this.environment = environment;
    }

    @RequestMapping("/info")
    public String info() {
        String property = environment.getProperty("PRIVATE_KEY");
        System.out.println(property);
        return "config project";
    }
}
