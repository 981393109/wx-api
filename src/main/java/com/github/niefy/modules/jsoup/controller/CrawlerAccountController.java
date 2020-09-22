package com.github.niefy.modules.jsoup.controller;


import com.github.niefy.common.utils.ResponseUtil;
import com.github.niefy.modules.jsoup.service.ICrawlerAccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jsoup")
public class CrawlerAccountController {

    @Autowired
    ICrawlerAccountService crawlerAccountService;

    @ApiOperation("爬取公众号信息")
    @PostMapping("/doCrawler")
    public Object doCrawler(){
        Object account = crawlerAccountService.getAccount();
        return ResponseUtil.ok(account);
    }


}
