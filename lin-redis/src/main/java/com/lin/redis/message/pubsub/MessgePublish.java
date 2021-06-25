package com.lin.redis.message.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessgePublish {

    @Autowired
    StringRedisTemplate redisTemplate;

    public void publish(String channel, String msg) {
        redisTemplate.convertAndSend(channel, msg);
    }

}
