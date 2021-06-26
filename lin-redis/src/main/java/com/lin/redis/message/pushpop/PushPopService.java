package com.lin.redis.message.pushpop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.lin.redis.message.MsgConstant.LIST_PUSH_POP_MSG;
import static com.lin.redis.message.MsgConstant.PUB_SUB_TIME_OUT;

@Slf4j
@Service
public class PushPopService {

    @Autowired
    private RedisTemplate redisTemplate;

    //左进
    public Long push(String... params) {
        Long aLong = redisTemplate.opsForList().leftPushAll(LIST_PUSH_POP_MSG, params);
        return aLong;
    }

    //右出，轮询检测
    public String pop() {
        String str = (String) redisTemplate.opsForList().rightPop(LIST_PUSH_POP_MSG);
        return str;
    }

    //阻塞读取，有获取，没有堵塞
    public void blockingConsume() {
        List<Object> obj = redisTemplate.executePipelined(new RedisCallback<Object>() {
            //   @Nullable
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                //队列没有元素会阻塞操作，直到队列获取新的元素或超时
                //return connection.bRPop(PUB_SUB_TIME_OUT, LIST_PUSH_POP_MSG.getBytes());
                return connection.bLPop(PUB_SUB_TIME_OUT, LIST_PUSH_POP_MSG.getBytes());
            }
        }, new StringRedisSerializer());
        for (Object str : obj) {
            log.info("blockingConsume : {}", str);
        }
    }
}
