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

import static com.lin.redis.message.MsgConstant.*;

@Slf4j
@Service
public class PushPopService {

    @Autowired
    private RedisTemplate redisTemplate;

    //左进
    public Long push(String... params) {
        //Long aLong = redisTemplate.opsForList().leftPushAll(LIST_PUSH_POP_MSG, params);
        //Long aLong = redisTemplate.opsForList().leftPushAll(LIST_PUSH_POP_MSG, "a1", "b2", 99, 88);
        Long aLong = redisTemplate.opsForList().leftPush(LIST_PUSH_POP_MSG, 123);
        //redisTemplate.opsForList().r
        return aLong;
    }   

    //右出，轮询检测
    public String pop() {
        String str = redisTemplate.opsForList().rightPop(LIST_PUSH_POP_MSG).toString();
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

    public String rightPopLeftPush() {
        String str;
        try {
            str = redisTemplate.opsForList().rightPopAndLeftPush(LIST_PUSH_POP_MSG, LIST_PUSH_POP_BACKUP_MSG).toString();
            // 其他业务，处理失败了还能在 LIST_PUSH_POP_BACKUP_MSG 队列找到备份
        } catch (Exception e) {
            log.error("业务异常：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        // 先进先出业务完毕出栈，让异常的消息留在队列里
        redisTemplate.opsForList().leftPop(LIST_PUSH_POP_BACKUP_MSG);
        return str;
    }
    
}
