package com.lin.mockito.service;

import com.lin.mockito.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 目标类里面 Alt + Insert 选择 Test 选项创建测试类
 * 右键项目模块 Run "All Tests" with Coverage 可查看测试覆盖率
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void getUserById() {
        User user = userService.getUserById(1);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUserId(), 1);
        Assert.assertEquals(user.getUsername(), "admin");
    }
}