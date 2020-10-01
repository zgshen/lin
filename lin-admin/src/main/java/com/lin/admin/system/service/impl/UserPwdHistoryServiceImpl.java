package com.lin.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.system.entity.UserPwdHistory;
import com.lin.admin.system.mapper.UserPwdHistoryMapper;
import com.lin.admin.system.service.IUserPwdHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户密码修改历史 Service
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserPwdHistoryServiceImpl extends ServiceImpl<UserPwdHistoryMapper, UserPwdHistory> implements IUserPwdHistoryService {

    @Autowired
    private UserPwdHistoryMapper userPwdHistoryMapper;

    @Override
    public List<UserPwdHistory> getList(Long userId) {
        return userPwdHistoryMapper.getList(userId);
    }

    @Override
    public void delete(Long userId) {
        userPwdHistoryMapper.deleteByUserId(userId);
    }
}
