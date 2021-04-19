package com.lin.mq.service;

import com.lin.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * 生产者确认机制
 */
@Slf4j
//@Service
public class MQProductConfirmService implements RabbitTemplate.ConfirmCallback , RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init() {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
    }

    public void sendMsg(String msg) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("发送消息id：{}，内容：{}", correlationData.getId(), msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.LIN_MQ_EXCHANGE, RabbitMQConfig.LIN_MQ_KEY.replace("*", "TEST"),
                msg, correlationData);
    }

    public void sendMsg(String msg, String key) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        log.info("发送消息id：{}，内容：{}", correlationData.getId(), msg);
        rabbitTemplate.convertAndSend(RabbitMQConfig.LIN_MQ_EXCHANGE, key, msg, correlationData);
    }

    /**
     * 确认回调信息看是否成功
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData == null ? "" : correlationData.getId();
        if (b) {
            log.info("生产消息确认成功，id：{}", id);
        } else {
            log.info("生产消息确认失败，id：{}", id);
        }
    }

    /**
     * 不可路由情况的处理
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        String msgId = message.getMessageProperties().getMessageId();
        log.info("消息不可达，message id：{}", msgId);
    }
}
