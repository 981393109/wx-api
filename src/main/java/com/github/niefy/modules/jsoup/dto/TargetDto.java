package com.github.niefy.modules.jsoup.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
public class TargetDto {

    @ApiModelProperty(value = "目标网址url")
    private String targetUrl;

    @ApiModelProperty(value = "a签中href包含字符（便于筛选）")
    private String relHrefContains;

    @ApiModelProperty(value = "文章标题的class标签内容")
    private String articleTitle;

    @ApiModelProperty(value = "封面图包含字段")
    private String imgContains;

    @ApiModelProperty(value = "目标网址url")
    private String hostUrl;

}
