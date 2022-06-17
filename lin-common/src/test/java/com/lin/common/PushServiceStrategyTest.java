package com.lin.common;

import com.lin.common.refactor.spring.PushService;
import com.lin.common.refactor.spring.context.PushServiceInterfaceContext;
import com.lin.common.refactor.spring.context.PushServiceMapContext;
import com.lin.common.refactor.spring.impl.EmailPushService;
import com.lin.common.refactor.spring.impl.SmsPushService;
import com.lin.common.refactor.spring.impl.WeChatPushService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class PushServiceStrategyTest {

    SmsPushService smsPushService = new SmsPushService();
    EmailPushService emailPushService = new EmailPushService();
    WeChatPushService wechatPushService = new WeChatPushService();

    PushServiceInterfaceContext pushServiceInterfaceContext;
    PushServiceMapContext pushServiceMapContext;

    @BeforeEach
    void setUp() {
        pushServiceInterfaceContext = new PushServiceInterfaceContext(
                Arrays.asList(smsPushService, emailPushService, wechatPushService));
        pushServiceMapContext = new PushServiceMapContext(new ConcurrentHashMap<String, PushService>(){{
            put("smsPushService", smsPushService);
            put("emailPushService", emailPushService);
            put("wechatPushService", wechatPushService);
        }});
    }

    @Test
    public void test_spring_interface() {
        String type = "email", msg = "hello world.";
        System.out.println("-----interface-----");
        pushServiceInterfaceContext.getService(type).push(msg);
        System.out.println("--------map--------");
        pushServiceMapContext.getService(type).push(msg);
    }

}
