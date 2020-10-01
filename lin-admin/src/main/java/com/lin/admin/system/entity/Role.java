package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("sys_role")
@ApiModel(value = "角色对象")
public class Role {

	/**
	 * 角色 ID
	 */
	@TableId
	@ApiModelProperty(value = "角色 ID", name = "roleId", example = "0")
	private Long roleId;

	/**
	 * 角色名称
	 */
	@ApiModelProperty(value = "角色名称", name = "roleName")
	private String roleName;

	/**
	 * 角色标识
	 */
	@ApiModelProperty(value = "角色标识", name = "roleSign")
	private String roleSign;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注", name = "remark")
	private String remark;

	/**
	 * 业务单元类型
	 */
	@ApiModelProperty(value = "业务单元类型", name = "buType")
	private String buType;

	/**
	 * 创建人 ID
	 */
	@ApiModelProperty(value = "创建人 ID", name = "userIdCreate", example = "0")
	private Long userIdCreate;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", name = "gmtCreate")
	private Date gmtCreate;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间", name = "gmtCreate")
	private Date gmtModified;

	/**
	 * 菜单 ID 集合
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "菜单 ID 集合", name = "menuIds")
	private List<Long> menuIds;

}
