package com.lin.common.refactor.spring;

import com.lin.common.refactor.spring.context.PushServiceMapContext;
import com.lin.common.refactor.spring.context.PushServiceInterfaceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BizController {

    @Resource(name = "smsPushService")
    PushService smsPushService;

    @Resource(name = "emailPushService")
    PushService emailPushService;

    @Resource(name = "wechatPushService")
    PushService wechatPushService;

    PushServiceInterfaceContext pushServiceInterfaceContext;
    PushServiceMapContext pushServiceMapContext;

    @Autowired
    BizController(PushServiceInterfaceContext pushServiceInterfaceContext,
                  PushServiceMapContext pushServiceMapContext) {
        this.pushServiceInterfaceContext = pushServiceInterfaceContext;
        this.pushServiceMapContext = pushServiceMapContext;
    }

    @RequestMapping("/push")
    public String bizTest(String type) {
        smsPushService.push(type);
        emailPushService.push(type);
        wechatPushService.push(type);
        return "ok";
    }

    @RequestMapping("/push1")
    public String bizTest1(String type, String msg) {
        pushServiceInterfaceContext.getService(type).push(msg);
        return "ok";
    }

    @RequestMapping("/push2")
    public String bizTest2(String type, String msg) {
        pushServiceMapContext.getService(type).push(msg);
        return "ok";
    }
}
