package com.lin.jpa.controller;

import com.lin.jpa.entity.Coupon;
import com.lin.jpa.repository.CouponRepository;
import com.lin.jpa.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shardingsphere.core.strategy.keygen.SnowflakeShardingKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class ShardingController {

    @Autowired
    CouponService couponService;
    @Autowired
    SnowflakeShardingKeyGenerator keyGenerator;

    @RequestMapping("/jpaTest")
    public List<Coupon> jpaTest() {
        List<Coupon> all = couponService.findAll();
        if (CollectionUtils.isNotEmpty(all)) {
            log.info(all.toString());
        }
        return all;
    }

    @RequestMapping("/save")
    public String save() {
        couponService.save();
        return "ok";
    }

    public static void main(String[] args) {
        String code = 1 + String.valueOf(System.currentTimeMillis()) + ((int) (Math.random() * 90000 + 10000));
        //System.out.println(code);
        SnowflakeShardingKeyGenerator keyGenerator = new SnowflakeShardingKeyGenerator();
        for (int i = 0; i < 20; i++) {
            long d = (Long) keyGenerator.generateKey();
            //System.out.println(d);
            int j = i % 4 < 2 ? 1 : 2;
            //System.out.println(i%4);
            System.out.println(j);
        }
        //01 01 01 01
    }
}
