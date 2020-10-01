package com.lin.admin.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.admin.common.entity.Dict;
import com.lin.admin.common.mapper.DictMapper;
import com.lin.admin.common.service.IDictService;
import com.lin.admin.system.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public Dict get(Long id) {
        return dictMapper.get(id);
    }

    @Override
    public List<Dict> list(Map<String, Object> map) {
        return dictMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return dictMapper.count(map);
    }

    @Override
    public int update(Dict dict) {
        return dictMapper.update(dict);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return dictMapper.batchRemove(ids);
    }

    @Override

    public List<Dict> listType() {
        return dictMapper.listType();
    }

    @Override
    public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = dictMapper.list(param).get(0).getName();
        return rString;
    }

    @Override
    public List<Dict> getHobbyList(User userDO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "hobby");
        List<Dict> hobbyList = dictMapper.list(param);

        if (StringUtils.isNotEmpty(userDO.getHobby())) {
            String userHobbys[] = userDO.getHobby().split(";");
            for (String userHobby : userHobbys) {
                for (Dict hobby : hobbyList) {
                    if (!Objects.equals(userHobby, hobby.getId().toString())) {
                        continue;
                    }
                    hobby.setRemarks("true");
                    break;
                }
            }
        }

        return hobbyList;
    }

    @Override
    public List<Dict> getSexList() {
        Map<String, Object> param = new HashMap<>();
        param.put("type", "sex");
        return dictMapper.list(param);
    }

    @Override
    public List<Dict> listByType(String type) {
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        return dictMapper.list(param);
    }

}
