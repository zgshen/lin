package com.lin.mq.controller;

import com.lin.mq.service.MQProductConfirmService;
import com.lin.mq.service.MQProductService;
import com.lin.mq.service.MQProductTransService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MQController {

    @Autowired
    MQProductTransService mqProductService;

    @PostMapping("/send")
    public String sendMsg(@RequestBody String msg) {
        log.info(msg);
        mqProductService.sendMsg(msg);
        return "ok";
    }

    /*@PostMapping("/send2")
    public String sendMsgCopy(@RequestBody String msg) {
        log.info(msg);
        mqProductService.sendMsg(msg, "notExists");
        return "ok";
    }*/

}
