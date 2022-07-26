package com.lin.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RefreshScope
public class ClientController {

    @Value("${config.name}")
    private String configName;

    @Value("${config.active}")
    private String configActive;
    @Value("${config.version}")
    private String configVersion;

    @RequestMapping("/info")
    public String info() {
        log.info("config name:{}, active:{}, version:{}", configName, configActive, configVersion);
        Map<String, String > res = new HashMap<>();
        res.put("name", configName);
        res.put("active", configActive);
        res.put("version", configVersion);
        return res.toString();
    }
}
