package com.lin.admin.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("sys_user")
@ApiModel(value = "用户对象")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户 ID
     */
    @TableId
    @ApiModelProperty(value = "用户 ID", name = "userId")
    private Long userId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "username")
    private String username;
    /**
     * 用户真实姓名
     */
    @ApiModelProperty(value = "用户真实姓名", name = "name")
    private String name;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "password")
    private String password;
    /**
     * 部门 ID
     */
    @ApiModelProperty(value = "部门 ID", name = "deptId", example = "0")
    private Long deptId;
    /**
     * 部门名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "部门名称", name = "deptName")
    private String deptName;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile")
    private String mobile;
    /**
     * 工号
     */
    @ApiModelProperty(value = "工号", name = "empNo")
    private String empNo;
    /**
     * 状态 0:禁用，1:正常
     */
    @ApiModelProperty(value = "状态 0:禁用，1:正常", name = "status", example = "1")
    private Integer status;
    /**
     * 创建用户 ID
     */
    @ApiModelProperty(value = "创建用户 ID", name = "userIdCreate", example = "1")
    private Long userIdCreate;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "gmtCreate")
    private Date gmtCreate;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", name = "gmtModified")
    private Date gmtModified;
    /**
     * 角色
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "角色 ID 集合", name = "roleIds")
    private List<Long> roleIds;
    /**
     * 性别:1-男;2-女
     */
    @ApiModelProperty(value = "性别", name = "sex", example = "1")
    private Integer sex;
    /**
     * 出生年月
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "出生年月 yyyy-MM-dd", name = "birth")
    private Date birth;
    /**
     * 图片 ID
     */
    @ApiModelProperty(value = "图片 ID", name = "picId", example = "0")
    private Long picId;
    /**
     * 图片 URL
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "图片 URL", name = "picUrl")
    private String picUrl;
    /**
     * 现居住地
     */
    @ApiModelProperty(value = "现居住地", name = "liveAddress")
    private String liveAddress;
    /**
     * 爱好
     */
    @ApiModelProperty(value = "爱好", name = "hobby")
    private String hobby;
    /**
     * 省份
     */
    @ApiModelProperty(value = "省份", name = "province")
    private String province;
    /**
     * 所在城市
     */
    @ApiModelProperty(value = "所在城市", name = "city")
    private String city;
    /**
     * 所在地区
     */
    @ApiModelProperty(value = "所在地区", name = "district")
    private String district;

    /**
     * 锁定状态:0-否;1-是
     */
    @ApiModelProperty(value = "锁定状态:0-否;1-是", name = "isLock", example = "0")
    private Integer isLock;

    /**
     * 锁定时间
     */
    @ApiModelProperty(value = "锁定时间", name = "lockTime")
    private Date lockTime;

    /**
     * 密码最后修改时间
     */
    @ApiModelProperty(value = "密码最后修改时间", name = "pwdUpdateTime")
    private Date pwdUpdateTime;

    /**
     * 无效密码尝试次数
     */
    @ApiModelProperty(value = "无效密码尝试次数", name = "retryTime", example = "0")
    private Integer retryTime;
}
