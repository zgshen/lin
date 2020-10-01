package com.lin.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.system.entity.PasswordStrategy;
import com.lin.admin.system.mapper.PasswordStrategyMapper;
import com.lin.admin.system.service.IPasswordStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 密码策略 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PasswordStrategyServiceImpl extends ServiceImpl<PasswordStrategyMapper, PasswordStrategy> implements IPasswordStrategyService {

    @Autowired
    private PasswordStrategyMapper passwordStrategyMapper;

    @Override
    public PasswordStrategy getCurrentSystemPasswordStrategy(Map params) {
        if (params == null) {
            params = new HashMap(0);
            params.put("id", 1);
        }
        List<PasswordStrategy> passwordStrategies = passwordStrategyMapper.selectByMap(params);
        if (passwordStrategies != null && passwordStrategies.size() > 0) {
            return passwordStrategies.get(0);
        }
        return new PasswordStrategy();
    }
}
