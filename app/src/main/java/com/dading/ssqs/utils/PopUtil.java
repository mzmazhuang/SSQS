package com.dading.ssqs.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/13 17:44
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PopUtil {

    public static void closePop(PopupWindow pop) {
        if (pop != null && pop.isShowing())
            pop.dismiss();
    }


    public static PopupWindow popuMake(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //设置PopupWindow的背景图片是透明的颜色图片
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        mPopupWindow.getBackground().setAlpha(150);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(true);
        return mPopupWindow;
    }

    public static PopupWindow popuMakeFalse(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.
                MATCH_PARENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        mPopupWindow.getBackground().setAlpha(100);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(false);
        return mPopupWindow;
    }

    public static PopupWindow popuMakeFalseW(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.
                MATCH_PARENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopupWindow.getBackground().setAlpha(150);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(false);
        return mPopupWindow;
    }

    public static PopupWindow popuMakeWrap(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.
                WRAP_CONTENT, true);
        //设置PopupWindow的背景图片是透明的颜色图片
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
        mPopupWindow.getBackground().setAlpha(200);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(true);
        return mPopupWindow;
    }

    public static PopupWindow popuMakemm(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.
                MATCH_PARENT, true);
        //设置PopupWindow的背景图片是透明的颜色图片
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.getBackground().setAlpha(200);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(true);
        return mPopupWindow;
    }

    public static PopupWindow popuMakeWwf(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.
                WRAP_CONTENT, false);
        //设置PopupWindow的背景图片是透明的颜色图片
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.getBackground().setAlpha(200);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(false);
        return mPopupWindow;
    }

    public static PopupWindow popuMakeWw(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.
                WRAP_CONTENT, false);
        //设置PopupWindow的背景图片是透明的颜色图片
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.getBackground().setAlpha(200);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(true);
        return mPopupWindow;
    }

    public static PopupWindow popuMakeMwf(View lv) {
        /**
         * 创建PopupWindow
         * 参数一:要显示的View对象
         * 参数二:宽度
         * 参数三:高度
         * 参数四:是否可以获取焦点
         */
        PopupWindow mPopupWindow = null;
        mPopupWindow = new PopupWindow(lv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.
                WRAP_CONTENT, false);
        //设置PopupWindow的背景图片是透明的颜色图片
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.getBackground().setAlpha(200);//设置半透明给其他区域
        //设置点击PopupWindow外面的位置可隐藏PopupWindow窗口
        mPopupWindow.setOutsideTouchable(false);
        return mPopupWindow;
    }
}
