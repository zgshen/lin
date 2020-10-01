package com.lin.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.common.service.IDictService;
import com.lin.admin.system.entity.Role;
import com.lin.admin.system.entity.RoleMenu;
import com.lin.admin.system.mapper.RoleMapper;
import com.lin.admin.system.mapper.RoleMenuMapper;
import com.lin.admin.system.mapper.UserRoleMapper;
import com.lin.admin.system.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private IDictService dictService;

    @Override
    public List<Role> list() {
        List<Role> roles = roleMapper.list(new HashMap<>(16));
        return roles;
    }

    @Override
    public List<Role> list(Map<String, Object> params) {
        List<Role> roles = roleMapper.list(params);
        for (Role role : roles) {
            Long roleId = role.getRoleId();
            List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(roleId);
            role.setMenuIds(menuIds);
        }
        return roles;
    }

    @Override
    public List<Role> list(Long userId) {
        List<Long> rolesIds = userRoleMapper.listRoleId(userId);
        List<Role> roles = roleMapper.list(new HashMap<>(16));
        for (Role roleDO : roles) {
            roleDO.setRoleSign("false");
            for (Long roleId : rolesIds) {
                if (Objects.equals(roleDO.getRoleId(), roleId)) {
                    roleDO.setRoleSign("true");
                    break;
                }
            }
        }
        return roles;
    }

    @Transactional
    @Override
    public int saveRole(Role role) {
        int count = roleMapper.insert(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        List<RoleMenu> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu rmDo = new RoleMenu();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        roleMenuMapper.removeByRoleId(roleId);
        if (rms.size() > 0) {
            roleMenuMapper.batchSave(rms);
        }
        return count;
    }

    @Transactional
    @Override
    public int remove(Long id) {
        int count = roleMapper.remove(id);
        userRoleMapper.removeByRoleId(id);
        roleMenuMapper.removeByRoleId(id);
        return count;
    }

    @Override
    public Role get(Long id) {
        Role role = roleMapper.get(id);
        if (role == null) {
            role = new Role();
        }
        Long roleId = role.getRoleId();
        // 菜单
        List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(roleId);
        role.setMenuIds(menuIds);
        return role;
    }

    @Override
    public int update(Role role) {
        role.setGmtModified(new Date());
        int r = roleMapper.updateById(role);
        List<Long> menuIds = role.getMenuIds();
        Long roleId = role.getRoleId();
        roleMenuMapper.removeByRoleId(roleId);
        List<RoleMenu> rms = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu rmDo = new RoleMenu();
            rmDo.setRoleId(roleId);
            rmDo.setMenuId(menuId);
            rms.add(rmDo);
        }
        if (rms.size() > 0) {
            roleMenuMapper.batchSave(rms);
        }
        return r;
    }

    @Override
    public int batchremove(Long[] ids) {
        int r = roleMapper.batchRemove(ids);
        return r;
    }

    @Override
    public int count(Map<String, Object> map) {
        return roleMapper.count(map);
    }

    @Override
    public List<String> selectRoleBuType(Long userId) {
        return this.baseMapper.selectRoleBuType(userId);
    }
}
