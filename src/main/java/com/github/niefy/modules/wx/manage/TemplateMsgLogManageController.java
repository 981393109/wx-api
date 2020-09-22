package com.github.niefy.modules.wx.manage;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.niefy.modules.wx.entity.TemplateMsgLog;
import com.github.niefy.modules.wx.service.TemplateMsgLogService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;


/**
 * 模版消息发送记录
 *
 * @author niefy
 * @email niefy@qq.com
 * @date 2019-11-12 18:30:15
 */
@RestController
@RequestMapping("/manage/templateMsgLog")
@Api(tags = "模版消息发送记录")
public class TemplateMsgLogManageController {
    @Autowired
    private TemplateMsgLogService templateMsgLogService;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("获取模版消息发送记录列表")
//    @RequiresPermissions("wx:templatemsglog:list")
    public R list(@CookieValue String appid,@RequestParam Map<String, Object> params) {
        params.put("appid",appid);
        PageUtils page = templateMsgLogService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation("获取模版消息发送记录信息")
    @GetMapping("/info/{logId}")
//    @RequiresPermissions("wx:templatemsglog:info")
    public R info(@CookieValue String appid,@PathVariable("logId") Integer logId) {
        TemplateMsgLog templateMsgLog = templateMsgLogService.getById(logId);

        return R.ok().put("templateMsgLog", templateMsgLog);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存模版消息发送记录")
//    @RequiresPermissions("wx:templatemsglog:save")
    public R save(@CookieValue String appid,@RequestBody TemplateMsgLog templateMsgLog) {
        templateMsgLogService.save(templateMsgLog);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改模版消息发送记录")
//    @RequiresPermissions("wx:templatemsglog:update")
    public R update(@CookieValue String appid,@RequestBody TemplateMsgLog templateMsgLog) {
        templateMsgLogService.updateById(templateMsgLog);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除模版消息发送记录")
//    @RequiresPermissions("wx:templatemsglog:delete")
    public R delete(@CookieValue String appid,@RequestBody Integer[] logIds) {
        templateMsgLogService.removeByIds(Arrays.asList(logIds));

        return R.ok();
    }

}
