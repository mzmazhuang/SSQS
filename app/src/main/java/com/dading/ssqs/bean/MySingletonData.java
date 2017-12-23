package com.dading.ssqs.bean;

import android.content.Context;

import java.util.Date;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/1 9:32
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MySingletonData extends Date{

    private static MySingletonData mInstance;
    private static Context         mCtx;

    private MySingletonData(Context context) {
        this.mCtx = context;
    }


    public static synchronized MySingletonData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingletonData(context.getApplicationContext());
        }
        return mInstance;
    }
}


