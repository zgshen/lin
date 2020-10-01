package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.lin.admin.system.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户与角色对应关系 mapper
 */
public interface UserRoleMapper extends Mapper<UserRole> {

    UserRole get(Long id);

    List<UserRole> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(UserRole userRole);

    int update(UserRole userRole);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Long> listRoleId(Long userId);

    int removeByUserId(@Param("userId") Long userId);

    int removeByRoleId(Long roleId);

    int batchSave(List<UserRole> list);

    int batchRemoveByUserId(Long[] ids);
}
