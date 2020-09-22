package com.github.niefy.modules.jsoup.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("lj_crawler_account")
@ApiModel(value="CrawlerAccount对象", description="")
public class CrawlerAccount extends SysBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公众号名称")
    private String name;

    @ApiModelProperty(value = "微信号")
    private String wxName;

    @ApiModelProperty(value = "是否启用")
    private Integer enabled;

    @ApiModelProperty(value = "type 1:公众号 2:网页")
    private Integer type;

}
