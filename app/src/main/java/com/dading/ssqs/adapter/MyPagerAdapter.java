package com.dading.ssqs.adapter;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/12 11:07
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dading.ssqs.base.BaseTabsContainer;

import java.util.ArrayList;

/**
 * 创建者	 张传榴
 * 创建时间	2016-4-7 下午3:27:30
 * 描述 	用来给内容区域viewpage设置显示内容
 * 版本		$Rev$
 * 更新者	$Author$
 * 更新时间	$Data$
 * 更新描述
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> baseDataControllar;


    public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> baseDataControllar) {
        super(fragmentManager);

        this.baseDataControllar = baseDataControllar;
    }

    @Override
    public int getCount() {
        return baseDataControllar.size();
    }

    @Override
    public Fragment getItem(int position) {
        return baseDataControllar.get(position);
    }
}
