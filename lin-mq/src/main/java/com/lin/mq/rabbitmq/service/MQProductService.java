package com.lin.mq.rabbitmq.service;

import com.lin.mq.rabbitmq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
//@Service
public class MQProductService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMsg(String msg) {
        log.info("发送消息：{}", msg);
        amqpTemplate.convertAndSend(RabbitMQConfig.LIN_MQ_EXCHANGE, RabbitMQConfig.LIN_MQ_KEY.replace("*", "TEST"), msg);
    }

}
