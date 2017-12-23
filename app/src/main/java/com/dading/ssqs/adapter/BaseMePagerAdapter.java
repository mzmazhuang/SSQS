package com.dading.ssqs.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/30 11:16
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class BaseMePagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return setSize();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = setView(position);
        if (view != null) {
            container.addView(view);
        }
        return view;
    }

    protected abstract View setView(int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    protected abstract int setSize();
}
