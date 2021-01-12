package com.lin.jpa.service;

import com.lin.jpa.entity.Coupon;
import com.lin.jpa.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {
    @Autowired
    CouponRepository couponRepository;

    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    public boolean save() {
        for (int i = 0; i < 4; i++) {
            int j = i % 4 < 2 ? 1 : 2;
            Coupon coupon = new Coupon();
            //coupon.setId((Long) keyGenerator.generateKey());
            String code = j + String.valueOf(System.currentTimeMillis()) + ((int) (Math.random() * 90000 + 10000));
            coupon.setCouponCode(code);
            coupon.setType(j);
            coupon.setCreateTime(new Date());
            couponRepository.save(coupon);
        }
        return true;
    }
}
