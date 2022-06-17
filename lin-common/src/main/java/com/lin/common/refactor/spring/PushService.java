package com.lin.common.refactor.spring;

/**
 * 即 Strategy 接口
 */
public interface PushService {
    PushChannel type();
    boolean push(String msg);
}
