package com.lin.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_task")
@ApiModel(value = "定时任务对象")
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value = "主键", name = "id")
    private Long id;
    /**
     * cron 表达式
     */
    @ApiModelProperty(value = "cron 表达式", name = "cronExpression", required = true)
    private String cronExpression;
    /**
     * 任务调用的方法名
     */
    @ApiModelProperty(value = "任务调用的方法名", name = "methodName", required = true)
    private String methodName;
    /**
     * 任务是否有状态
     */
    @ApiModelProperty(value = "任务是否有状态", name = "isConcurrent")
    private String isConcurrent;
    /**
     * 任务描述
     */
    @ApiModelProperty(value = "任务描述", name = "description", required = true)
    private String description;
    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者", name = "updateBy")
    private String updateBy;
    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    @ApiModelProperty(value = "任务执行时调用哪个类的方法 包名+类名", name = "beanClass", required = true)
    private String beanClass;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
    /**
     * 任务状态:0-禁用;1-开启
     */
    @ApiModelProperty(value = "任务状态:0-禁用;1-开启", name = "jobStatus")
    private String jobStatus;
    /**
     * 任务分组
     */
    @ApiModelProperty(value = "任务分组", name = "jobGroup", required = true)
    private String jobGroup;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "updateDate")
    private Date updateDate;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", name = "createBy")
    private String createBy;
    /**
     * Spring bean
     */
    @ApiModelProperty(value = "Spring bean", name = "springBean")
    private String springBean;
    /**
     * 任务名
     */
    @ApiModelProperty(value = "任务名", name = "jobName", required = true)
    private String jobName;

    @Override
    public String toString() {
        return "TaskDO{" +
                "id=" + id +
                ", cronExpression='" + cronExpression + '\'' +
                ", methodName='" + methodName + '\'' +
                ", isConcurrent='" + isConcurrent + '\'' +
                ", description='" + description + '\'' +
                ", updateBy='" + updateBy + '\'' +
                ", beanClass='" + beanClass + '\'' +
                ", createDate=" + createDate +
                ", jobStatus='" + jobStatus + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", updateDate=" + updateDate +
                ", createBy='" + createBy + '\'' +
                ", springBean='" + springBean + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}
