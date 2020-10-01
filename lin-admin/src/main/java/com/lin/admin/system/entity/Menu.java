package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "菜单对象")
@TableName("sys_menu")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 菜单 ID
	 */
	@TableId
	@ApiModelProperty(value = "菜单 ID", name = "deptId", example = "0")
	private Long menuId;
	/**
	 * 父菜单ID，一级菜单为0
	 */
	@ApiModelProperty(value = "父菜单ID，一级菜单为0", name = "parentId", example = "0")
	private Long parentId;
	/**
	 * 菜单名称
	 */
	@ApiModelProperty(value = "菜单名称", name = "name")
	private String name;
	/**
	 * 菜单 URL
	 */
	@ApiModelProperty(value = "菜单 URL", name = "url")
	private String url;

	/**
	 * VUE 菜单 URL
	 */
	@ApiModelProperty(value = "VUE 菜单 URL", name = "url")
	private String vueUrl;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	@ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)", name = "perms")
	private String perms;
	/**
	 * 类型 0：目录 1：菜单 2：按钮
	 */
	@ApiModelProperty(value = "类型 0：目录 1：菜单 2：按钮", name = "type", example = "0")
	private Integer type;
	/**
	 * 菜单图标
	 */
	@ApiModelProperty(value = "菜单图标", name = "icon")
	private String icon;

	/**
	 * VUE 菜单图标
	 */
	@ApiModelProperty(value = "VUE 菜单图标", name = "icon")
	private String vueIcon;

	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序", name = "orderNum", example = "1")
	private Integer orderNum;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", name = "gmtCreate")
	private Date gmtCreate;
	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间", name = "gmtModified")
	private Date gmtModified;

}
