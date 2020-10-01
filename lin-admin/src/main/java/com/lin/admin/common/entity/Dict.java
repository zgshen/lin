package com.lin.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 字典表
 */
@Data
@ApiModel(value = "字典对象")
@TableName("sys_dict")
public class Dict implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value = "字典 ID", name = "id", example = "1")
    private Long id;
    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名", name = "name")
    private String name;
    /**
     * 数据值
     */
    @ApiModelProperty(value = "数据值", name = "value")
    private String value;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型", name = "type")
    private String type;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", name = "description")
    private String description;
    /**
     * 排序（升序）
     */
    @ApiModelProperty(value = "排序（升序）", name = "description", example = "1")
    private BigDecimal sort;
    /**
     * 父级编号
     */
    @ApiModelProperty(value = "父级编号", name = "parentId", example = "0")
    private Long parentId;
    /**
     * 创建者 ID
     */
    @ApiModelProperty(value = "创建者 ID", name = "createBy", example = "1")
    private Integer createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
    /**
     * 更新者 ID
     */
    @ApiModelProperty(value = "更新者 ID", name = "updateBy", example = "1")
    private Long updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", name = "updateDate")
    private Date updateDate;
    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", name = "remarks")
    private String remarks;
    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", name = "delFlag")
    private String delFlag;

    @Override
    public String toString() {
        return "DictDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", sort=" + sort +
                ", parentId=" + parentId +
                ", createBy=" + createBy +
                ", createDate=" + createDate +
                ", updateBy=" + updateBy +
                ", updateDate=" + updateDate +
                ", remarks='" + remarks + '\'' +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }
}
