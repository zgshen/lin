package com.lin.admin.common.controller;


import com.lin.admin.common.utils.ShiroUtils;
import com.lin.admin.system.entity.User;

/**
 * 基础控制器
 */
public class BaseController {

	/**
	 * 获取用户信息
	 * @return
	 */
	public User getUser() {
		return ShiroUtils.getUser();
	}

	/**
	 * 获取用户 ID
	 * @return
	 */
	public Long getUserId() {
		return getUser().getUserId();
	}

	/**
	 * 获取用户名
	 * @return
	 */
	public String getUsername() {
		return getUser().getUsername();
	}

}