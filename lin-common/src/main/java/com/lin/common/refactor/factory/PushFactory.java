package com.lin.common.refactor.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PushFactory {

    private static Map<String, PushService> pushMap = new HashMap<>();

    static {
        pushMap.put(PushChannel.sms.getVal(), new SmsPushService());
        pushMap.put(PushChannel.email.getVal(), new EmailPushService());
        pushMap.put(PushChannel.wechat.getVal(), new WeChatPushService());
    }

    public static PushService getPushService(String type) {
        PushService pushService = pushMap.get(type);
        if (pushService == null) throw new NoSuchElementException();
        return pushService;
    }

    public static PushService getPushServiceOptional(String type) {
        return Optional.ofNullable(pushMap.get(type))
                .orElseThrow(() -> new NoSuchElementException());
    }
}
