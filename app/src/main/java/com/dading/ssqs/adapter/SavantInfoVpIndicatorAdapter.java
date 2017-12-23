package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/9 16:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantInfoVpIndicatorAdapter extends BaseMePagerAdapter {
    private final Context         context;
    private final ArrayList<View> data;
    private final ArrayList<String>           dataTabs;

    public SavantInfoVpIndicatorAdapter(Context context, ArrayList<View> list, ArrayList<String> listTabs) {
        this.context = context;
        this.data = list;
        this.dataTabs = listTabs;
    }

    @Override
    protected View setView(int position) {
        View view = data.get(position);
        return view;
    }

    @Override
    protected int setSize() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataTabs.get(position);
    }
}
