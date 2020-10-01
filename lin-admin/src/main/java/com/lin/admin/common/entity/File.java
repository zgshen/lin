package com.lin.admin.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传
 */
@Data
@TableName("sys_file")
@ApiModel(value = "文件对象")
public class File implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件 ID
     */
    @TableId
    @ApiModelProperty(value = "文件 ID", name = "id", example = "0")
    private Long id;
    /**
     * 文件类型:0-图片;1-文档;2-视频;3-音乐;99-其他
     */
    @ApiModelProperty(value = "文件类型", name = "type", example = "0")
    private Integer type;
    /**
     * URL地址
     */
    @ApiModelProperty(value = "URL地址", name = "url")
    private String url;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;

    public File() {
        super();
    }

    public File(Integer type, String url, Date createDate) {
        super();
        this.type = type;
        this.url = url;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "FileDO{" +
                "id=" + id +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
