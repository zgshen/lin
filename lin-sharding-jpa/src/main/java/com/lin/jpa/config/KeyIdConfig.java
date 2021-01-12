package com.lin.jpa.config;

import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主键 id 生成器
 */
@Configuration
public class KeyIdConfig {

    @Bean
    public SnowflakeShardingKeyGenerator couponKeyGenerator() {
        return new SnowflakeShardingKeyGenerator();
    }

    /*@Bean("orderKeyGenerator")
    public SnowflakeShardingKeyGenerator orderKeyGenerator() {
        return new SnowflakeShardingKeyGenerator();
    }*/
}
