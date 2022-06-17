package com.lin.common.refactor.spring.context;

import com.lin.common.refactor.spring.PushService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Spring接口多个实现可用哈希表注入
 */
@Component
public class PushServiceMapContext {
    private final Map<String, PushService> serviceMap = new ConcurrentHashMap<>();

    public PushServiceMapContext(Map<String, PushService> serviceMap) {
        this.serviceMap.clear();
        this.serviceMap.putAll(serviceMap);
    }

    public boolean apply(String type, String msg) {
        PushService service = serviceMap.get(type + "PushService");
        return service.push(msg);
    }
}
