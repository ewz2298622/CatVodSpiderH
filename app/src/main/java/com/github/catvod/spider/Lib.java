package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;
//import android.util.Base64;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Util;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Qile
 */
public class Lib extends Spider {

    private String siteUrl = "https://www.libvio.pro";

    private Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<>();
        header.put("User-Agent", Util.CHROME);
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        if (!extend.isEmpty()) siteUrl = extend;
    }

    /**
     * 首页内容初始化，主要要完成分类id和值、二级筛选等，
     * 也可以在这个方法里面完成首页推荐视频获取
     *
     * @param filter true: 开启二级筛选 false:关闭
     * @return 返回字符串
     */
    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("1", "2", "4", "15",  "16");
        List<String> typeNames = Arrays.asList("电影", "剧集", "动漫", "日韩剧", "欧美剧");
        for (int i = 0; i < typeIds.size(); i++) classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        String f = "{\"1\": [{\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}]}, {\"key\": \"lang\", \"name\": \"语言\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国语\", \"v\": \"国语\"}, {\"n\": \"英语\", \"v\": \"英语\"}, {\"n\": \"粤语\", \"v\": \"粤语\"}, {\"n\": \"闽南语\", \"v\": \"闽南语\"}, {\"n\": \"韩语\", \"v\": \"韩语\"}, {\"n\": \"日语\", \"v\": \"日语\"}, {\"n\": \"法语\", \"v\": \"法语\"}, {\"n\": \"德语\", \"v\": \"德语\"}, {\"n\": \"其它\", \"v\": \"其它\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"2\": [{\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"中国大陆\", \"v\": \"中国大陆\"}, {\"n\": \"中国香港\", \"v\": \"中国香港\"}, {\"n\": \"中国台湾\", \"v\": \"中国台湾\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"印度\", \"v\": \"印度\"}, {\"n\": \"意大利\", \"v\": \"意大利\"}, {\"n\": \"西班牙\", \"v\": \"西班牙\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2007\", \"v\": \"2007\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}, {\"n\": \"2003\", \"v\": \"2003\"}, {\"n\": \"2002\", \"v\": \"2002\"}, {\"n\": \"2001\", \"v\": \"2001\"}, {\"n\": \"2000\", \"v\": \"2000\"}]}, {\"key\": \"lang\", \"name\": \"语言\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国语\", \"v\": \"国语\"}, {\"n\": \"英语\", \"v\": \"英语\"}, {\"n\": \"粤语\", \"v\": \"粤语\"}, {\"n\": \"闽南语\", \"v\": \"闽南语\"}, {\"n\": \"韩语\", \"v\": \"韩语\"}, {\"n\": \"日语\", \"v\": \"日语\"}, {\"n\": \"其它\", \"v\": \"其它\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"4\": [{\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"大陆\", \"v\": \"大陆\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}, {\"n\": \"香港\", \"v\": \"香港\"}, {\"n\": \"台湾\", \"v\": \"台湾\"}, {\"n\": \"泰国\", \"v\": \"泰国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"新加坡\", \"v\": \"新加坡\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2007\", \"v\": \"2007\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}, {\"n\": \"2003\", \"v\": \"2003\"}, {\"n\": \"2002\", \"v\": \"2002\"}, {\"n\": \"2001\", \"v\": \"2001\"}, {\"n\": \"2000\", \"v\": \"2000\"}]}, {\"key\": \"lang\", \"name\": \"语言\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国语\", \"v\": \"国语\"}, {\"n\": \"日语\", \"v\": \"日语\"}, {\"n\": \"英语\", \"v\": \"英语\"}, {\"n\": \"粤语\", \"v\": \"粤语\"}, {\"n\": \"闽南语\", \"v\": \"闽南语\"}, {\"n\": \"韩语\", \"v\": \"韩语\"}, {\"n\": \"其它\", \"v\": \"其它\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"15\": [{\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"日本\", \"v\": \"日本\"}, {\"n\": \"韩国\", \"v\": \"韩国\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2007\", \"v\": \"2007\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}, {\"n\": \"2003\", \"v\": \"2003\"}, {\"n\": \"2002\", \"v\": \"2002\"}, {\"n\": \"2001\", \"v\": \"2001\"}, {\"n\": \"2000\", \"v\": \"2000\"}]}, {\"key\": \"lang\", \"name\": \"语言\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国语\", \"v\": \"国语\"}, {\"n\": \"英语\", \"v\": \"英语\"}, {\"n\": \"粤语\", \"v\": \"粤语\"}, {\"n\": \"闽南语\", \"v\": \"闽南语\"}, {\"n\": \"韩语\", \"v\": \"韩语\"}, {\"n\": \"日语\", \"v\": \"日语\"}, {\"n\": \"其它\", \"v\": \"其它\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}], \"16\": [{\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"美国\", \"v\": \"美国\"}, {\"n\": \"法国\", \"v\": \"法国\"}, {\"n\": \"英国\", \"v\": \"英国\"}, {\"n\": \"德国\", \"v\": \"德国\"}, {\"n\": \"加拿大\", \"v\": \"加拿大\"}, {\"n\": \"其他\", \"v\": \"其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2023\", \"v\": \"2023\"}, {\"n\": \"2022\", \"v\": \"2022\"}, {\"n\": \"2021\", \"v\": \"2021\"}, {\"n\": \"2020\", \"v\": \"2020\"}, {\"n\": \"2019\", \"v\": \"2019\"}, {\"n\": \"2018\", \"v\": \"2018\"}, {\"n\": \"2017\", \"v\": \"2017\"}, {\"n\": \"2016\", \"v\": \"2016\"}, {\"n\": \"2015\", \"v\": \"2015\"}, {\"n\": \"2014\", \"v\": \"2014\"}, {\"n\": \"2013\", \"v\": \"2013\"}, {\"n\": \"2012\", \"v\": \"2012\"}, {\"n\": \"2011\", \"v\": \"2011\"}, {\"n\": \"2010\", \"v\": \"2010\"}, {\"n\": \"2009\", \"v\": \"2009\"}, {\"n\": \"2008\", \"v\": \"2008\"}, {\"n\": \"2007\", \"v\": \"2007\"}, {\"n\": \"2006\", \"v\": \"2006\"}, {\"n\": \"2005\", \"v\": \"2005\"}, {\"n\": \"2004\", \"v\": \"2004\"}, {\"n\": \"2003\", \"v\": \"2003\"}, {\"n\": \"2002\", \"v\": \"2002\"}, {\"n\": \"2001\", \"v\": \"2001\"}, {\"n\": \"2000\", \"v\": \"2000\"}]}, {\"key\": \"lang\", \"name\": \"语言\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"国语\", \"v\": \"国语\"}, {\"n\": \"英语\", \"v\": \"英语\"}, {\"n\": \"粤语\", \"v\": \"粤语\"}, {\"n\": \"闽南语\", \"v\": \"闽南语\"}, {\"n\": \"韩语\", \"v\": \"韩语\"}, {\"n\": \"日语\", \"v\": \"日语\"}, {\"n\": \"其它\", \"v\": \"其它\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"time\"}, {\"n\": \"人气\", \"v\": \"hits\"}, {\"n\": \"评分\", \"v\": \"score\"}]}]}";
        JSONObject filterConfig = new JSONObject(f);
        Document doc = Jsoup.parse(OkHttp.string(siteUrl, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".stui-vodlist").eq(0).select(".stui-vodlist__box")) {
            String vid = siteUrl + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("a").attr("data-original");
            String remark = li.select("a").select(".pic-text").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(classes, list, filterConfig);
    }

    /**
     * 分类内容方法
     *
     * @param tid    影片分类id值，来自 homeContent 里面的 type_id 值
     * @param pg     第几页
     * @param filter 不用管这个参数
     * @param extend 用户已经选择的二级筛选数据
     * @return 返回字符串
     */
    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) throws Exception {    
        String area = extend.get("area") == null ? "" : extend.get("area"); 
        String year = extend.get("year") == null ? "" : extend.get("year");
        String by = extend.get("by") == null ? "" : extend.get("by");              
        String lang = extend.get("lang") == null ? "" : extend.get("lang");   
        
        String cateUrl = siteUrl + String.format("/show/%s-%s-%s--%s----%s---%s.html", tid, area, by, lang, pg, year);
        Document doc = Jsoup.parse(OkHttp.string(cateUrl, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select("div.stui-vodlist__box")) {
            String vid = siteUrl + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("a").attr("data-original");
            String remark = li.select("span.pic-text").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }

    /**
     * 影片详情方法
     *
     * @param ids ids.get(0) 来源于分类方法或搜索方法的 vod_id
     * @return 返回字符串
     */
    @Override
    public String detailContent(List<String> ids) throws Exception {
        Document doc = Jsoup.parse(OkHttp.string(ids.get(0), getHeader()));              
        String title = doc.select("h1.title").text();
        String pic = doc.select("img.lazyload").attr("data-original");        
        String desc = doc.select("span.detail-content").text(); //简介 
        String text = doc.select("p.data").text();
        Pattern pattern = Pattern.compile(
                "类型：([^/]+) /地区：([^/]+) /年份：(\\d+).*?主演：([^/]+) /导演：([^/]+)(?:\\s+总集数：[^/]+\\s+评分：[^/]+\\s+最后更新：[^/]+)");
        Matcher matcher = pattern.matcher(text);
        String typeName = "";
        String area = "";
        String actor = "";
        String year = "";
        String director = "";
        if (matcher.find()) {
            typeName = matcher.group(1);
            area = matcher.group(2);
            year = matcher.group(3);
            actor = matcher.group(4);
            director = matcher.group(5);
        }
        
        // 取播放列表数据
        Elements sources = doc.select(".stui-pannel__head h3");
        Elements sourceList = doc.select(".stui-content__playlist"); 
        StringBuilder vod_play_url = new StringBuilder();
        StringBuilder vod_play_from = new StringBuilder();
        for (int i = 0; i < sources.size(); i++) {
            String spanText = sources.get(i).text();
            if (spanText.contains("视频下载 (夸克)") || spanText.contains("视频下载（UC）")) continue;
            String playFromText = spanText + "";
        //    String spanText = circuits.get(i).select("span").text();
        //    String smallText = sourceList.get(i).select("small").text();           
        //    String playFromText = spanText + "(共" + smallText + "集)";
            vod_play_from.append(playFromText).append("$$$");
            Elements playList = sourceList.get(i).select("li a");
            for (int j = 0; j < playList.size(); j++) {
                Element a = playList.get(j);
                String href = siteUrl + a.attr("href");
                String vod = a.text();
                vod_play_url.append(vod).append("$").append(href);
                boolean notLastEpisode = j < playList.size() - 1;
                vod_play_url.append(notLastEpisode ? "#" : "$$$");
            }
        }
        
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodPic(pic);
        vod.setVodName(title);        
        vod.setTypeName(typeName);
        vod.setVodYear(year);
        vod.setVodArea(area);
        vod.setVodActor(actor);
        vod.setVodDirector(director);
        vod.setVodContent("😎温馨提示【软件接口免费，请勿付费购买】资源均为免费分享："+desc);  //简介2
        vod.setVodPlayFrom(vod_play_from.toString());
        vod.setVodPlayUrl(vod_play_url.toString());
        return Result.string(vod);
    }

    /**
     * 搜索
     *
     * @param key 关键字/词
     * @return 返回值
     */
    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchUrl = siteUrl + "/search/-------------.html?wd=" + URLEncoder.encode(key) + "&submit=";
        Document doc = Jsoup.parse(OkHttp.string(searchUrl, getHeader()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select("div.stui-vodlist__box")) {
            String vid = siteUrl + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("a").attr("data-original");
            String remark = li.select("span.pic-text").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }

    @Override
    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        return Result.get().url(id).parse().header(getHeader()).string();
    }
}
