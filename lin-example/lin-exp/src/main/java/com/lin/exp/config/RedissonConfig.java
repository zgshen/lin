package com.lin.exp.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${redisson.host.config}")
    private String address;

    @Bean
    public RedissonClient config() {
        Config config = new Config();
        // 传输模式既可以设置为EPOLL，也可以设置为NIO等
        config.setTransportMode(TransportMode.NIO);
        // 设置服务节点部署模式: 集群、单一节点/主从模式/哨兵模式
        config.useSingleServer().setAddress(address).setKeepAlive(true);

        return Redisson.create(config);
    }
}
