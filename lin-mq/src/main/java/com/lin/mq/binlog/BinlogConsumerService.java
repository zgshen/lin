package com.lin.mq.binlog;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class BinlogConsumerService {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = BinlogMQConfig.BINLOG_MQ_QUEUE, autoDelete = "false"),
            exchange = @Exchange(value = BinlogMQConfig.BINLOG_MQ_EXCHANGE, type = ExchangeTypes.TOPIC), key = BinlogMQConfig.BINLOG_MQ_KEY),
            containerFactory = "pointTaskContainerFactory")
    @RabbitHandler
    public void process(Message msg, Channel channel) throws IOException {
        log.info("===binlog消费者获取mq消息：{}", msg);
        log.info("===msg properties: " + msg.getMessageProperties().toString());
        log.info("===msg body: " + new String(msg.getBody()));
        //com.lin.mq.rabbitmq.config.RabbitMQConfig.rabbitTransactionManager 有设置事务模式需要手动ack
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(),false);
    }
}
