package com.lin.mq.service;

import com.lin.mq.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MQConsumerService {

    /*@RabbitHandler
    public void process(String msg)  {
        log.info("获取mq消息：{}", msg);
    }*/

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConfig.LIN_MQ_QUEUE, autoDelete = "false"),
            exchange = @Exchange(value = RabbitMQConfig.LIN_MQ_EXCHANGE, type = ExchangeTypes.TOPIC), key = RabbitMQConfig.LIN_MQ_KEY),
            containerFactory = "pointTaskContainerFactory")
    @RabbitHandler
    public void process(Message msg, Channel channel) throws IOException, InterruptedException {
        log.info("消费者获取mq消息：{}", msg);
        //错误会一只消费不掉死循环
        /*if (msg != null) {
            throw new RuntimeException();
        }*/

        //10s后确认
        Thread.sleep(10000L);
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
    }

}
