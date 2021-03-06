package com.github.niefy.modules.wx.manage;

import java.util.Arrays;
import java.util.Map;

import com.github.niefy.modules.wx.entity.MsgTemplate;
import com.github.niefy.modules.wx.form.TemplateMsgBatchForm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import me.chanjar.weixin.common.error.WxErrorException;

import com.github.niefy.modules.wx.service.MsgTemplateService;
import com.github.niefy.modules.wx.service.TemplateMsgService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;


/**
 * 消息模板
 *
 * @author niefy
 * @email niefy@qq.com
 * @date 2019-11-12 18:30:15
 */
@RestController
@RequestMapping("/manage/msgTemplate")
@Api(tags = "消息模板")
public class MsgTemplateManageController {
    @Autowired
    private MsgTemplateService msgTemplateService;
    @Autowired
    private TemplateMsgService templateMsgService;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("获取消息模板列表")
//    @RequiresPermissions("wx:msgtemplate:list")
    public R list(@CookieValue String appid,@RequestParam Map<String, Object> params) {
        params.put("appid",appid);
        PageUtils page = msgTemplateService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation("获取消息模板详情")
    @GetMapping("/info/{id}")
//    @RequiresPermissions("wx:msgtemplate:info")
    public R info(@PathVariable("id") Long id) {
        MsgTemplate msgTemplate = msgTemplateService.getById(id);

        return R.ok().put("msgTemplate", msgTemplate);
    }
    /**
     * 信息
     */
    @GetMapping("/getByName")
    @ApiOperation("获取消息模板信息")
//    @RequiresPermissions("wx:msgtemplate:info")
    public R getByName( String name){
        MsgTemplate msgTemplate = msgTemplateService.selectByName(name);

        return R.ok().put("msgTemplate", msgTemplate);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存消息模板")
//    @RequiresPermissions("wx:msgtemplate:save")
    public R save(@RequestBody MsgTemplate msgTemplate) {
        msgTemplateService.save(msgTemplate);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("修改消息模板")
    @PostMapping("/update")
//    @RequiresPermissions("wx:msgtemplate:update")
    public R update(@RequestBody MsgTemplate msgTemplate) {
        msgTemplateService.updateById(msgTemplate);

        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("删除消息模板")
    @PostMapping("/delete")
//    @RequiresPermissions("wx:msgtemplate:delete")
    public R delete(@RequestBody String[] ids) {
        msgTemplateService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 同步公众号模板
     */
    @ApiOperation("同步公众号模板消息模板")
    @PostMapping("/syncWxTemplate")
//    @RequiresPermissions("wx:msgtemplate:save")
    public R syncWxTemplate(@CookieValue String appid) throws WxErrorException {
        this.wxMpService.switchoverTo(appid);
        msgTemplateService.syncWxTemplate(appid);
        return R.ok();
    }

    /**
     * 批量向用户发送模板消息
     * 通过用户筛选条件（一般使用标签筛选），将消息发送给数据库中所有符合筛选条件的用户
     */
    @PostMapping("/sendMsgBatch")
    @ApiOperation("批量向用户发送模板消息")
//    @RequiresPermissions("wx:msgtemplate:save")
    public R sendMsgBatch(@CookieValue String appid,@RequestBody TemplateMsgBatchForm form) {
        this.wxMpService.switchoverTo(appid);
        templateMsgService.sendMsgBatch(form, appid);
        return R.ok();
    }


}
