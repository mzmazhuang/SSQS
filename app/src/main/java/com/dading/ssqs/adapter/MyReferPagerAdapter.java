package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/12 16:44
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyReferPagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment>   list;
    private final ArrayList<String> listTab;

    public MyReferPagerAdapter (FragmentManager supportFragmentManager, ArrayList<Fragment> list, ArrayList<String> listTab) {
        super(supportFragmentManager);
        this.list = list;
        this.listTab = listTab;
    }

 /*   public MyReferPagerAdapter(FragmentManager context, ArrayList<Fragment> list, ArrayList<String> listTab) {
        this.context = context;
        this.list = list;
        this.listTab = listTab;
    }*/

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Fragment getItem (int position) {
        return list.get(position);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTab.get(position);
    }
}

