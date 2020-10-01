package com.lin.admin.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.common.model.Tree;
import com.lin.admin.system.entity.Dept;
import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
public interface IDeptService extends IService<Dept> {

    Dept get(Long deptId);

    /**
     * 查找所有的子节点 ID
     *
     * @param parentId
     * @return
     */
    List<Long> getByParentId(Long parentId);

    List<Dept> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int update(Dept sysDept);

    int remove(Long deptId);

    int batchRemove(Long[] deptIds);

    Tree<Dept> getTree();

    boolean checkDeptHasUser(Long deptId);
}
