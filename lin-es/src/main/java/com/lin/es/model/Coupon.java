package com.lin.es.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "coupon", type = "java")
@Accessors(chain = true)
public class Coupon {
    private Long id;
    private String couponCode;
    private String activityName;
    private Integer type;
    private Date createTime;

}
