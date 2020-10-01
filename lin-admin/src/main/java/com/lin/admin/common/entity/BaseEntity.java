package com.lin.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

/**
 * 公共实体
 */
public class BaseEntity {

    /**
     * 主键
     */
    @TableId
    Long id;

    /**
     * 创建人Id
     */
    private String createId;

    /**
     * 创建人名称
     */
    private String createMan;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人Id
     */
    private String updateId;

    /**
     * 更新人名称
     */
    private String updateMan;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}