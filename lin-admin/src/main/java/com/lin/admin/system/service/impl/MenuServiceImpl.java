package com.lin.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.common.model.Tree;
import com.lin.admin.common.utils.BuildTree;
import com.lin.admin.system.entity.Menu;
import com.lin.admin.system.mapper.MenuMapper;
import com.lin.admin.system.mapper.RoleMenuMapper;
import com.lin.admin.system.service.IMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    /**
     * @param
     * @return 树形菜单
     */
    @Cacheable
    @Override
    public Tree<Menu> getSysMenuTree(Long id) {
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = menuMapper.listMenuByUserId(id);
        for (Menu sysMenuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> attributes = new HashMap<>(16);
            attributes.put("url", sysMenuDO.getUrl());
            attributes.put("icon", sysMenuDO.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public List<Menu> list(Map<String, Object> params) {
        List<Menu> menus = menuMapper.list(params);
        return menus;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int remove(Long id) {
        int result = menuMapper.remove(id);
        return result;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Override
    public int update(Menu menu) {
        int r = menuMapper.update(menu);
        return r;
    }

    @Override
    public Menu get(Long id) {
        Menu menuDO = menuMapper.get(id);
        return menuDO;
    }

    @Override
    public Tree<Menu> getTree() {
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = menuMapper.list(new HashMap<>(16));
        for (Menu sysMenuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Tree<Menu> getTree(Long id) {
        // 根据roleId查询权限
        List<Menu> menus = menuMapper.list(new HashMap<String, Object>(16));
        List<Long> menuIds = roleMenuMapper.listMenuIdByRoleId(id);
        List<Long> temp = menuIds;
        for (Menu menu : menus) {
            if (temp.contains(menu.getParentId())) {
                menuIds.remove(menu.getParentId());
            }
        }
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        List<Menu> menuDOs = menuMapper.list(new HashMap<String, Object>(16));
        for (Menu sysMenuDO : menuDOs) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(sysMenuDO.getMenuId().toString());
            tree.setParentId(sysMenuDO.getParentId().toString());
            tree.setText(sysMenuDO.getName());
            Map<String, Object> state = new HashMap<>(16);
            Long menuId = sysMenuDO.getMenuId();
            if (menuIds.contains(menuId)) {
                state.put("selected", true);
            } else {
                state.put("selected", false);
            }
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public Set<String> listPerms(Long userId) {
        List<String> perms = menuMapper.listUserPerms(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            if (StringUtils.isNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    @Override
    public void removeByParentId(Long parentId) {
        menuMapper.deleteByParentId(parentId);
    }

    @Override
    public void removeFatherAndSonMenuByParentId(Long parentId) {
        menuMapper.remove(parentId);
        menuMapper.deleteByParentId(parentId);
    }

    @Override
    public List<Tree<Menu>> listMenuTree(Long id) {
        // 父节点 ID
        Long parentId = 0L;
        HashMap<Long, Long> parentIdMap = new HashMap<>();
        List<Tree<Menu>> trees = new ArrayList<>();
        List<Tree<Menu>> newTrees = new ArrayList<>();
        List<Menu> menuDOs = menuMapper.listMenuByUserId(id);
        for (Menu sysMenuDO : menuDOs) {
            setTree(trees, sysMenuDO);

            // 查询菜单的父节点
            Long menuParentId = sysMenuDO.getParentId();
            if (!parentId.equals(menuParentId)) {
                // 去重父节点 ID
                parentIdMap.put(menuParentId, menuParentId);
            }
        }

        // 通过子节点找到父节点 ID
        for (Long value : parentIdMap.values()) {
            Menu menuDO = menuMapper.get(value);
            if (menuDO == null) {
                continue;
            }
            setTree(trees, menuDO);
        }

        // 去重
        Set set = new HashSet();
        for (Tree tree : trees) {
            if (set.add(tree)) {
                newTrees.add(tree);
            }
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        List<Tree<Menu>> list = BuildTree.buildList(newTrees, "0");
        return list;
    }

    /**
     * 添加菜单树节点
     *
     * @param trees
     * @param sysMenuDO
     */
    private void setTree(List<Tree<Menu>> trees, Menu sysMenuDO) {
        Tree<Menu> tree = new Tree<>();
        tree.setId(sysMenuDO.getMenuId().toString());
        tree.setParentId(sysMenuDO.getParentId().toString());
        tree.setText(sysMenuDO.getName());
        Map<String, Object> attributes = new HashMap<>(16);
        attributes.put("url", sysMenuDO.getUrl());
        attributes.put("icon", sysMenuDO.getIcon());
        attributes.put("vueUrl", sysMenuDO.getVueUrl());
        attributes.put("vueIcon", sysMenuDO.getVueIcon());
        tree.setAttributes(attributes);
        trees.add(tree);
    }

}
