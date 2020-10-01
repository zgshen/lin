package com.lin.admin.common.service;

import com.lin.admin.common.entity.File;

import java.util.List;
import java.util.Map;

/**
 * 文件上传
 */
public interface IFileService {

    File get(Long id);

    List<File> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(File sysFile);

    int update(File sysFile);

    int remove(Long id);

    int batchRemove(Long[] ids);

    /**
     * 判断一个文件是否存在
     *
     * @param url FileDO中存的路径
     * @return
     */
    Boolean isExist(String url);
}
