package com.dading.ssqs.interfaces;

import android.view.View;

import com.dading.ssqs.bean.BetBean;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/2 9:58
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class MyItemClickListern implements View.OnClickListener {
    public   BetBean bean;

    public MyItemClickListern( BetBean bean) {
        this.bean = bean;
    }

    @Override
    public abstract void onClick(View v);
}
