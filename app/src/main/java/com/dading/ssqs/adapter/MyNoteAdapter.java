package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/6 15:36
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

/**
 * 创建者     ZCL
 * 创建时间   2016/9/29 15:39
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyNoteAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> data;
    private final ArrayList<String>   tab;

    public MyNoteAdapter (android.support.v4.app.FragmentManager supportFragmentManager, ArrayList<Fragment> list, ArrayList<String> listTab) {
        super(supportFragmentManager);
        this.data = list;
        this.tab = listTab;
    }


    @Override
    public int getCount ( ) {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab.get(position);
    }

    @Override
    public Fragment getItem (int position) {
        return data.get(position);
    }
}

