package com.dading.ssqs.controllar.details;

import android.view.View;

import com.dading.ssqs.bean.JCScorebean;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/19 10:17
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class MyItemScoreClickListern implements View.OnClickListener {
    public JCScorebean.ListEntity.ItemsEntity bean;

    public MyItemScoreClickListern(JCScorebean.ListEntity.ItemsEntity bean) {
        this.bean = bean;
    }

    @Override
    public abstract void onClick(View v);
}
