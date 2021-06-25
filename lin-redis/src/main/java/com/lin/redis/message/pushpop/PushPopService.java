package com.lin.redis.message.pushpop;

import com.lin.redis.message.MsgConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushPopService {

    @Autowired
    private RedisTemplate redisTemplate;

    //左进
    public Long push(String... params) {
        Long aLong = redisTemplate.opsForList().leftPushAll(MsgConstant.LIST_PUSH_POP_MSG, params);
        return aLong;
    }

    //右出
    public String pop() {
        String str = (String) redisTemplate.opsForList().rightPop(MsgConstant.LIST_PUSH_POP_MSG);
        return str;
    }

}
