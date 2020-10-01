package com.lin.admin.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.common.model.Tree;
import com.lin.admin.common.utils.BuildTree;
import com.lin.admin.system.entity.Dept;
import com.lin.admin.system.mapper.DeptMapper;
import com.lin.admin.system.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门 ServiceImpl
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Autowired
    private DeptMapper sysDeptMapper;

    @Override
    public Dept get(Long deptId){
        return sysDeptMapper.get(deptId);
    }

    @Override
    public List<Long> getByParentId(Long parentId) {
        return sysDeptMapper.getByParentId(parentId);
    }

    @Override
    public List<Dept> list(Map<String, Object> map){
        return sysDeptMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map){
        return sysDeptMapper.count(map);
    }

    @Override
    public int update(Dept sysDept){
        return sysDeptMapper.update(sysDept);
    }

    @Override
    public int remove(Long deptId){
        return sysDeptMapper.remove(deptId);
    }

    @Override
    public int batchRemove(Long[] deptIds){
        return sysDeptMapper.batchRemove(deptIds);
    }

    @Override
    public Tree<Dept> getTree() {
        List<Tree<Dept>> trees = new ArrayList<Tree<Dept>>();
        List<Dept> sysDepts = sysDeptMapper.list(new HashMap<String,Object>(16));
        for (Dept sysDept : sysDepts) {
            Tree<Dept> tree = new Tree<Dept>();
            tree.setId(sysDept.getDeptId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setText(sysDept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Dept> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public boolean checkDeptHasUser(Long deptId) {
        // TODO Auto-generated method stub
        //查询部门以及此部门的下级部门
        int result = sysDeptMapper.getDeptUserNumber(deptId);
        return result==0?true:false;
    }

}
