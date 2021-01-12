package com.lin.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "coupon")
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增方案由 shardingjdbc 设置的处理，配置用的雪花算法
    private Long id;
    private String couponCode;
    private Integer type;
    private Date createTime;
}
