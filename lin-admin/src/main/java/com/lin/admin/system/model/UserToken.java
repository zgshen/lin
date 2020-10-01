package com.lin.admin.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "用户 token 对象")
public class UserToken implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户 ID", name = "userId", example = "0")
    private Long userId;
    @ApiModelProperty(value = "账号", name = "username")
    private String username;
    @ApiModelProperty(value = "用户名", name = "name")
    private String name;
    @ApiModelProperty(value = "密码", name = "password")
    private String password;
    @ApiModelProperty(value = "部门 ID", name = "deptId", example = "0")
    private Long deptId;

}
