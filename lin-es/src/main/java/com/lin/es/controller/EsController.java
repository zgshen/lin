package com.lin.es.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.es.model.Coupon;
import com.lin.es.repository.CouponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@Slf4j
public class EsController {

    @Autowired
    CouponRepository couponRepository;

    private static ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/add")
    public String add(@RequestBody List<Coupon> couponList) throws JsonProcessingException {
        log.info(mapper.writeValueAsString(couponList));
        couponRepository.saveAll(couponList);
        return "ok";
    }

    @GetMapping("/get/{id}")
    public Coupon getById(@PathVariable Long id) {
        Coupon coupon = couponRepository.findById(id).get();
        return coupon;
    }

    @PostMapping("/getAll")
    public List getAll() {
        Iterator<Coupon> iterator = couponRepository.findAll().iterator();
        List<Coupon> couponList = new ArrayList<>();
        iterator.forEachRemaining(coupon -> {couponList.add(coupon);});
        return couponList;
    }

    @GetMapping("/delete/{id}")
    public String deleteById(Long id) {
        couponRepository.deleteById(id);
        return "ok";
    }
}
