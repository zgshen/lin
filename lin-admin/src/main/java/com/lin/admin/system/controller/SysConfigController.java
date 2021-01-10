package com.lin.admin.system.controller;

import com.lin.admin.common.entity.Dict;
import com.lin.admin.common.model.Result;
import com.lin.admin.common.service.IDictService;
import com.lin.admin.common.utils.ResultUtil;
import com.lin.admin.system.config.SysConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置管理接口
 */
@Api(value = "系统配置管理接口", tags = {"系统配置管理接口"})
@RequestMapping("/sys/sysConfig")
@Controller
public class SysConfigController {

    @Autowired
    private SysConfig sysConfig;

    @Autowired
    private IDictService dictService;

    @ApiOperation(value = "查看系统信息接口", produces = "application/json")
    @GetMapping("getSysInfo")
    @ResponseBody
    Result getSysInfo() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("type", "system_info");
        List<Dict> dictList = dictService.list(map);

        if (dictList != null && dictList.size() > 0) {
            // 字典中查询到系统配置信息,使用字典配置的信息,无才使用配置文件信息
            HashMap<String, Object> data = new HashMap<>(3);
            String name = null;
            String logoUrl = null;
            String version = null;
            for (Dict dictDO : dictList) {
                String dictName = dictDO.getName();
                if ("name".equals(dictName)) {
                    name = dictDO.getValue();
                } else if ("logo-url".equals(dictName)) {
                    logoUrl = dictDO.getValue();
                } else if ("version".equals(dictName)) {
                    version = dictDO.getValue();
                }
            }
            data.put("name", name);
            data.put("logoUrl", logoUrl);
            data.put("version", version);
            return ResultUtil.renderSuccess(data);
        }
        return ResultUtil.renderSuccess(sysConfig);
    }
}
