package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.system.entity.UserPwdHistory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 用户密码修改历史 Mapper
 */
public interface UserPwdHistoryMapper extends BaseMapper<UserPwdHistory> {

    /**
     * 查询密码修改历史
     *
     * @param userId 用户 ID
     * @return
     */
    @Select(" SELECT * FROM SYS_USER_PWD_HISTORY WHERE USER_ID=#{userId} ORDER BY CREATE_TIME DESC ")
    List<UserPwdHistory> getList(Long userId);

    /**
     * 删除密码修改历史
     *
     * @param userId 用户 ID
     */
    @Delete(" DELETE FROM SYS_USER_PWD_HISTORY WHERE USER_ID=#{userId} ")
    void deleteByUserId(Long userId);
}
