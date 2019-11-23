package com.example.asc.asc.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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

    /**
     * 功能：前台交易构造HTTP POST自动提交表单<br>
     * @param action 表单提交地址<br>
     * @param hiddens 以MAP形式存储的表单键值<br>
     * @param encoding 上送请求报文域encoding字段的值<br>
     * @return 构造好的HTTP POST交易表单<br>
     */
    public static String createAutoFormHtml(String action, Map<String,Object> hiddens, String encoding) {
        StringBuffer sf = new StringBuffer();
        sf.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset="+encoding+"\"/></head><body>");
        sf.append("<form id = \"pay_form\" action=\"" + action
                + "\" method=\"post\">");
        if (null != hiddens && 0 != hiddens.size()) {
            Set<Map.Entry<String, Object>> set = hiddens.entrySet();
            Iterator<Map.Entry<String, Object>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> ey = it.next();
                String key = ey.getKey();
                String value = ey.getValue().toString();
                sf.append("<input type=\"hidden\" name=\"" + key + "\" id=\""
                        + key + "\" value=\"" + value + "\"/>");
            }
        }
        sf.append("</form>");
        sf.append("</body>");
        sf.append("<script type=\"text/javascript\">");
        sf.append("document.all.pay_form.submit();");
        sf.append("</script>");
        sf.append("</html>");
        return sf.toString();
    }


}
