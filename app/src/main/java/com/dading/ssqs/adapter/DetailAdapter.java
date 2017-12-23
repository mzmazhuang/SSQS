package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dading.ssqs.bean.FragmentInfoBean;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/9/13.
 */
public abstract class DetailAdapter extends FragmentPagerAdapter {
    private ArrayList<FragmentInfoBean> mList;
    public  Context                     context;

    public DetailAdapter (FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mList = getList( );
    }


    @Override
    public Fragment getItem (int position) {
        try {
            return mList.get(position).getFragment( ).newInstance( );
        } catch (InstantiationException e) {
            e.printStackTrace( );
        } catch (IllegalAccessException e) {
            e.printStackTrace( );
        }
        return null;
    }

    public abstract ArrayList<FragmentInfoBean> getList ( );

    @Override
    public int getCount ( ) {
        return mList.size( );
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return mList.get(position).getTitle( );
    }

    /**
     * isViewFromObject次方法要么不重写 要么返回true 返回ture几个界面一起显示 false newInstance会抛出异常
     */
   /* @Override
    public boolean isViewFromObject (View view, Object object) {
        return true;
    }*/
}
