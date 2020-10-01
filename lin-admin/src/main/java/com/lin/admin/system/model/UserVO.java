package com.lin.admin.system.model;

import com.lin.admin.system.entity.User;
import lombok.Data;

@Data
public class UserVO {
    /**
     * 更新的用户对象
     */
    private User userDO = new User();
    /**
     * 旧密码
     */
    private String pwdOld;
    /**
     * 新密码
     */
    private String pwdNew;

}
