package com.lin.common;

import com.lin.common.refactor.Enum.PUSH_CHANNEL;
import com.lin.common.refactor.factory.PushFactory;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class PushServiceTest {

    /**
     * 工厂
     */
    @Test
    public void test() {
        boolean sms = PushFactory.getPushService("sms").push("sms msg.");
        Assert.assertEquals(true, sms);

        boolean email = PushFactory.getPushService("email").push("email msg.");
        Assert.assertEquals(true, email);

        Assert.assertThrows(NoSuchElementException.class, () -> PushFactory.getPushService("gms"));
    }

    /**
     * 工厂，用optional
     */
    @Test
    public void test_optional() {
        boolean email = PushFactory.getPushServiceOptional("email").push("email msg.");
        Assert.assertEquals(true, email);

        Assert.assertThrows(NoSuchElementException.class, () -> PushFactory.getPushServiceOptional("gms"));
    }

    /**
     * 充血型枚举
     */
    @Test
    public void test_enum() {
        boolean push = PUSH_CHANNEL.valueOf("sms").getPushService().push("msg...");
        Assert.assertEquals(true, push);

        PUSH_CHANNEL.valueOf("sms").getPushService().push("sms msg.");
    }
}
