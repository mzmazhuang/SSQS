package com.dading.ssqs.utils;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/2 16:42
 * 描述	      ${通过calendar获取不同的时间日期 如前一天后一天 上周下周}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class CalendarUtil {

    private int weeks = 0;// 用来全局控制 上一周，本周，下一周的周数变化
    private int MaxDate; // 一月最大天数
    private int MaxYear; // 一年最大天数

    public static void main (String[] args) {
        CalendarUtil tt = new CalendarUtil( );
        System.out.println("获取当天日期:" + tt.getNowTime("yyyy-MM-dd"));
        System.out.println("获取本周一日期:" + tt.getMondayOFWeek("yyyy-MM-dd"));
        System.out.println("获取本周日的日期~:" + tt.getCurrentWeekday( ));
        System.out.println("获取上周一日期:" + tt.getPreviousWeekday("yyyy-MM-dd"));
        System.out.println("获取上周日日期:" + tt.getPreviousWeekSunday("yyyy-MM-dd"));
        System.out.println("获取下周一日期:" + tt.getNextMonday( ));
        System.out.println("获取下周日日期:" + tt.getNextSunday( ));
        System.out.println("获得相应周的周六的日期:" + tt.getNowTime("yyyy-MM-dd"));
        System.out.println("获取本月第一天日期:" + tt.getFirstDayOfMonth( ));
        System.out.println("获取本月最后一天日期:" + tt.getDefaultDay( ));
        System.out.println("获取上月第一天日期:" + tt.getPreviousMonthFirst( ));
        System.out.println("获取上月最后一天的日期:" + tt.getPreviousMonthEnd( ));
        System.out.println("获取下月第一天日期:" + tt.getNextMonthFirst( ));
        System.out.println("获取下月最后一天日期:" + tt.getNextMonthEnd( ));
        System.out.println("获取本年的第一天日期:" + tt.getCurrentYearFirst( ));
        System.out.println("获取本年最后一天日期:" + tt.getCurrentYearEnd( ));
        System.out.println("获取去年的第一天日期:" + tt.getPreviousYearFirst( ));
        System.out.println("获取去年的最后一天日期:" + tt.getPreviousYearEnd( ));
        System.out.println("获取明年第一天日期:" + tt.getNextYearFirst( ));
        System.out.println("获取明年最后一天日期:" + tt.getNextYearEnd( ));
        System.out.println("获取本季度第一天:" + tt.getThisSeasonFirstTime(11));
        System.out.println("获取本季度最后一天:" + tt.getThisSeasonFinallyTime(11));
        System.out.println("获取两个日期之间间隔天数2008-12-1~2008-9.29:"
                + CalendarUtil.getTwoDay("2008-12-1", "2008-9-29"));
        System.out.println("获取当前月的第几周：" + getWeekOfMonth( ));
        System.out.println("获取当前年份：" + getYear( ));
        System.out.println("获取当前月份：" + getMonth( ));
        System.out.println("获取今天在本年的第几天：" + getDayOfYear( ));
        System.out.println("获得今天在本月的第几天(获得当前日)：" + getDayOfMonth( ));
        System.out.println("获得今天在本周的第几天：" + getDayOfWeek( ));
        System.out.println("获得半年后的日期："
                + convertDateToString(getTimeYearNext( )));
    }

    /**
     * 获取当前年份
     * @return
     */
    public static int getYear ( ) {
        return Calendar.getInstance( ).get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int getMonth ( ) {
        return Calendar.getInstance( ).get(Calendar.MONTH) + 1;
    }

    /**
     * 获得今天在本年的第几天
     * @return
     */
    public static int getDayOfYear ( ) {
        return Calendar.getInstance( ).get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得今天在本月的第几天
     * @return
     */
    public static int getDayOfMonth ( ) {
        return Calendar.getInstance( ).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得今天在本周的第几天
     * @return
     */
    public static int getDayOfWeek ( ) {
        return Calendar.getInstance( ).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前月的第几周
     * @return
     */
    public static int getWeekOfMonth ( ) {
        return Calendar.getInstance( ).get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获得半年后的日期
     * @return
     */
    public static Date getTimeYearNext ( ) {
        Calendar.getInstance( ).add(Calendar.DAY_OF_YEAR, 183);
        return Calendar.getInstance( ).getTime( );
    }

    /**
     * 讲date转换成年月日字符串日期
     * @param dateTime
     * @return
     */
    public static String convertDateToString (Date dateTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return df.format(dateTime);
    }

    /**
     * 获取两个日期之间间隔天数2008-12-1~2008-9.29:
     * @param sj1
     * @param sj2
     * @return
     */
    public static String getTwoDay (String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime( ) - mydate.getTime( )) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }


    public static String getWeek (String sdate) {
        // 再转换为时间
        Date date = CalendarUtil.strToDate(sdate);
        Calendar c = Calendar.getInstance( );
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE", Locale.CHINA).format(c.getTime( ));
    }

    /**
     * 讲字符串日期转换成date
     * @param strDate
     * @return
     */
    public static Date strToDate (String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 两个字符串日期相差天数
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays (String date1, String date2) {
        if (date1 == null || date1.equals(""))
            return 0;
        if (date2 == null || date2.equals(""))
            return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = null;
        Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
        }
        long day = (date.getTime( ) - mydate.getTime( )) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * 获取本月最后一天日期
     *
     * @return
     */
    public String getDefaultDay ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取上月第一天日期
     * @return
     */
    public String getPreviousMonthFirst ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -1);// 减一个月，变为下月的1号
        // lastDate.add(Calendar.DATE,-1);//减去一天，变为当月最后一天

        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取本月第一天日期
     *
     * @return
     */
    public String getFirstDayOfMonth ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取本周日的日期
     *
     * @return
     */
    public String getCurrentWeekday ( ) {
        weeks = 0;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        Date monday = currentDate.getTime( );

        DateFormat df = DateFormat.getDateInstance( );
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取当天日期
     *
     * @param dateformat
     * @return
     */
    public String getNowTime (String dateformat) {
        Date now = new Date( );
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat, Locale.CHINA);// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        return hehe;
    }

    /**
     * 获取今天是一周第几天
     * @return
     */
    private int getMondayPlus ( ) {
        Calendar cd = Calendar.getInstance( );
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获取本周一日期
     *
     * @param format
     * @return
     */
    public String getMondayOFWeek (String format) {
        weeks = 0;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime( );

        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取本周六日期
     *
     * @param format
     * @return
     */
    public String getSaturday (String format) {
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 6);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 获取本周日日期
     *
     * @param format
     * @return
     */
    public String getSunday (String format) {
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks + 7);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 上周日
     *
     * @param format
     * @return
     */
    public String getPreviousWeekSunday (String format) {
        weeks = 0;
        weeks--;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 上上周
     *
     * @param format
     * @return
     */
    public String getPre2WeekSunday (String format) {
        weeks = 0;
        weeks = weeks--;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + weeks - 6);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 上上上周
     *
     * @param format
     * @return
     */
    public String getPre3WeekSunday (String format) {
        weeks = 0;
        weeks = weeks--;
        weeks = weeks--;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + (weeks - 6 - 6));
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 上周一
     *
     * @param format
     * @return
     */
    public String getPreviousWeekday (String format) {
        weeks = 0;
        weeks--;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 上上周一
     *
     * @param format
     * @return
     */
    public String getPre2Weekday (String format) {
        weeks = 0;
        weeks--;
        weeks--;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 上上上周一
     *
     * @param format
     * @return
     */
    public String getPre3Weekday (String format) {
        weeks = 0;
        weeks--;
        weeks--;
        weeks--;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 * weeks);
        Date monday = currentDate.getTime( );
        DateFormat df = new SimpleDateFormat(format, Locale.CHINA);
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 下周一
     *
     * @return
     */
    public String getNextMonday ( ) {
        weeks++;
        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        Date monday = currentDate.getTime( );
        DateFormat df = DateFormat.getDateInstance( );
        String preMonday = df.format(monday);
        return preMonday;
    }

    /**
     * 下星期日
     *
     * @return
     */
    public String getNextSunday ( ) {

        int mondayPlus = this.getMondayPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7 + 6);
        Date monday = currentDate.getTime( );
        DateFormat df = DateFormat.getDateInstance( );
        String preMonday = df.format(monday);
        return preMonday;
    }
    /**
     * 获取今天是一月第几天
     * @return
     */
    private int getMonthPlus ( ) {
        Calendar cd = Calendar.getInstance( );
        int monthOfNumber = cd.get(Calendar.DAY_OF_MONTH);
        cd.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        cd.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        MaxDate = cd.get(Calendar.DATE);
        if (monthOfNumber == 1) {
            return -MaxDate;
        } else {
            return 1 - monthOfNumber;
        }
    }

    /**
     * 获取上月最后一天的日期
     *
     * @return
     */
    public String getPreviousMonthEnd ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.add(Calendar.MONTH, -1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取下月第一天日期
     *
     * @return
     */
    public String getNextMonthFirst ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.add(Calendar.MONTH, 1);// 减一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取下月最后一天日期
     *
     * @return
     */
    public String getNextMonthEnd ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.add(Calendar.MONTH, 1);// 加一个月
        lastDate.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        lastDate.roll(Calendar.DATE, -1);// 日期回滚一天，也就是本月最后一天
        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取明年最后一天日期
     *
     * @return
     */
    public String getNextYearEnd ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        lastDate.roll(Calendar.DAY_OF_YEAR, -1);
        str = sdf.format(lastDate.getTime( ));
        return str;
    }

    /**
     * 获取明年第一天日期
     *
     * @return
     */
    public String getNextYearFirst ( ) {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        Calendar lastDate = Calendar.getInstance( );
        lastDate.add(Calendar.YEAR, 1);// 加一个年
        lastDate.set(Calendar.DAY_OF_YEAR, 1);
        str = sdf.format(lastDate.getTime( ));
        return str;

    }

    //每年的天数
    private int getMaxYear ( ) {
        Calendar cd = Calendar.getInstance( );
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        return MaxYear;
    }

    /**
     * /**
     * 获取今天是一年第几天
     * @return
     */
    private int getYearPlus ( ) {
        Calendar cd = Calendar.getInstance( );
        int yearOfNumber = cd.get(Calendar.DAY_OF_YEAR);// 获得当天是一年中的第几天
        cd.set(Calendar.DAY_OF_YEAR, 1);// 把日期设为当年第一天
        cd.roll(Calendar.DAY_OF_YEAR, -1);// 把日期回滚一天。
        int MaxYear = cd.get(Calendar.DAY_OF_YEAR);
        if (yearOfNumber == 1) {
            return -MaxYear;
        } else {
            return 1 - yearOfNumber;
        }
    }

    /**
     * 获取本年的第一天日期
     *
     * @return
     */    public String getCurrentYearFirst ( ) {
        int yearPlus = this.getYearPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, yearPlus);
        Date yearDay = currentDate.getTime( );
        DateFormat df = DateFormat.getDateInstance( );
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获得本年最后一天的日期
     */
    public String getCurrentYearEnd ( ) {
        Date date = new Date( );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        return years + "-12-31";
    }

    /**
     * 获得上年第一天的日期
     * @return
     */
    public String getPreviousYearFirst ( ) {
        Date date = new Date( );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);
        years_value--;
        return years_value + "-1-1";
    }

    /**
     * 获得上年最后一天的日期
      * @return
     */
    public String getPreviousYearEnd ( ) {
        weeks--;
        int yearPlus = this.getYearPlus( );
        GregorianCalendar currentDate = new GregorianCalendar( );
        currentDate.add(GregorianCalendar.DATE, yearPlus + MaxYear * weeks
                + (MaxYear - 1));
        Date yearDay = currentDate.getTime( );
        DateFormat df = DateFormat.getDateInstance( );
        String preYearDay = df.format(yearDay);
        return preYearDay;
    }

    /**
     * 获取本季度第一天
     * @param month
     * @return
     */
    public String getThisSeasonFirstTime (int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date( );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + start_month + "-" + start_days;
        return seasonDate;

    }


    /**
     * 获取本季度最后一天
     * @param month
     * @return
     */
    public String getThisSeasonFinallyTime (int month) {
        int array[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}};
        int season = 1;
        if (month >= 1 && month <= 3) {
            season = 1;
        }
        if (month >= 4 && month <= 6) {
            season = 2;
        }
        if (month >= 7 && month <= 9) {
            season = 3;
        }
        if (month >= 10 && month <= 12) {
            season = 4;
        }
        int start_month = array[season - 1][0];
        int end_month = array[season - 1][2];

        Date date = new Date( );
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);// 可以方便地修改日期格式
        String years = dateFormat.format(date);
        int years_value = Integer.parseInt(years);

        int start_days = 1;// years+"-"+String.valueOf(start_month)+"-1";//getLastDayOfMonth(years_value,start_month);
        int end_days = getLastDayOfMonth(years_value, end_month);
        String seasonDate = years_value + "-" + end_month + "-" + end_days;
        return seasonDate;

    }

    /**
     * 每月最后一号
     * @param year
     * @param month
     * @return
     */
    private int getLastDayOfMonth (int year, int month) {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            return 31;
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 0;
    }

    /**
     * 是否是瑞年
     * @param year
     * @return
     */
    public boolean isLeapYear (int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 是否是瑞年
     * @param year
     * @return
     */
    public boolean isLeapYear2 (int year) {
        return new GregorianCalendar( ).isLeapYear(year);
    }
}

