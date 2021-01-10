package com.lin.admin.system.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统配置
 */
@Data
@Component
public class SysConfig {

    /**
     * 系统名称
     */
    @Value("${prop.system.name}")
    private String name;

    /**
     * 系统 LOGO
     */
    @Value("${prop.system.logo-url}")
    private String logoUrl;

    /**
     * 系统版本号
     */
    @Value("${prop.system.version}")
    private String version;

}