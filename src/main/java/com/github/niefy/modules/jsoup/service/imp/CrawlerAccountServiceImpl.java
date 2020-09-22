package com.github.niefy.modules.jsoup.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.niefy.modules.jsoup.dto.TargetDto;
import com.github.niefy.modules.jsoup.entity.Wtoutiao;
import com.github.niefy.modules.jsoup.mapper.CrawlerAccountMapper;
import com.github.niefy.modules.jsoup.entity.CrawlerAccount;
import com.github.niefy.modules.jsoup.service.ICrawlerAccountService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CrawlerAccountServiceImpl extends ServiceImpl<CrawlerAccountMapper,CrawlerAccount> implements ICrawlerAccountService{


    @Override
    public Object getAccount() {
        QueryWrapper<CrawlerAccount> caQw = new QueryWrapper<>();
        caQw.lambda().eq(CrawlerAccount::getDelFlag,0);
        caQw.lambda().eq(CrawlerAccount::getEnabled,1);
        caQw.lambda().eq(CrawlerAccount::getType,2);
        List<CrawlerAccount> list = this.list(caQw);
        if(list!=null && list.size()>0){
            for (CrawlerAccount crawlerAccount : list) {
                String remark = crawlerAccount.getRemark();
                TargetDto targetDto = JSON.parseObject(remark, TargetDto.class);
                targetDto.setTargetUrl(crawlerAccount.getWxName());
                Map<String, Object> map = this.crawlerDataByUrl(targetDto);
                System.out.println(map);
            }
        }
        return "ok";
    }

    @Override
    public Map<String, Object> crawlerDataByUrl(TargetDto targetDto) {
        boolean isStop = false;
        Document doc = null;
        Map<String ,Object > result = new HashMap<>();
        List<Wtoutiao> list = new LinkedList<>();
        int total = 0;
        try {
            doc = Jsoup.connect(targetDto.getTargetUrl()).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36").timeout(3000).post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByTag("a");//找到所有a标签
        for (Element element : elements) {
            final String relHref = element.attr("href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
            //用if语句过滤掉不是文章链接的内容。因为文章的链接有两个，但评论的链接只有一个，反正指向相同的页面就拿评论的链接来用吧
            if (relHref.startsWith("http://") && relHref.contains(targetDto.getRelHrefContains()) ) {
                StringBuffer sb = new StringBuffer();
                sb.append(relHref);
                //可以通过这个url获取文章
                Integer count = getArticleFromUrl(sb.toString(), targetDto.getArticleTitle(), targetDto.getImgContains(),list);
                total+=count;
            }
        }
        result.put("count",total);
        result.put("list",list);
        return result;
    }


    /**
     * 获取文章内容
     * @param detailurl
     */
    public static Integer getArticleFromUrl(String detailurl,String articleTitle,String imgContains,List<Wtoutiao> list) {
        int num = 0;
        try {
            Wtoutiao wtoutiao = new Wtoutiao();
            Document document = Jsoup.connect(detailurl).userAgent("Mozilla/5.0").timeout(3000).post();
            //得到标签
            Element elementContent = document.getElementsByClass(articleTitle).first();
            //得到第一张图片作为封面图
            Elements imgs = document.getElementsByTag("img");
            for (Element element : imgs) {
                if(element.attr("src").contains(imgContains)){
                    wtoutiao.setThumbUrl(element.attr("src"));
                    break;
                }
            }
            //新增数据文章数据
            wtoutiao.setTitle(elementContent.text());
            wtoutiao.setUrl(detailurl);
            list.add(wtoutiao);
            num++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }



}
