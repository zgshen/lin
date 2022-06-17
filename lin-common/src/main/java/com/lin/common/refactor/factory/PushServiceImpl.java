package com.lin.common.refactor.factory;

/**
 * 使用工厂模式的方法
 */
public class PushServiceImpl {
}

enum PushChannel {
    sms("sms"),
    email("email"),
    wechat("wechat");

    String channel;
    PushChannel(String channel) {
        this.channel = channel;
    }

    String getVal() {
        return channel;
    }
}

/**
 * 推送渠道有短信、邮件、微信等等......
 * 懒得新建类文件所以直接写在这里一起
 */
class SmsPushService implements PushService {
    @Override
    public boolean push(String msg) {
        System.out.println("sms:" + msg);
        return true;
    }
}

class EmailPushService implements PushService {
    @Override
    public boolean push(String msg) {
        System.out.println("email:" + msg);
        return true;
    }
}

class WeChatPushService implements PushService {
    @Override
    public boolean push(String msg) {
        System.out.println("wechat:" + msg);
        return true;
    }
}
