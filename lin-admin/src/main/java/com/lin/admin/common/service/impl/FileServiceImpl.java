package com.lin.admin.common.service.impl;

import com.lin.admin.common.config.AdminConfig;
import com.lin.admin.common.entity.File;
import com.lin.admin.common.mapper.FileMapper;
import com.lin.admin.common.service.IFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;


@Service
public class FileServiceImpl implements IFileService {

    @Autowired
    private FileMapper sysFileMapper;
    @Autowired
    private AdminConfig adminConfig;

    @Override
    public File get(Long id) {
        return sysFileMapper.get(id);
    }

    @Override
    public List<File> list(Map<String, Object> map) {
        return sysFileMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return sysFileMapper.count(map);
    }

    @Override
    public int save(File sysFile) {
        return sysFileMapper.insert(sysFile);
    }

    @Override
    public int update(File sysFile) {
        return sysFileMapper.update(sysFile);
    }

    @Override
    public int remove(Long id) {
        return sysFileMapper.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return sysFileMapper.batchRemove(ids);
    }

    @Override
    public Boolean isExist(String url) {
        Boolean isExist = false;
        if (!StringUtils.isEmpty(url)) {
            String filePath = url.replace("/files/", "");
            filePath = adminConfig.getUploadPath() + "/files/" + filePath;
            java.io.File file = new java.io.File(filePath);
            if (file.exists()) {
                isExist = true;
            }
        }
        return isExist;
    }
}
