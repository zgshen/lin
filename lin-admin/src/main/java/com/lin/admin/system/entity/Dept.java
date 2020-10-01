package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("sys_dept")
@ApiModel(value = "部门对象")
public class Dept implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门 ID
	 */
	@TableId
	@ApiModelProperty(value = "部门 ID", name = "deptId", example = "0")
	private Long deptId;
	/**
	 * 上级部门 ID，一级部门为0
	 */
	@ApiModelProperty(value = "上级部门 ID，一级部门为0", name = "parentId", example = "0")
	private Long parentId;
	/**
	 * 部门名称
	 */
	@ApiModelProperty(value = "部门名称", name = "name")
	private String name;
	/**
	 * 排序
	 */
	@ApiModelProperty(value = "排序", name = "orderNum", example = "1")
	private Integer orderNum;
	/**
	 * 是否删除  -1：已删除  0：正常
	 */
	@ApiModelProperty(value = "是否删除  -1：已删除  0：正常", name = "delFlag", example = "0")
	private Integer delFlag;

	/**
	 * 子节点
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "子节点", name = "children")
	private List<Dept> children;

}
