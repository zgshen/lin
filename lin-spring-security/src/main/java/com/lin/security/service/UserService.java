package com.lin.security.service;

import com.lin.security.entity.CustomUser;
import org.springframework.stereotype.Service;

/**
 * 模拟数据库操作
 */
@Service
public class UserService {

    public CustomUser getUserByUsername(String username) {
        return new CustomUser(1, "", "", null);
    }

}
