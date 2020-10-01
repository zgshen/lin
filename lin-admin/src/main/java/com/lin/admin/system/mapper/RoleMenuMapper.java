package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.lin.admin.system.entity.RoleMenu;

import java.util.List;
import java.util.Map;

/**
 * 角色与菜单对应关系
 */
public interface RoleMenuMapper extends Mapper<RoleMenu> {

    RoleMenu get(Long id);

    List<RoleMenu> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(RoleMenu roleMenu);

    int update(RoleMenu roleMenu);

    int remove(Long id);

    int batchRemove(Long[] ids);

    List<Long> listMenuIdByRoleId(Long roleId);

    int removeByRoleId(Long roleId);

    int removeByMenuId(Long menuId);

    int batchSave(List<RoleMenu> list);
}
