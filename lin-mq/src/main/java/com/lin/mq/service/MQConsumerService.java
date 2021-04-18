package com.lin.mq.service;

import com.lin.mq.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = RabbitMQConfig.LIN_MQ_QUEUE, autoDelete = "false"),
        exchange = @Exchange(value = RabbitMQConfig.LIN_MQ_EXCHANGE, type = ExchangeTypes.TOPIC), key = RabbitMQConfig.LIN_MQ_KEY),
        containerFactory = "pointTaskContainerFactory")
public class MQConsumerService {

    @RabbitHandler
    public void process(String msg)  {
        log.info("获取mq消息：{}", msg);
    }
}
