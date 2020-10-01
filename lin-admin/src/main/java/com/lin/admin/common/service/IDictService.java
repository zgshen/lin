package com.lin.admin.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.admin.common.entity.Dict;
import com.lin.admin.system.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 */
public interface IDictService extends IService<Dict> {

    Dict get(Long id);

    List<Dict> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int update(Dict dict);

    int batchRemove(Long[] ids);

    List<Dict> listType();

    String getName(String type, String value);

    /**
     * 获取爱好列表
     *
     * @param userDO
     * @return
     */
    List<Dict> getHobbyList(User userDO);

    /**
     * 获取性别列表
     *
     * @return
     */
    List<Dict> getSexList();

    /**
     * 根据type获取数据
     *
     * @param type
     * @return
     */
    List<Dict> listByType(String type);

}
