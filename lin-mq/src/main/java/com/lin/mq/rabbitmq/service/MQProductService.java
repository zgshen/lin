package com.lin.mq.rabbitmq.service;

import com.lin.mq.rabbitmq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@Slf4j
//@Service
public class MQProductService implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    /*@Autowired
    private AmqpTemplate amqpTemplate;*/
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this::confirm);
        rabbitTemplate.setReturnCallback(this::returnedMessage);
    }

    public void sendMsg(String msg) {
        log.info("发送消息：{}", msg);
        //消息默认持久化，要改变持久化属性可以设置 MessageProperties 的属性 deliveryMode
        rabbitTemplate.convertAndSend(RabbitMQConfig.LIN_MQ_EXCHANGE, RabbitMQConfig.LIN_MQ_KEY.replace("*", "TEST"), msg);
    }

    /**
     * 消息发送到队列后回调确认
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("确认ack:{}", ack);
    }

    /**
     * 发送失败返回信息
     */
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        log.info("返回信息returnedMessage:{}", message);
    }
}
