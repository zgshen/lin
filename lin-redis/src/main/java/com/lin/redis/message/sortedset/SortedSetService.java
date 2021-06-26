package com.lin.redis.message.sortedset;

import com.lin.redis.message.MsgConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class SortedSetService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param businessId 业务 id（如订单 id 等）
     * @param expiredTime 延时时间，单位秒
     */
    public void produce(String businessId, long expiredTime) {
        redisTemplate.opsForZSet().add(MsgConstant.SORTED_SET_MSG, businessId, System.currentTimeMillis() + expiredTime * 1000);
    }

    /**
     * 简单的消费程序
     */
    public void consume() {
        while (true) {
            //(K key, double min, double max, long offset, long count)
            Set<String> set = redisTemplate.opsForZSet().rangeByScore(MsgConstant.SORTED_SET_MSG, 0, System.currentTimeMillis(), 0, 1);
            if (set == null || set.isEmpty()) continue;
            log.info(set.toString());
            String next = set.iterator().next();
            Long remove = redisTemplate.opsForZSet().remove(MsgConstant.SORTED_SET_MSG, next);
            if (remove > 0) log.info("{} remove success.", next);
        }
    }
}
