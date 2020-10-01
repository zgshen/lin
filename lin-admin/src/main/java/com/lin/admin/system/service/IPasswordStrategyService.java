package com.lin.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.system.entity.PasswordStrategy;
import java.util.Map;

/**
 * 密码策略 IService
 */
public interface IPasswordStrategyService extends IService<PasswordStrategy> {

	/**
	 * 查询当前系统的系统配置
	 */
	PasswordStrategy getCurrentSystemPasswordStrategy(Map params);
}
