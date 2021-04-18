package com.lin.mq.controller;

import com.lin.mq.service.MQProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MQController {

    @Autowired
    MQProductService mqProductService;

    @PostMapping("/send")
    public String sendMsg(@RequestBody String msg) {
        log.info(msg);
        mqProductService.sendMsg(msg);
        return "ok";
    }

}
