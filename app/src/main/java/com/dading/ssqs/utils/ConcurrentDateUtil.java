package com.dading.ssqs.utils;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/19 14:08
 * 描述	      ${通过date获取时间字符串或者将时间字符串换成date}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConcurrentDateUtil {

    private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmm", Locale.CHINA);
        }
    };
    private static ThreadLocal<DateFormat> threadLocal2 = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
    };

    /**
     * 通过年月日字符串获取date队对象
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr) throws ParseException {
        return threadLocal.get().parse(dateStr);
    }

    /**
     * 通过date获取年月日字符串
     * @param date
     * @return
     */
    public static String format(Date date){
        return threadLocal.get().format(date);
    }

    /**
     * 通过年月日时分秒字符串获取date队对象
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseGood(String dateStr) throws ParseException {
        return threadLocal2.get().parse(dateStr);
    }

    /**
     * 通过date获取年月日时分秒字符串
     * @param date
     * @return
     */
    public static String formatGood(Date date){
        return threadLocal2.get().format(date);
    }
}
