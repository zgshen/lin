package com.lin.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * topic exchange
     */
    public final static String LIN_MQ_EXCHANGE = "LIN_MQ_EXCHANGE";
    /**
     * queue name
     */
    public final static String LIN_MQ_QUEUE = "LIN_MQ_QUEUE";
    /**
     * route key
     */
    public final static String LIN_MQ_KEY = "LIN_MQ_KEY.*";

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(LIN_MQ_EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(LIN_MQ_QUEUE);
    }

    @Bean
    Binding bindingExchangeMessages(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with(LIN_MQ_KEY);
    }

    /**
     * mq事务配置
     * MQProductTransService 对应用
     */
    @Bean
    public RabbitTransactionManager rabbitTransactionManager(CachingConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    /**
     * 以上为生产者配置
     * 以下为消费者配置
     * 系统第一次启动需要注释掉全部消费者注入，先启动生产者一次后再者才能一起启动
     */

    @Value("${default.prefetch.count}")
    private Integer DEFAULT_PREFETCH_COUNT;

    @Value("${default.concurrent}")
    private Integer DEFAULT_CONCURRENT;


    /**
     * 对应的 yml 配置
     * spring.rabbitmq.listener.simple.acknowledge-mode=manual #设置消费端手动 ack
     * spring.rabbitmq.listener.simple.concurrency=1 #消费者最小数量
     * spring.rabbitmq.listener.simple.max-concurrency=10 #消费之最大数量
     * spring.rabbitmq.listener.simple.prefetch=2 #在单个请求中处理的消息个数，他应该大于等于事务数量(unack的最大数量)
     * @return
     */
    @Bean("pointTaskContainerFactory")
    public SimpleRabbitListenerContainerFactory pointTaskContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(DEFAULT_PREFETCH_COUNT);
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}
