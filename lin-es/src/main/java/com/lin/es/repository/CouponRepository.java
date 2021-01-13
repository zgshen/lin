package com.lin.es.repository;

import com.lin.es.model.Coupon;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CouponRepository extends ElasticsearchRepository<Coupon, Long> {
}
