package com.example.asc.asc.util;

/**
 * 金钱的单位转换工具类
 *
 * @author zhanglize
 * @create 2019/11/1
 */
public class MoneyUtils {

    /**
     * 将单位分转换为元单位的
     *
     * @param amount
     * @return
     */
    public static String convertPart(Long amount) {
       return amount / 100 + "." + amount % 100 / 10 + amount % 100 % 10;
    }


    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static String changeY2F(String amount){
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(currency+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");
        }
        return amLong.toString();
    }
}
