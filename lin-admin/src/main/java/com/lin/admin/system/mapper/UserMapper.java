package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.system.entity.User;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * 用户 mapper
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过用户 ID 查找
     *
     * @param userId 用户 ID
     * @return
     */
    User get(Long userId);

    /**
     * 通过用户名查找
     *
     * @param username 用户名
     * @return
     */
    @Select(" SELECT * FROM SYS_USER WHERE USERNAME=#{username} ")
    User getByUsername(String username);

    List<User> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(User user);

    int update(User user);

    int batchRemove(Long[] userIds);

    Long[] listAllDept();

}