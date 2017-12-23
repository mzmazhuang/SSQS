package com.dading.ssqs.utils;

import android.util.Log;


public class LogUtil {
    private static boolean isTry = true;

    public static void util(String TAG, String msg) {
        if (isTry) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String TAG, String mes) {
        if (isTry) {
            Log.e(TAG, mes);
        }
    }

    public static void e(final String tag, final String message, final Throwable exception) {
        if (isTry) {
            Log.e(tag, message, exception);
        }
    }

    public static void e(final String tag, final Throwable e) {
        if (isTry) {
            e.printStackTrace();
            Log.e(tag, e.getMessage(), e);
        }
    }
}
