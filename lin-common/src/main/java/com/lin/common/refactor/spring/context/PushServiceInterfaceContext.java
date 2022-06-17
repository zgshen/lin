package com.lin.common.refactor.spring.context;

import com.lin.common.refactor.spring.PushChannel;
import com.lin.common.refactor.spring.PushService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Spring接口多个实现可用List列表注入
 */
@Component
public class PushServiceInterfaceContext {

    private final List<PushService> serviceList;

    // 提示 field injection is not recommended，所以用构造注入
    public PushServiceInterfaceContext(List<PushService> serviceList) {
        this.serviceList = serviceList;
    }

    public PushService getService(String type) {
        Optional<PushService> pushService = serviceList.stream()
                .filter(service -> service.type() == PushChannel.valueOf(type))
                .findAny();
        return pushService.orElseThrow(() -> new NoSuchElementException());
    }
}
