package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_password_strategy")
@ApiModel(value = "密码策略实体")
public class PasswordStrategy implements Serializable {

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value = "主键", name = "id", example = "0")
    private Long id;

    /**
     * 用户密码有效期(单位:天):0-永不过期
     */
    @ApiModelProperty(value = "用户密码有效期(单位:天):0-永不过期", name = "expirationDate", required = true, example = "0")
    private Integer expirationDate;

    /**
     * 密码修改历史:0-不记住任何密码
     */
    @ApiModelProperty(value = "密码修改历史:0-不记住任何密码", name = "passwordHistory", required = true, example = "0")
    private Integer passwordHistory;

    /**
     * 密码最小长度
     */
    @ApiModelProperty(value = "密码最小长度", name = "minLength", required = true, example = "6")
    private Integer minLength;

    /**
     * 密码复杂性要求:0-无限制;1-必须混合使用字母和数字;2-必须混合使用大小写字母和数字;3-必须混合使用大小写字母、数字和特殊符号
     */
    @ApiModelProperty(value = "密码复杂性要求:0-无限制;1-必须混合使用字母和数字;2-必须混合使用大小写字母和数字;3-必须混合使用大小写字母、数字和特殊符号", name = "complexity", required = true, example = "0")
    private Integer complexity;

    /**
     * 最大无效登录尝试次数
     */
    @ApiModelProperty(value = "最大无效登录尝试次数", name = "retryTime", required = true, example = "0")
    private Integer retryTime;

    /**
     * 锁定有效期间(单位:分):0-始终(必须由管理员重置)
     */
    @ApiModelProperty(value = "锁定有效期间(单位:分):0-始终(必须由管理员重置)", name = "lockingTime", required = true, example = "0")
    private Integer lockingTime;

    /**
     * 创建人 ID
     */
    @ApiModelProperty(value = "创建人 ID", name = "userIdCreate", example = "1")
    private Integer userIdCreate;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间: yyyy-MM-dd hh:mm:ss", name = "gmtCreate")
    private Date gmtCreate;

    /**
     * 修改人 ID
     */
    @ApiModelProperty(value = "修改人 ID", name = "userIdModified", example = "1")
    private Integer userIdModified;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间: yyyy-MM-dd hh:mm:ss", name = "gmtModified")
    private Date gmtModified;

}
