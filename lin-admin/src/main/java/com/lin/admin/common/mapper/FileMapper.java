package com.lin.admin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.common.entity.File;

import java.util.List;
import java.util.Map;

/**
 * 文件上传
 */
public interface FileMapper extends BaseMapper<File> {

    File get(Long id);

    List<File> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(File file);

    int update(File file);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
