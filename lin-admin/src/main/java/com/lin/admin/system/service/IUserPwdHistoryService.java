package com.lin.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.system.entity.UserPwdHistory;

import java.util.List;

public interface IUserPwdHistoryService extends IService<UserPwdHistory> {

    /**
     * 查询列表
     *
     * @param userId 用户 ID
     * @return
     */
    List<UserPwdHistory> getList(Long userId);

    /**
     * 删除
     *
     * @param userId 用户 ID
     */
    void delete(Long userId);
}
