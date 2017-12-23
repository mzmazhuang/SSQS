package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/10 10:54
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyStoreAdapter extends BaseMePagerAdapter {
    private final Context         context;
    private final ArrayList<View> data;

    public MyStoreAdapter(Context context, ArrayList<View> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    protected View setView(int position) {

        return data.get(position);
    }

    @Override
    protected int setSize() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }
}
