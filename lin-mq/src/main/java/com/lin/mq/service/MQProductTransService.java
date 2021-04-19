package com.lin.mq.service;

import com.lin.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * mq事务机制，需要配置RabbitTransactionManager
 */
@Slf4j
@Service
public class MQProductTransService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        rabbitTemplate.setChannelTransacted(true);
    }

    @Transactional
    public void sendMsg(String msg) {
        log.info("生产者发送消息：{}", msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.LIN_MQ_EXCHANGE, RabbitMQConfig.LIN_MQ_KEY.replace("*", "TEST"), msg);
        //包含字符测试异常事务
        if (msg == null || msg.contains("test")) {
            throw new RuntimeException("test fail trans.");
        }
    }

}
