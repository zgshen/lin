package com.lin.redis.message.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestStreamProducer {

    @Autowired
    StringRedisTemplate redisTemplate;

    //发送流信息
    public void add(String streamKey, String msg) {
        redisTemplate.opsForStream().add(Record.of(msg).withStreamKey(streamKey));
    }

}
