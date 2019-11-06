package com.example.asc.asc.trd.common;

import com.blue.util.StringUtil;
import com.example.asc.asc.util.DateUtils;

import java.util.Date;

/**
 * 公用得日期类
 *
 * @author zhanglize
 * @create 2019/11/6
 */
public class DateCommonUtils {

    /**
     * 判断日期是否对应的日期格式
     *
     * @param msghd_trdt
     * @return
     */
    public static String judgeDateFormat(String msghd_trdt) {
        if (StringUtil.isEmpty(msghd_trdt)) {
            return DateUtils.timeToDate(new Date());
        } else {
            if (DateUtils.judgeDateFormat(msghd_trdt)) {
                return msghd_trdt;
            } else {
                return DateUtils.timeToDate(DateUtils.stringToDate(msghd_trdt));
            }
        }
    }
}
