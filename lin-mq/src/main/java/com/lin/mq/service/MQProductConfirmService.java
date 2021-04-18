package com.lin.mq.service;

import com.lin.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生产者确认机制
 */
@Slf4j
//@Service
public class MQProductConfirmService implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private void init() {
        rabbitTemplate.convertAndSend(this);
    }

    public void sendMsg(String msg) {
        log.info("发送消息：{}", msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.LIN_MQ_EXCHANGE, RabbitMQConfig.LIN_MQ_KEY.replace("*", "TEST"), msg);
    }

    /**
     * 确认回调信息看是否成功
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData == null ? "" : correlationData.getId();
        if (b) {
            log.info("确认成功，id：{}", id);
        } else {
            log.info("确认失败，id：{}", id);
        }
    }
}
