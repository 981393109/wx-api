package com.github.niefy.modules.wx.controller;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.WxUser;
import com.github.niefy.modules.wx.form.WxUserTaggingForm;
import com.github.niefy.modules.wx.service.WxUserService;
import com.github.niefy.modules.wx.service.WxUserTagsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 粉丝标签
 */
@RestController
@RequestMapping("/wxUserTags")
@RequiredArgsConstructor
@Api(tags = "粉丝标签相关接口")
public class WxUserTagsController {
    @Autowired
    WxUserTagsService wxUserTagsService;
    @Autowired
    WxUserService wxUserService;
    private final WxMpService wxMpService;

    @ApiOperation("得到粉丝标签")
    @GetMapping("/userTags")
    public R userTags(@CookieValue String appid,@CookieValue String openid){
        if(openid==null){
            return R.error("none_openid");
        }
        this.wxMpService.switchoverTo(appid);
        WxUser wxUser = wxUserService.getById(openid);
        if(wxUser==null){
            wxUser=wxUserService.refreshUserInfo(openid,appid);
            if(wxUser==null) {
                return R.error("not_subscribed");
            }
        }
        return R.ok().put(wxUser.getTagidList());
    }

    @ApiOperation("设置标签")
    @PostMapping("/tagging")
    public R tagging(@CookieValue String appid,@CookieValue String openid , @RequestBody WxUserTaggingForm form) {
        this.wxMpService.switchoverTo(appid);
        try {
            wxUserTagsService.tagging(form.getTagid(),openid);
        }catch (WxErrorException e){
            WxError error = e.getError();
            if(50005==error.getErrorCode()){//未关注公众号
                return R.error("not_subscribed");
            }else {
                return R.error(error.getErrorMsg());
            }
        }
        return R.ok();
    }

    @ApiOperation("取消标签")
    @PostMapping("/untagging")
    public R untagging(@CookieValue String appid,@CookieValue String openid , @RequestBody WxUserTaggingForm form) throws WxErrorException {
        this.wxMpService.switchoverTo(appid);
        wxUserTagsService.untagging(form.getTagid(),openid);
        return R.ok();
    }
}
