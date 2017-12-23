package com.dading.ssqs.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mazhuang on 2017/11/20.
 * 网络相关的工具类
 */

public class NetUtil {
    /**
     * Get the network user
     *
     * @param context
     * @return
     */

    private static NetworkInfo getNetworkInfo(Context context) {
        NetworkInfo ret = null;
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                ret = cm.getActiveNetworkInfo();
            } catch (Throwable ignored) {
            }
        }
        return ret;
    }


    /**
     * Check if there is any connectivity
     *
     * @param context
     * @return
     */

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }


    public static boolean isAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isAvailable());
    }


    /**
     * Check if there is any connectivity to a Wifi network
     *
     * @param context
     * @return
     */

    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }


    /**
     * Check if there is any connectivity to a mobile network
     *
     * @param context
     * @return
     */

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }


    public static String getNetworkTypeName(Context context) {
        String type = "";
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isConnected()) {
            type = info.getTypeName();
        }
        return type;
    }
}
