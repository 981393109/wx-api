package com.github.niefy.modules.jsoup.service.imp;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.niefy.modules.jsoup.dto.TargetDto;
import com.github.niefy.modules.jsoup.entity.Wtoutiao;
import com.github.niefy.modules.jsoup.mapper.CrawlerAccountMapper;
import com.github.niefy.modules.jsoup.entity.CrawlerAccount;
import com.github.niefy.modules.jsoup.service.ICrawlerAccountService;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
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
            trustEveryone();
            doc = this.getDocument(targetDto.getTargetUrl());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Elements elements = doc.getElementsByTag("a");//找到所有a标签
        for (Element element : elements) {
            final String relHref = element.attr("href"); // == "/"这个是href的属性值，一般都是链接。这里放的是文章的连接
            //用if语句过滤掉不是文章链接的内容。因为文章的链接有两个，但评论的链接只有一个，反正指向相同的页面就拿评论的链接来用吧
            if ((relHref.startsWith("http://")||relHref.startsWith("https://")||relHref.startsWith("//")||relHref.startsWith("/")) && relHref.contains(targetDto.getRelHrefContains()) ) {
                StringBuffer sb = new StringBuffer();
                if(relHref.startsWith("/")||relHref.startsWith("//"))
                    sb.append(targetDto.getHostUrl());
                sb.append(relHref);
                //可以通过这个url获取文章
                Integer count = getArticleItemFromUrl(sb.toString(),targetDto,element,list);
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
        System.out.println(detailurl);
        try {
            if(detailurl.startsWith("//"))
                detailurl="https:"+detailurl;
            Wtoutiao wtoutiao = new Wtoutiao();
            Document document = Jsoup.connect(detailurl).userAgent("Mozilla/5.0").timeout(3000).post();
            //得到标签
            Element elementContent = document.getElementsByClass(articleTitle).first();
            //得到第一张图片作为封面图
            Elements imgs = document.getElementsByTag("img");
            for (Element element : imgs) {
                if(StringUtils.isNotBlank(imgContains)&&element.attr("src").contains(imgContains)){
                     String  imgUrl = element.attr("src");
                     if(imgUrl.startsWith("//"))
                         imgUrl = "https:"+imgUrl;
                    wtoutiao.setThumbUrl(imgUrl);
                    break;
                }else if(StringUtils.isNotBlank(imgContains)){
                    String  imgUrl = element.attr("src");
                    if(imgUrl.startsWith("//"))
                        imgUrl = "https:"+imgUrl;
                    wtoutiao.setThumbUrl(imgUrl);
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


    /**
     * 获取文章标题以及url
     * @return
     */
    public static Integer getArticleItemFromUrl(String detailurl,TargetDto targetDto,Element element,List<Wtoutiao> list) {
        int num = 0;
        try {
            if (detailurl.startsWith("//"))
                detailurl = "https:" + detailurl;
            Wtoutiao wtoutiao = new Wtoutiao();
            //得到标题
            Element firstTitle = element.getElementsByClass(targetDto.getArticleTitle()).first();
            //得到封面图
            Elements imgs = element.getElementsByTag("img");
            if(firstTitle!=null){
                wtoutiao.setUrl(detailurl);
                wtoutiao.setTitle(firstTitle.text());
            }

            for (Element imgItem : imgs) {
                if(StringUtils.isNotBlank(targetDto.getImgContains())&&imgItem.attr("src").contains(targetDto.getImgContains())){
                    String imgUrl = imgItem.attr("src");
                    if(imgUrl.startsWith("//"))
                        imgUrl = "https:"+imgUrl;
                    wtoutiao.setThumbUrl(imgUrl);
                    break;
                }else if(StringUtils.isNotBlank(targetDto.getImgContains())){
                    String  imgUrl = imgItem.attr("src");
                    if(imgUrl.startsWith("//"))
                        imgUrl = "https:"+imgUrl;
                    wtoutiao.setThumbUrl(imgUrl);
                    break;
                }
            }
            list.add(wtoutiao);
            num++;
        }catch (Exception e){
            e.printStackTrace();
        }
        return num;
    }


    /**
     * 信任任何站点，实现https页面的正常访问
     *
     */

    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }




    public static Document getDocument(String url) throws IOException, InterruptedException{
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);              // 启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(10 * 1000);// 设置连接超时时间
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage page = webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(10 * 1000);// 等待js后台执行30秒
        String pageAsXml = page.asXml();
        Document doc = Jsoup.parse(pageAsXml, url);
        return doc;
    }





}
