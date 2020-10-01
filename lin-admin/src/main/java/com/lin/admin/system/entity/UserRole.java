package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_user_role")
@ApiModel(value = "用户角色对象")
public class UserRole {
    @TableId
    @ApiModelProperty(value = "用户角色 ID", name = "id", example = "1")
    private Long id;
    @ApiModelProperty(value = "用户 ID", name = "userId", example = "1")
    private Long userId;
    @ApiModelProperty(value = "角色 ID", name = "roleId", example = "1")
    private Long roleId;

    @Override
    public String toString() {
        return "UserRoleDO{" +
                "id=" + id +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
