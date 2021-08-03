package com.lin.mockito.controller;

import com.lin.mockito.model.User;
import com.lin.mockito.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("/getUserById")
    public User getUserById(int id) {
        return userService.getUserById(id);
    }

}
