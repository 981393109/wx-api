package com.github.niefy.modules.wx.manage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.niefy.modules.wx.form.WxMsgReplyForm;
import com.github.niefy.modules.wx.service.MsgReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.niefy.modules.wx.entity.WxMsg;
import com.github.niefy.modules.wx.service.WxMsgService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;



/**
 * 微信消息
 *
 * @author niefy
 * @date 2020-05-14 17:28:34
 */
@RestController
@RequestMapping("/manage/wxMsg")
@Api(tags = "微信消息相关接口")
public class WxMsgManageController {
    @Autowired
    private WxMsgService wxMsgService;
    @Autowired
    private MsgReplyService msgReplyService;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("获取消息列表")
//    @RequiresPermissions("wx:wxmsg:list")
    public R list(@RequestParam String appid){
        Map<String,Object> params = new HashMap<>();
        params.put("msgTypes","");
        params.put("startTime","");
        params.put("openid","");
        params.put("appid",appid);
        PageUtils page = wxMsgService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @ApiOperation("获取消息信息")
//    @RequiresPermissions("wx:wxmsg:info")
    public R info(@CookieValue String appid,@PathVariable("id") Long id){
		WxMsg wxMsg = wxMsgService.getById(id);

        return R.ok().put("wxMsg", wxMsg);
    }

    /**
     * 回复
     */
    @PostMapping("/reply")
    @ApiOperation("回复消息信息")
//    @RequiresPermissions("wx:wxmsg:save")
    public R reply( String appid,@RequestBody WxMsgReplyForm form){

        msgReplyService.reply(form.getOpenid(),form.getReplyType(),form.getReplyContent());
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除消息信息")
//    @RequiresPermissions("wx:wxmsg:delete")
    public R delete(@CookieValue String appid,@RequestBody Long[] ids){
		wxMsgService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
