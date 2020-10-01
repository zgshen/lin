package com.lin.admin.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.admin.common.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 */
public interface DictMapper extends BaseMapper<Dict> {

	Dict get(Long id);

	List<Dict> list(Map<String, Object> map);

	int count(Map<String, Object> map);

	int update(Dict dict);

	int batchRemove(Long[] ids);

	List<Dict> listType();
}
