package com.lin.mockito.dao;

import com.lin.mockito.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    public User getUserById(int id) {
        return new User().setId(1)
                .setName("nathan")
                .setAge(25);
    }

}
