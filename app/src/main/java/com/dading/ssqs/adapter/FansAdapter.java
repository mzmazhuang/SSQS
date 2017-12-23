package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/12 14:39
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FansAdapter extends BaseMePagerAdapter {
    private final Context           context;
    private final ArrayList<View>   data;
    private final ArrayList<String> listName;

    public FansAdapter(Context context, ArrayList<View> list, ArrayList<String> listName) {
        this.context = context;
        this.data = list;
        this.listName = listName;
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

    @Override
    public CharSequence getPageTitle(int position) {
        return listName.get(position);
    }
}
