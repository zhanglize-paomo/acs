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
}
