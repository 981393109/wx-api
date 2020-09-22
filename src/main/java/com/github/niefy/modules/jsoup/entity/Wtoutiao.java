package com.github.niefy.modules.jsoup.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 公众号头条文章
 * </p>
 *
 * @author cgs
 * @since 2020-09-10
 */
@Data
@ApiModel(value="Wtoutiao对象", description="")
public class Wtoutiao extends SysBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "公众号id")
    private String wxAccount;

    @ApiModelProperty(value = "公众号名称")
    private String wxAccountName;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "素材id")
    private String mediaId;

    @ApiModelProperty(value = "图文消息的封面图片素材url")
    private String thumbUrl;

    @ApiModelProperty(value = "图文消息的封面图片素材id（必须是永久mediaID）")
    private String thumbMediaId;

    @ApiModelProperty(value = "是否显示封面，0为false，即不显示，1为true，即显示")
    private Integer showCoverPic;

    @ApiModelProperty(value = "作者")
    private String author;

    @ApiModelProperty(value = "图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空")
    private String digest;

    @ApiModelProperty(value = "图文消息的原文地址，即点击“阅读原文”后的URL")
    private String content;

    @ApiModelProperty(value = "图文消息的原文地址，即点击“阅读原文”后的URL")
    private String contentSourceUrl;

    @ApiModelProperty(value = "图文页的url")
    private String url;

    @ApiModelProperty(value = "图文页的阅读次数/点击量")
    private Integer intPageReadCount;

    @ApiModelProperty(value = "分享次数")
    private Integer shareCount;


}
