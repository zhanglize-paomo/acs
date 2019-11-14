package com.example.asc.asc.util;

import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * @author zhanglize
 * @create 2019/11/11
 */
public class SortUtils {

    public static Map<String, String> Ksort(Map<String, String> map) {
        Map<String, String> stringMap = new HashMap<>();
        String[] key = new String[map.size()];
        int index = 0;
        for (String k : map.keySet()) {
            key[index] = k;
            index++;
        }
        Arrays.sort(key);
        for (String s : key) {
            stringMap.put(s,map.get(s));
        }
//        sb = sb.substring(0, sb.length() - 1);
//        // 将得到的字符串进行处理得到目标格式的字符串
//        try {
//            sb = URLEncoder.encode(sb, "UTF-8");
//        } catch (
//                UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        // 使用常见的UTF-8编码
//        sb = sb.replace("%3D", "=").replace("%26", "&");
        return stringMap;
    }

    /**
     * java对参数按key进行字典升序排列
     *
     * @param param   参数
     * @param encode  编码
     * @param isLower 是否小写
     * @return
     */
    public static String formatUrlParam(Map<String, String> param, String encode, boolean isLower) {
        String params = "";
        Map<String, String> map = param;
        try {
            List<Map.Entry<String, String>> itmes = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            //对所有传入的参数按照字段名从小到大排序
            //Collections.sort(items); 默认正序
            //可通过实现Comparator接口的compare方法来完成自定义排序
            Collections.sort(itmes, new Comparator<Map.Entry<String, String>>() {
                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    // TODO Auto-generated method stub
                    return (o1.getKey().toString().compareTo(o2.getKey()));
                }
            });
            //构造URL 键值对的形式
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> item : itmes) {
                if (StringUtils.isNotBlank(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    val = URLEncoder.encode(val, encode);
                    if (isLower) {
                        sb.append(key.toLowerCase() + "=" + val);
                    } else {
                        sb.append(key + "=" + val);
                    }
                    sb.append("&");
                }
            }
            params = sb.toString();
            if (!params.isEmpty()) {
                params = params.substring(0, params.length() - 1);
            }
        } catch (Exception e) {
            return "";
        }
        return params;
    }

}
