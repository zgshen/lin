package com.lin.admin.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.system.entity.Dept;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * 部门 mapper
 */
public interface DeptMapper extends BaseMapper<Dept> {
    Dept get(Long deptId);

    @Select(" SELECT DEPT_ID FROM SYS_DEPT WHERE PARENT_ID=#{parentId} ")
    List<Long> getByParentId(Long parentId);

    List<Dept> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(Dept dept);

    int update(Dept dept);

    int remove(Long deptId);

    int batchRemove(Long[] deptIds);

    Long[] listParentDept();

    int getDeptUserNumber(Long deptId);
}