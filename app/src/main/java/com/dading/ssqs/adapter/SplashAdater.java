package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/19 9:58
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SplashAdater extends FragmentPagerAdapter {


    private final ArrayList<Fragment> list;

    public SplashAdater (FragmentManager fm, ArrayList<Fragment> list) {
        super(fm);
        this.list = list;
    }


    @Override
    public Fragment getItem (int position) {
        return list.get(position);
    }

    @Override
    public int getCount ( ) {
        return list.size();
    }
}
