package com.dading.ssqs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/12 15:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScrollAdapterInner extends FragmentPagerAdapter {


    private final ArrayList<Fragment> dataFra;
    private final ArrayList<String>   dataIndicatorGuess;


    public ScrollAdapterInner (FragmentManager fragmentManager, ArrayList<Fragment> dataFragment, ArrayList<String> dataIndicatorGuess) {
        super(fragmentManager);
        this.dataFra = dataFragment;
        this.dataIndicatorGuess = dataIndicatorGuess;
    }

    @Override
    public int getCount ( ) {
        return dataFra.size( );
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return dataIndicatorGuess.get(position);
    }

    @Override
    public Fragment getItem (int position) {
        return dataFra.get(position);
    }
}
