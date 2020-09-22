package com.github.niefy.modules.wx.manage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.github.niefy.modules.wx.entity.WxUser;
import com.github.niefy.modules.wx.service.WxUserService;
import com.github.niefy.common.utils.PageUtils;
import com.github.niefy.common.utils.R;


/**
 * 用户表
 *
 * @author niefy
 * @email niefy@qq.com
 * @date 2020-03-07 13:55:23
 */
@RestController
@RequestMapping("/manage/wxUser")
@Api(tags = "用户表相关接口")
public class WxUserManageController {
    @Autowired
    private WxUserService userService;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation("获取微信用户列表")
//    @RequiresPermissions("wx:wxuser:list")
    public R list(@RequestParam String appid,@RequestParam Map<String, Object> params) {
        params.put("appid",appid);
        PageUtils page = new PageUtils(userService.queryPage(params));

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @PostMapping("/listByIds")
    @ApiOperation("获取微信用户详情")
//    @RequiresPermissions("wx:wxuser:list")
    public R listByIds(@CookieValue String appid,@RequestBody String[] openids){
        List<WxUser> users = userService.listByIds(Arrays.asList(openids));
        return R.ok().put(users);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{openid}")
    @ApiOperation("获取微信用户信息")
//    @RequiresPermissions("wx:wxuser:info")
    public R info(@RequestParam String appid,@PathVariable("openid") String openid) {
        WxUser wxUser = userService.getById(openid);

        return R.ok().put("wxUser", wxUser);
    }

    /**
     * 同步用户列表
     */
    @ApiOperation("同步用户列表")
    @PostMapping("/syncWxUsers")
//    @RequiresPermissions("wx:wxuser:save")
    public R syncWxUsers( String appid) {
        wxMpService.switchoverTo(appid);
        userService.syncWxUsers(appid);

        return R.ok("任务已建立");
    }



    /**
     * 删除
     */
    @ApiOperation("删除wx用户")
    @PostMapping("/delete")
//    @RequiresPermissions("wx:wxuser:delete")
    public R delete(@CookieValue String appid,@RequestBody String[] ids) {
        userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }




}
