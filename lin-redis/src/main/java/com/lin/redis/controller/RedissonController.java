package com.lin.redis.controller;

import com.lin.redis.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class RedissonController {

    @Autowired
    RedissonService redissonService;

    @RequestMapping("/cas")
    public String cas() {
        RAtomicLong atomicLong = redissonService.getRAtomicLong("cas.key");
        boolean a = atomicLong.compareAndSet(1, 10);
        log.info("a result is {}", a);
        log.info("incr result is {}", atomicLong.incrementAndGet());
        boolean b = atomicLong.compareAndSet(1, 10);
        log.info("b result is {}", b);
        atomicLong.getAndDelete();
        return "ok";
    }

    @RequestMapping("/lock")
    public String lock() {
        String key = "lock";
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                RLock lock = redissonService.getRLock(key);
                //10s 过期，多线程一个一个排队
                lock.lock(10, TimeUnit.SECONDS);
                log.info("Thread [{}] lock [{}] success", Thread.currentThread().getName(), key);
            }).start();
        }
        return "ok";
    }

    @RequestMapping("/tryLock")
    public String tryLock(String key) {
        RLock lock = redissonService.getRLock(key);
        try {
            //5s 尝试获取 锁时间，获得后 10s 过期
            boolean bs = lock.tryLock(5, 10, TimeUnit.SECONDS);
            if (bs) {
                // 业务代码
                log.info("do something, key:{}: " + key);

                lock.unlock();
            } else {
                Thread.sleep(300);
            }
        } catch (Exception e) {
            log.error("exp:{}", e);
            lock.unlock();
        }
        return "ok";
    }
}
