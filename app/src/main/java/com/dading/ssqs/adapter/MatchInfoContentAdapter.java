package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/12 14:50
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoContentAdapter extends PagerAdapter {
    private final Context           context;
    private final ArrayList<View>   data;
    private final ArrayList<String> dataTitle;

    public MatchInfoContentAdapter(ArrayList<View> dataView, ArrayList<String> dataIndicatorMatchContentTitle, Context context) {
        this.context = context;
        this.data = dataView;
        this.dataTitle = dataIndicatorMatchContentTitle;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = data.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataTitle.get(position);
    }

}
