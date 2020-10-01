package com.lin.admin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.common.entity.Log;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 */
public interface LogMapper extends BaseMapper<Log> {

	Log get(Long id);
	
	List<Log> list(Map<String, Object> map);

	int count(Map<String, Object> map);
	
	int save(Log log);
	
	int update(Log log);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
