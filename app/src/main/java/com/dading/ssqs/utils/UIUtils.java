package com.dading.ssqs.utils;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.text.Editable;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;

import com.dading.ssqs.SSQSApplication;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * @author Administrator
 * @version $Rev: 40 $
 * @time 2015-7-15 上午10:59:15
 * @des 和ui相关的工具类 主要功能获取上下文
 * @updateAuthor $Author: admin $
 * @updateDate $Date: 2015-07-19 11:26:02 +0800 (星期日, 19 七月 2015) $
 * @updateDes TODO
 */
public class UIUtils {
    /**
     * 得到上下文
     */
    public static Context getContext() {
        return SSQSApplication.getContext();
    }

    /**
     * 注册广播
     */
    public static void ReRecevice(BroadcastReceiver receiver, String action) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(action);
        UIUtils.getContext().registerReceiver(receiver, filter);
    }

    /**
     * 解除广播
     */
    public static void UnReRecevice(BroadcastReceiver receiver) {
        UIUtils.getContext().unregisterReceiver(receiver);
    }

    /**
     * Fsong广播
     */
    public static void SendReRecevice(String action) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(action);
        UIUtils.getContext().sendBroadcast(intent);
    }

    public void isNullClear(List l) {
        if (l != null)
            l.clear();
    }

    public void isNullClearMap(Map m) {
        if (m != null)
            m.clear();
    }

    public void isDissmiss(PopupWindow p) {
        if (p != null)
            p.dismiss();
    }

    /**
     * 得到Sputils
     */
    public static SpUtils getSputils() {
        return SSQSApplication.getSpUtils();
    }

    /**
     * 两次点击按钮之间的点击间隔不能少于1000毫秒
     */
    private static final int MIN_CLICK_DELAY_TIME = 1500;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    /**
     * 得到Resouce对象
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 得到String.xml中的字符串数组
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程id
     */
    public static long getMainThreadid() {
        return SSQSApplication.getMainTreadId();
    }

    /**
     * 得到主线程Handler
     */
    public static Handler getMainThreadHandler() {
        return SSQSApplication.getHandler();
    }

    /**
     * 安全的执行一个任务
     */
    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();

        if (curThreadId == getMainThreadid()) {// 如果当前线程是主线程
            task.run();
        } else {// 如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }

    }

    /**
     * 延迟执行任务
     */
    public static void postTaskDelay(Runnable task, int delayMillis) {
        getMainThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 移除任务
     */
    public static void removeTask(Runnable task) {
        if (task != null)
            getMainThreadHandler().removeCallbacks(task);
    }

    /**
     * 移除任务
     */
    public static void removeTaskAll(Runnable task) {
        if (task != null)
            getMainThreadHandler().removeCallbacksAndMessages(null);
    }

    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        // px/dip = density;
        float density = getResource().getDisplayMetrics().density;
        int px = (int) (dip * density + .5f);
        return px;
    }

    /**
     * px-->dip
     */
    public static int px2Dip(int px) {
        // px/dip = density;
        float density = getResource().getDisplayMetrics().density;
        int dip = (int) (px / density + .5f);
        return dip;
    }

    /**
     * 判断手机格式是否正确
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证身份证号是否正确
     * param s
     * return
     */
    public static boolean isID(Editable s) {
        String regex = "^(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])$";
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile(regex);
        m = p.matcher(s);
        b = m.matches();
        return b;
    }

    /**
     * 随机字符串
     * 0-62之间的随机整数
     * nextInt（）无范围的随机数
     * nextInt（n）从0到n的随机数
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void hideKeyBord(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
