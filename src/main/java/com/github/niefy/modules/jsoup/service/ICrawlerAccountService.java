package com.github.niefy.modules.jsoup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.niefy.modules.jsoup.dto.TargetDto;
import com.github.niefy.modules.jsoup.entity.CrawlerAccount;

import java.util.Map;

public interface ICrawlerAccountService extends IService<CrawlerAccount> {


    //得到数据库中的网页信息进行爬取文章
    Object getAccount();


    //爬取网页数据
    Map<String ,Object> crawlerDataByUrl(TargetDto targetDto);

}
