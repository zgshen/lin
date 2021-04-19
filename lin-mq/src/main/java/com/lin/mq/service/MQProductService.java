package com.lin.mq.service;

import com.lin.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
