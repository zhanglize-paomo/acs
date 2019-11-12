package com.example.asc.asc.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * XML工具类
 *
 * @author zhanglize
 * @create 2019/11/12
 */
public class XmlUtil {

    public static  Map<String,Map<String,Object>>  xmlStrToMap(String xmlStr) {
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            Element root = document.getRootElement();
            Iterator it = root.elementIterator();
            // 递归 xml 转 map
            Map<String,Map<String,Object>>  rootMap = getContentMap(root, it);
            return rootMap;
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static  Map<String,Map<String,Object>>  getContentMap(Element root, Iterator it){
        Map<String,Map<String,Object>> rootMap = new HashMap<>();
        Map<String, Object> nextRootMap = new HashMap<>();
        while(it.hasNext()){
            Element nextRoot = (Element)it.next();
            Iterator nextIt = nextRoot.elementIterator();
            if(nextIt.hasNext()){ //当前xml标签下，存嵌套了其他标签
                Map<String,Map<String,Object>>  nextContentMap = getContentMap(nextRoot, nextIt);
                nextRootMap.putAll(nextContentMap);
            }else{ //当前xml标签下，只有值，未嵌套其他标签
                nextRootMap.put(nextRoot.getName(), nextRoot.getTextTrim());
            }
        }
        rootMap.put(root.getName(), nextRootMap);
        return rootMap;
    }

}
