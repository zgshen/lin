package com.lin.mockito.service;

import com.lin.mockito.dao.UserDao;
import com.lin.mockito.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 目标类里面 Alt + Insert 选择 Test 选项创建测试类
 * 右键项目模块 Run "All Tests" with Coverage 可查看测试覆盖率
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserDao userDao;

    @BeforeEach
    void setUp() {
        Mockito.when(userDao.getUserById(1))
                .thenReturn(new User().setId(1)
                        .setName("Jane")
                        .setAge(25));
    }

    @Test
    void getUserById() {
        User user = userService.getUserById(1);
        Assert.assertNotNull(user);
        //Assert.assertEquals(user.getName(), "nathan");
        Assert.assertEquals(user.getName(), "Jane");
    }
}