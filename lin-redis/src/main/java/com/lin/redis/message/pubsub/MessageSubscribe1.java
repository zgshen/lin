package com.lin.redis.message.pubsub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageSubscribe1 implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
        log.info("sub1, topic name: {}, message: {}", new String(bytes), new String(message.getBody()));
    }

}
