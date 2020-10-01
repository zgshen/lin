package com.lin.admin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.common.entity.Log;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ILogService extends IService<Log> {
    void saveLog(Log logDO);

    List<Log> queryList(Map params);

    int remove(Long id);

    int batchRemove(Long[] ids);
}
