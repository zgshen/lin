package com.lin.mq.binlog;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BinlogMQConfig {

    public final static String BINLOG_MQ_EXCHANGE = "BINLOG_MQ_EXCHANGE";
    
    public final static String BINLOG_MQ_QUEUE = "BINLOG_MQ_QUEUE";
    
    public final static String BINLOG_MQ_KEY = "BINLOG_MQ_KEY.*";

    @Bean
    public TopicExchange binlogTopicExchange() {
        return new TopicExchange(BINLOG_MQ_EXCHANGE);
    }

    @Bean
    public Queue binlogQueue() {
        return new Queue(BINLOG_MQ_QUEUE);
    }

    @Bean
    Binding bindingBinlogExchangeMessages(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(BINLOG_MQ_KEY);
    }
}
