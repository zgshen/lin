package com.lin.redis.message.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import static com.lin.redis.message.MsgConstant.STREAM_GROUP;

@Slf4j
public class TestStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    StringRedisTemplate redisTemplate;

    public TestStreamListener(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {

        log.info("MessageId: " + message.getId());
        log.info("Stream: " + message.getStream());
        log.info("Body: " + message.getValue());
        //手动确认
        redisTemplate.opsForStream().acknowledge(STREAM_GROUP, message);
    }
}
