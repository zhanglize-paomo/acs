package com.example.asc.asc.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JsoupHtmlUtils解析html文件
 *
 * @author zhanglize
 * @create 2019/11/21
 */
public class JsoupHtmlUtils {

    /**
     * 获取request的参数信息
     *
     * @param html 待解析的html
     * @return
     */
    public static TreeMap<String,Object> getJsoupHtmlUtils(String html) {
        TreeMap<String,Object> treeMap = new TreeMap<>();
        //第一步，将字符内容解析成一个Document类
        Document doc = Jsoup.parse(html);
        //第二步，根据我们需要得到的标签，选择提取相应标签的内容
        Elements elements = doc.select("input[type=hidden]");
        for (Element element : elements) {
            String id = match(element.toString(), "input", "id");
            String value = match(element.toString(), "input", "value");
            treeMap.put(id,value);
        }
        return treeMap;
    }

    /**
     * 获取指定HTML标签的指定属性的值
     *
     * @param source  要匹配的源文本
     * @param element 标签名称
     * @param attr    标签的属性名称
     * @return 属性值列表
     */
    public static String match(String source, String element, String attr) {
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?(\\s.*?)?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        String str = null;
        while (m.find()) {
            str = m.group(1);
        }
        return str;
    }


}
