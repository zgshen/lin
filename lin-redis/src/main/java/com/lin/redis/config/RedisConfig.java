package com.lin.redis.config;

import com.lin.redis.message.MsgConstant;
import com.lin.redis.message.pubsub.MessageSubscribe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    /**
     * @param connectionFactory
     * @param adapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter adapter, MessageListenerAdapter adapter1) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //主题的监听
        container.addMessageListener(adapter, new PatternTopic(MsgConstant.PUB_SUB_MSG));
        container.addMessageListener(adapter, new PatternTopic(MsgConstant.PUB_SUB_MSG1));
        return container;
    }

    /**
     * 多个订阅
     * @param message
     * @return
     */
    @Bean
    public MessageListenerAdapter adapter(MessageSubscribe message){
        // MessgePublish 的 onMessage 监听获取订阅数据
        return new MessageListenerAdapter(message, "onMessage");
    }

    @Bean
    public MessageListenerAdapter adapter1(MessageSubscribe message){
        // MessgePublish 的 onMessage
        return new MessageListenerAdapter(message, "onMessage");
    }


}
