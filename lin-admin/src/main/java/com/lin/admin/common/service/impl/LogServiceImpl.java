package com.lin.admin.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.common.entity.Log;
import com.lin.admin.common.mapper.LogMapper;
import com.lin.admin.common.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Autowired
    LogMapper logMapper;

    @Async
    @Override
    public void saveLog(Log logDO) {
        if (logDO.getParams() == null) {
            logDO.setParams("");
        }
        logMapper.insert(logDO);
    }

    @Override
    public List<Log> queryList(Map params) {
        List<Log> logs = logMapper.list(params);
        return logs;
    }

    @Override
    public int remove(Long id) {
        int count = logMapper.remove(id);
        return count;
    }

    @Override
    public int batchRemove(Long[] ids) {
        return logMapper.batchRemove(ids);
    }
}
