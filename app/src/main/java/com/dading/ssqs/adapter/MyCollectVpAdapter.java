package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/22 10:50
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyCollectVpAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> data;
    private final ArrayList<String>   dataTitle;

    public MyCollectVpAdapter (android.support.v4.app.FragmentManager fm, ArrayList<Fragment> list, ArrayList<String> listTitle) {
        super(fm);
        this.data = list;
        this.dataTitle = listTitle;
    }

  /*  public MyCollectVpAdapter(FragmentManager context, ArrayList<Fragment> list, ArrayList<String> listTitle) {
        this.context = context;
        this.data = list;
        this.dataTitle = listTitle;
    }*/

    @Override
    public int getCount ( ) {
        if (data != null) {
            return data.size( );
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return dataTitle.get(position);
    }

    @Override
    public Fragment getItem (int position) {
        return data.get(position);
    }
}
