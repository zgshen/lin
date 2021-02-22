package com.lin.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/403")
    public String page403() {
        return "403";
    }

    @GetMapping("/404")
    public String page404() {
        return "404";
    }

    @GetMapping("/500")
    public String page500() {
        return "500";
    }
}
