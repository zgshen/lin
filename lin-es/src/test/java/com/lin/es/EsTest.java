package com.lin.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lin.es.model.Coupon;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EsTest {
    private static ObjectMapper mapper = new ObjectMapper();
    public static void main12(String[] args) throws JsonProcessingException {
        List<Coupon> couponList = new ArrayList<>();
        Coupon coupon = null;
        for (int i = 0; i < 10; i++) {
            coupon = new Coupon().setId(i+1L).setCouponCode("1000000100"+i).setCreateTime(new Date());
            if (i < 3) {
                coupon.setActivityName("买一送一").setType(1);
            } else if (i < 8) {
                coupon.setActivityName("满50减5").setType(2);
            } else {
                coupon.setActivityName("满100减15").setType(3);
            }
            couponList.add(coupon);
        }
        String s = mapper.writeValueAsString(couponList);
        System.out.println(s);
    }

    public static void main(String[] args) throws UnknownHostException {
    }

}
