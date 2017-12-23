package com.dading.ssqs.interfaces;

import android.view.View;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/21 17:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class MyItemClickListenerPostion implements View.OnClickListener {
    public  int     p;

    public MyItemClickListenerPostion(int postion) {
        this.p = postion;
    }

    @Override
    public abstract void onClick(View v);
}
