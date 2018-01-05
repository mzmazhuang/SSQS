package com.dading.ssqs.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

    /**
     * 获取程序版本名称u.
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void hideKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive()) {
            return;
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Drawable createListSelectorDrawable(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            int[] attrs = new int[]{android.R.attr.selectableItemBackground};
            TypedArray ta = context.obtainStyledAttributes(attrs);
            Drawable drawableFromTheme = ta.getDrawable(0);
            ta.recycle();
            return drawableFromTheme;
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(0x0f000000));
            stateListDrawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(0x0f000000));
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(0x0f000000));
            stateListDrawable.addState(new int[]{}, new ColorDrawable(0x00000000));
            return stateListDrawable;
        }
    }

    private static final Paint maskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public static Drawable createBarSelectorDrawable() {
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable maskDrawable = null;
            maskPaint.setColor(0xffffffff);
            maskDrawable = new Drawable() {
                @Override
                public void draw(Canvas canvas) {
                    android.graphics.Rect bounds = getBounds();
                    canvas.drawCircle(bounds.centerX(), bounds.centerY(), AndroidUtilities.dp(18), maskPaint);
                }

                @Override
                public void setAlpha(int alpha) {

                }

                @Override
                public void setColorFilter(ColorFilter colorFilter) {

                }

                @Override
                public int getOpacity() {
                    return PixelFormat.UNKNOWN;
                }
            };
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{new int[]{}},
                    new int[]{0xff000000}
            );
            return new RippleDrawable(colorStateList, null, maskDrawable);
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(0x0f000000));
            stateListDrawable.addState(new int[]{android.R.attr.state_focused}, new ColorDrawable(0x0f000000));
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(0x0f000000));
            stateListDrawable.addState(new int[]{}, new ColorDrawable(0x00000000));
            return stateListDrawable;
        }
    }
}
