package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/6 15:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FreeTaskAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> data;
    private ArrayList<String>   dataTab;

    public FreeTaskAdapter (FragmentManager supportFragmentManager, ArrayList<Fragment> list, ArrayList<String> listTab) {
        super(supportFragmentManager);
        this.data = list;
        this.dataTab = listTab;
    }


    @Override
    public int getCount ( ) {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataTab.get(position);
    }

    @Override
    public Fragment getItem (int position) {
        return data.get(position);
    }
}
