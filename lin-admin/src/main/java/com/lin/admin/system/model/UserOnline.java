package com.lin.admin.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "用户在线对象")
public class UserOnline {

    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "用户 ID", name = "userId")
    private String userId;

    @ApiModelProperty(value = "用户名", name = "username")
    private String username;

    /**
     * 用户主机地址
     */
    @ApiModelProperty(value = "用户主机地址", name = "host")
    private String host;

    /**
     * 用户登录时系统 IP
     */
    @ApiModelProperty(value = "用户登录时系统 IP", name = "systemHost")
    private String systemHost;

    /**
     * 用户浏览器类型
     */
    @ApiModelProperty(value = "用户浏览器类型", name = "userAgent")
    private String userAgent;

    /**
     * 在线状态
     */
    @ApiModelProperty(value = "在线状态", name = "status")
    private String status = "on_line";

    /**
     * session创建时间
     */
    @ApiModelProperty(value = "session创建时间", name = "startTimestamp")
    private Date startTimestamp;
    /**
     * session最后访问时间
     */
    @ApiModelProperty(value = "session最后访问时间", name = "lastAccessTime")
    private Date lastAccessTime;

    /**
     * 超时时间
     */
    @ApiModelProperty(value = "超时时间", name = "timeout")
    private Long timeout;

    /**
     * 备份的当前用户会话
     */
    @ApiModelProperty(value = "备份的当前用户会话", name = "onlineSession")
    private String onlineSession;

}
