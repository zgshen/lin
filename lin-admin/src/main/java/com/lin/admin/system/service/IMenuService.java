package com.lin.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.common.model.Tree;
import com.lin.admin.system.entity.Menu;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface IMenuService extends IService<Menu> {
    Tree<Menu> getSysMenuTree(Long id);

    List<Tree<Menu>> listMenuTree(Long id);

    Tree<Menu> getTree();

    Tree<Menu> getTree(Long id);

    List<Menu> list(Map<String, Object> params);

    int remove(Long id);

    int update(Menu menu);

    Menu get(Long id);

    Set<String> listPerms(Long userId);

    /**
     * 通过父节点 ID 删除
     * @param parentId
     */
    void removeByParentId(Long parentId);

    /**
     * 删除父子节点
     * @param parentId
     */
    void removeFatherAndSonMenuByParentId(Long parentId);
}
