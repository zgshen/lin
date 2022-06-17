package com.lin.common.refactor.Enum;

public enum PushChannel {

    sms(PushService.smsPush()),
    email(PushService.emailPush()),
    we_chat(PushService.wechatPush());

    PushService pushService;

    private PushChannel(PushService pushService) {
        this.pushService = pushService;
    }

    public PushService getPushService() {
        return pushService;
    }

}
