package com.dading.ssqs.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/29 11:25
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TmtUtils {
    public static void midToast(Context context,String str, int showTime) {
        Toast toast = Toast.makeText(context, str, showTime);
        toast.setGravity(Gravity.CENTER, 0, 0);  //设置显示位置
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.YELLOW);     //设置字体颜色
        toast.show();
    }
}
