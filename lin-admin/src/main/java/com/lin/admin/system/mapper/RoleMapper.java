package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.system.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 角色 mapper
 */
public interface RoleMapper extends BaseMapper<Role> {

    Role get(Long roleId);

    List<Role> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int update(Role role);

    int remove(Long roleId);

    int batchRemove(Long[] roleIds);

    @Select("select distinct (bu_type) from sys_role where role_id in (" +
            "select role_id from sys_user_role where user_id = #{userId})")
    List<String> selectRoleBuType(@Param("userId") Long userId);
}
