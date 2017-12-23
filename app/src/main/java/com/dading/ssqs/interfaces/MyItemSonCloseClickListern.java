package com.dading.ssqs.interfaces;

import android.content.Context;
import android.view.View;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/2 9:58
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class MyItemSonCloseClickListern implements View.OnClickListener {
    private  Context context;
    public        int     postion;


    public MyItemSonCloseClickListern(Context context, int postion) {
        this.postion = postion;
        this.context = context;
    }

    @Override
    public abstract void onClick(View v);
}
