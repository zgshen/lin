package com.lin.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @RequestMapping("/test")
    public String test() {
        return "ok";
    }

    @RequestMapping("/auth/test")
    public String authTest() {
        return "ok";
    }

    @RequestMapping("/auth/test/one")
    public String authTest1() {
        return "ok";
    }

}
