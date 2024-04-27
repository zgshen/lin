package com.lin.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.lin.redis.message.pubsub.MessageSubscribe;
import com.lin.redis.message.pubsub.MessageSubscribe1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static com.lin.redis.message.MsgConstant.*;

@Configuration
public class RedisConfig {

    /**
     * 序列化问题配置，解决 org.springframework.data.redis.core.ListOperations#leftPushAll(java.lang.Object, java.lang.Object[]) 在 redis 存储时带特殊前缀问题
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Object> redisTemplate = new RedisTemplate<>();
        // 设置redis连接
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // 将redisTemplate的序列化方式更改为StringRedisSerializer
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

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
        //主题的监听，adapter 和 adapter1 对应下面两个 bean 实例，有多少订阅者都在下面加上
        container.addMessageListener(adapter, new PatternTopic(PUB_SUB_MSG));//普通的订阅者
        container.addMessageListener(adapter1, new PatternTopic(PUB_SUB_MSG_FUZZY));//模糊匹配的订阅者
        return container;
    }

    /**
     * 多个订阅
     * @param message
     * @return
     */
    @Bean
    public MessageListenerAdapter adapter(MessageSubscribe message){
        // MessageSubscribe 的 onMessage 监听获取订阅数据
        return new MessageListenerAdapter(message, "onMessage");
    }

    @Bean
    public MessageListenerAdapter adapter1(MessageSubscribe1 message){
        // MessageSubscribe1 的 onMessage
        return new MessageListenerAdapter(message, "onMessage");
    }


}
