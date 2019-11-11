package com.example.asc.asc.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class MD5 {

	/**
	 * MD5加密规则
	 *
	 * @param str
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String md5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            byte[] byteDigest = md.digest();
            int i;
            //字符数组转换成字符串
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString().toLowerCase();
            // 16位的加密
             //return buf.toString().substring(8, 24).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


	/***
	 * 其他参数按照参数排序后的值拼接
	 *
	 * @param mapParams
	 * @param excludeStr:要被排除掉，不組進字串的的name
	 * @return 按照ascii由小到大排序 参数按照参数排序后的值拼接
	 */
	public static String getSignContent(Map<String, ? extends Object> mapParams, String excludeStr,
										String replaceKeyStr) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(mapParams.keySet());
		Collections.sort(keys);

		int index = 0;
		for (int i = 0; i < keys.size(); ++i) {
			String key = (String) keys.get(i);

			if (excludeStr == null || !key.equalsIgnoreCase(replaceKeyStr + excludeStr)) {
				Object value = mapParams.get(key);
//				if (value != null && org.apache.commons.lang.StringUtils.isNotBlank(value.toString())) {
//					if (replaceKeyStr != null) {
//						key = key.replace(replaceKeyStr, "");
//					}
//					content.append(new StringBuilder().append(value).toString());
//					++index;
//				}
			}

		}
		return content.toString();
	}

	public static String getSignMsg(Map<String, String> map,String keySign) {
		StringBuffer sb = new StringBuffer();
		sb.append(putPairsSequenceAndTogether(map));
		sb.append("&key="+keySign);
		System.out.println(sb.toString());
		return sb.toString().trim();
	}
	
	public static String putPairsSequenceAndTogether(Map<String, String> info) {
	    List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(info.entrySet());
	    Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
	        @Override
	        public int compare(Map.Entry<String, String> arg0, Map.Entry<String, String> arg1) {
	            return (arg0.getKey()).compareTo(arg1.getKey());
	        }
	    });
	    String ret = "";
	    for (Map.Entry<String, String> entry : infoIds) {
	        ret += entry.getKey();
	        ret += "=";
	        ret += entry.getValue();
	        ret += "&";
	    }
	    ret = ret.substring(0, ret.length() - 1);
	    return ret;
	}
	
	public static String doEncrypt(TreeMap<String, String> map,String privateKey) throws Exception {
		Set<String> keys =  map.keySet();
		StringBuilder originStr = new StringBuilder();
		for(String key:keys){
			if(null!=map.get(key)&&!map.get(key).equals("")&&!"mac".equals(key))
			originStr.append(key).append("=").append(map.get(key)).append("&");
		}
	    originStr = originStr.append("k=");
		String str=originStr.toString();
		str=str+privateKey;
		System.out.println(str);
		return str;
	}


}
