package com.lin.common.refactor.spring.impl;

import com.lin.common.refactor.spring.PushChannel;
import com.lin.common.refactor.spring.PushService;
import org.springframework.stereotype.Service;

/**
 * 各类实现
 */
@Service
public class SmsPushService implements PushService {
    @Override
    public boolean push(String msg) {
        System.out.println("sms:" + msg);
        return true;
    }

    @Override
    public PushChannel type() {
        return PushChannel.sms;
    }
}
