package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "角色关联菜单对象")
@TableName("sys_role_menu")
public class RoleMenu {
	@TableId
	@ApiModelProperty(value = "角色关联菜单 ID", name = "id", example = "1")
	private Long id;
	@ApiModelProperty(value = "角色 ID", name = "roleId", example = "1")
	private Long  roleId;
	@ApiModelProperty(value = "菜单 ID", name = "menuId", example = "1")
	private Long menuId;
	
	@Override
	public String toString() {
		return "RoleMenuDO{" +
				"id=" + id +
				", roleId=" + roleId +
				", menuId=" + menuId +
				'}';
	}
}
