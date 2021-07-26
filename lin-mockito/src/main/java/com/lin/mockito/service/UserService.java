package com.lin.mockito.service;

import com.lin.mockito.dao.UserDao;
import com.lin.mockito.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }
}
