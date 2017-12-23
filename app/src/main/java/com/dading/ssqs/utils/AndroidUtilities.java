package com.dading.ssqs.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.bean.Constent;

import java.lang.reflect.Method;
import java.util.Hashtable;

/**
 * Created by mazhuang on 2017/11/20.
 * 通用的工具类
 */

public class AndroidUtilities {
    private static final String TAG = "AndroidUtilities";

    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    private static Boolean isTablet = null;
    public static float density = 1;
    public static int statusBarHeight = 0;
    private static int keyboardHeight = 0;
    private static final Point displaySize = new Point();
    private static final DisplayMetrics displayMetrics = new DisplayMetrics();
    private static Point screenSize;

    static {
        density = SSQSApplication.getContext().getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }

    //获取屏幕宽高
    public static Point getRealScreenSize() {
        if (screenSize != null) {
            return screenSize;
        }
        Point size = new Point();
        try {
            WindowManager windowManager = (WindowManager) SSQSApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                windowManager.getDefaultDisplay().getRealSize(size);
            } else {
                try {
                    Method mGetRawW = Display.class.getMethod("getRawWidth");
                    Method mGetRawH = Display.class.getMethod("getRawHeight");
                    size.set((Integer) mGetRawW.invoke(windowManager.getDefaultDisplay()), (Integer) mGetRawH.invoke(windowManager.getDefaultDisplay()));
                } catch (Exception e) {
                    size.set(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getHeight());
                    Logger.e(TAG, e + "");
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e + "");
        }
        screenSize = size;
        return screenSize;
    }

    public static void checkDisplaySize() {
        try {
            Configuration configuration = SSQSApplication.getContext().getResources().getConfiguration();
            boolean usingHardwareInput = configuration.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO;
            WindowManager manager = (WindowManager) SSQSApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getMetrics(displayMetrics);
                    display.getSize(displaySize);
                    Logger.d(TAG, "display size = " + displaySize.x + " " + displaySize.y + " " + displayMetrics.xdpi + "x" + displayMetrics.ydpi);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e + "");
        }
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (delay == 0) {
            SSQSApplication.getHandler().post(runnable);
        } else {
            SSQSApplication.getHandler().postDelayed(runnable, delay);
        }
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        SSQSApplication.getHandler().removeCallbacks(runnable);
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static boolean checkIsLogin(int errorCode, Context context) {
        if (403 == errorCode) {
            UIUtils.SendReRecevice(Constent.LOADING_ACTION);
            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manger = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            if (info != null) {
                return info.isConnected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static void disableShowInput(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {//TODO: handle exception
            }
        }
    }
}
