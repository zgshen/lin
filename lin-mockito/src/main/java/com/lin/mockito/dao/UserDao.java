package com.lin.mockito.dao;

import com.lin.mockito.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public User getUserById(int id) {
        return jdbcTemplate.queryForObject("select user_id,username,is_lock,city from sys_user where user_id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
    }

}
