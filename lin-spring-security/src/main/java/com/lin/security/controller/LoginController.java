package com.lin.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String index() {
        System.out.println("Login success.");
        return "success";
    }
}
