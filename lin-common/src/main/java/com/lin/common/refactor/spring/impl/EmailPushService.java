package com.lin.common.refactor.spring.impl;

import com.lin.common.refactor.spring.PushChannel;
import com.lin.common.refactor.spring.PushService;
import org.springframework.stereotype.Service;

@Service
public class EmailPushService implements PushService {
    @Override
    public boolean push(String msg) {
        System.out.println("email:" + msg);
        return true;
    }

    @Override
    public PushChannel type() {
        return PushChannel.email;
    }
}
