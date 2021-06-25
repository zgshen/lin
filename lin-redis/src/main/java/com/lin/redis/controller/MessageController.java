package com.lin.redis.controller;

import com.lin.redis.message.MsgConstant;
import com.lin.redis.message.pubsub.MessgePublish;
import com.lin.redis.message.pushpop.PushPopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MessageController {

    private final static String LIST_PUSH_POP_MSG = "msg:list-push-pop";

    @Autowired
    MessgePublish messgePublish;

    @Autowired
    PushPopService pushPopService;

    /**
     * 发布订阅模式
     * @param msg
     * @return
     */
    @RequestMapping("/pubSub")
    public String pubSub(String msg) {
        messgePublish.publish(MsgConstant.PUB_SUB_MSG, msg);
        messgePublish.publish(MsgConstant.PUB_SUB_MSG1, msg);
        return "ok";
    }

    /**
     * 通过 lpush 和 rpop 或者 rpush 和 lpop 实现消息队列
     * @return
     */
    @RequestMapping("/listPush")
    public String listPush() {
        Long aLong = pushPopService.push("a", "b", "c", "m", "n");
        log.info("success num {}", aLong);
        return "ok";
    }

    @RequestMapping("/listPop")
    public String listPop() {
        String pop = pushPopService.pop();
        return pop;
    }

}
