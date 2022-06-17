package com.lin.common.refactor.Enum;

public enum PUSH_CHANNEL {

    sms(PushService.smsPush()),
    email(PushService.emailPush()),
    we_chat(PushService.wechatPush());

    PushService pushService;

    private PUSH_CHANNEL(PushService pushService) {
        this.pushService = pushService;
    }

    public PushService getPushService() {
        return pushService;
    }

}
