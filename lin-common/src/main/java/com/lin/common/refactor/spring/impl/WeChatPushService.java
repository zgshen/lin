package com.lin.common.refactor.spring.impl;

import com.lin.common.refactor.spring.PushChannel;
import com.lin.common.refactor.spring.PushService;
import org.springframework.stereotype.Service;

@Service("wechatPushService")
public class WeChatPushService implements PushService {
    @Override
    public boolean push(String msg) {
        System.out.println("wechat:" + msg);
        return true;
    }

    @Override
    public PushChannel type() {
        return PushChannel.wechat;
    }
}
