package com.lin.redis.controller;

import com.lin.redis.message.MsgConstant;
import com.lin.redis.message.pubsub.MessgePublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final static String LIST_PUSH_POP_MSG = "msg:list-push-pop";

    @Autowired
    MessgePublish messgePublish;

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


}
