package com.github.niefy.modules.jsoup.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class SysBaseEntity extends BaseParams {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键 id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(hidden = true ,value = "创建人")
    private String createBy;

    @ApiModelProperty(hidden = true ,value = "创建时间")
    private LocalDateTime createAt;

    @ApiModelProperty(hidden = true ,value = "更新人")
    private String updateBy;

    @ApiModelProperty(hidden = true ,value = "更新时间")
    private LocalDateTime updateAt;

    @ApiModelProperty(hidden = true ,value = "删除标识（1是，0否）")
    private Byte delFlag;
    //@Size(min = 0, max = 250, message = "事故名称最多255个字符")
    @ApiModelProperty(value = "备注")
    private String remark;

}
