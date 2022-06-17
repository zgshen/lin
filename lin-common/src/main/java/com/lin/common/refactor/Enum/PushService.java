package com.lin.common.refactor.Enum;

public interface PushService {
    public boolean push(String msg);

    public static PushService smsPush() {
        return new PushService() {
            public boolean push(String msg) {
                System.out.println("sms:" + msg);
                return true;
            }
        };
    }

    public static PushService emailPush() {
        return new PushService() {
            public boolean push(String msg) {
                System.out.println("email:" + msg);
                return true;
            }
        };
    }

    public static PushService wechatPush() {
        return new PushService() {
            public boolean push(String msg) {
                System.out.println("wechat:" + msg);
                return true;
            }
        };
    }
}
