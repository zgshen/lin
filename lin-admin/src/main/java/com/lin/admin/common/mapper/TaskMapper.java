package com.lin.admin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.common.entity.Task;

import java.util.List;
import java.util.Map;

public interface TaskMapper extends BaseMapper<Task> {

    List<Task> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int update(Task task);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
