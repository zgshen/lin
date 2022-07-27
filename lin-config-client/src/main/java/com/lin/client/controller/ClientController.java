package com.lin.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
@RefreshScope
@RequiredArgsConstructor
public class ClientController {
    @Value("${config.name}")
    private String configName;
    @Value("${config.active}")
    private String configActive;
    @Value("${config.version}")
    private String configVersion;

    private final JdbcTemplate jdbcTemplate;

    @RequestMapping("/info")
    public String info() {
        log.info("config name:{}, active:{}, version:{}", configName, configActive, configVersion);
        Map<String, String > res = new HashMap<>();
        res.put("name", configName);
        res.put("active", configActive);
        res.put("version", configVersion);
        return res.toString();
    }

    @RequestMapping("/getData")
    public String getData() {
        Map<String, Object> map = jdbcTemplate.queryForMap("select * from sys_user where id=1");
        return Optional.of(map).orElse(new HashMap<>()).toString();
    }
}
