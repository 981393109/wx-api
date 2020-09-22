package com.github.niefy.modules.wx.manage;

import com.github.niefy.common.utils.R;
import com.github.niefy.modules.wx.entity.WxAccount;
import com.github.niefy.modules.wx.service.WxAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;



/**
 * 公众号账号
 *
 * @author niefy
 * @date 2020-06-17 13:56:51
 */
@RestController
@RequestMapping("/manage/wxAccount")
@Api(tags = "公众号账号相关接口")
public class WxAccountManageController {
    @Autowired
    private WxAccountService wxAccountService;

    /**
     * 列表
     */
    @ApiOperation("公众号账号列表")
    @GetMapping("/list")
//    @RequiresPermissions("wx:wxaccount:list")
    public R list(){
        List<WxAccount> list = wxAccountService.list();

        return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @ApiOperation("公众号账号详情")
    @GetMapping("/info/{appid}")
//    @RequiresPermissions("wx:wxaccount:info")
    public R info(@PathVariable("id") String appid){
		WxAccount wxAccount = wxAccountService.getById(appid);

        return R.ok().put("wxAccount", wxAccount);
    }

    /**
     * 保存
     */
    @ApiOperation("保存公众号账号")
    @PostMapping("/save")
//    @RequiresPermissions("wx:wxaccount:save")
    public R save(@RequestBody WxAccount wxAccount){
		wxAccountService.save(wxAccount);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除公众号账号")
//    @RequiresPermissions("wx:wxaccount:delete")
    public R delete(@RequestBody String[] appids){
		wxAccountService.removeByIds(Arrays.asList(appids));

        return R.ok();
    }

}
