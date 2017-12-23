package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 10:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RedPeopleAdapter extends FragmentPagerAdapter {
    private final String[]            tabs;
    private final ArrayList<Fragment> data;

    public RedPeopleAdapter (FragmentManager supportFragmentManager, String[] tabs, ArrayList<Fragment> list) {
        super(supportFragmentManager);
        this.tabs = tabs;
        this.data = list;
    }

 /*   public RedPeopleAdapter(Context context, String[] tabs, ArrayList<View> list) {
        this.context = context;
        this.tabs = tabs;
        this.data = list;
    }*/


    @Override
    public int getCount ( ) {
        if (data != null)
            return data.size( );
        return 0;
    }

    @Override
    public CharSequence getPageTitle (int position) {

        return tabs[position];

    }

    @Override
    public Fragment getItem (int position) {
        return data.get(position);
    }
}
