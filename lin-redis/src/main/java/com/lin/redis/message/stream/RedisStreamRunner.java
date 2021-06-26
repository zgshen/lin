package com.lin.redis.message.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.time.Duration;

import static com.lin.redis.message.MsgConstant.*;

/**
 * 启动初始化配置，注册 listener
 */
@Slf4j
@Component
public class RedisStreamRunner implements ApplicationRunner, DisposableBean {

    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;
    private final ThreadPoolTaskExecutor executor;
    private final RedisConnectionFactory redisConnectionFactory;
    private final StringRedisTemplate stringRedisTemplate;

    public RedisStreamRunner(ThreadPoolTaskExecutor executor, RedisConnectionFactory redisConnectionFactory, StringRedisTemplate stringRedisTemplate) {
        this.executor = executor;
        this.redisConnectionFactory = redisConnectionFactory;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                        .batchSize(10)// 一次性最多拉取多少条消息
                        .executor(executor)// 执行消息轮询的执行器
                        .pollTimeout(Duration.ZERO)// 超时时间，设置为0，表示不超时（超时后会抛出异常）
                        .build();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(redisConnectionFactory, options);

        initStreamAndGroup(stringRedisTemplate.opsForStream(), STREAM_KEY, STREAM_GROUP);
        // autoAcknowledge 为 false，需要手动 ack 的
        container.receive(Consumer.from(STREAM_GROUP, STREAM_CONSUMER),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()),
                new TestStreamListener(stringRedisTemplate));

        this.container = container;
        this.container.start();
    }

    /**
     * 不存在则创建
     */
    private void initStreamAndGroup(StreamOperations<String, ?, ?> ops, String streamKey, String group) {
        String status = "OK";
        try {
            StreamInfo.XInfoGroups groups = ops.groups(streamKey);
            if (groups.stream().noneMatch(xInfoGroup -> group.equals(xInfoGroup.groupName()))) {
                status = ops.createGroup(streamKey, group);
            }
        } catch (Exception exception) {
            RecordId initialRecord = ops.add(ObjectRecord.create(streamKey, "Initial Record"));
            Assert.notNull(initialRecord, "Cannot initialize stream with key '" + streamKey + "'");
            status = ops.createGroup(streamKey, ReadOffset.from(initialRecord), group);
        } finally {
            Assert.isTrue("OK".equals(status), "Cannot create group with name '" + group + "'");
        }
    }

    @Override
    public void destroy() {
        this.container.stop();
    }

}
