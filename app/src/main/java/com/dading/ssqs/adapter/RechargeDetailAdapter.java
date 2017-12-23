package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.FragmentInfoBean;
import com.dading.ssqs.controllar.recharge.RechargeAllFragment;
import com.dading.ssqs.controllar.recharge.RechargeFailFragment;
import com.dading.ssqs.controllar.recharge.RechargeSucFragment;
import com.dading.ssqs.controllar.recharge.RechargeWaitCheckedFragment;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/9/11.
 */
public class RechargeDetailAdapter extends DetailAdapter {

    private ArrayList<FragmentInfoBean> mList;

    public RechargeDetailAdapter (FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public ArrayList<FragmentInfoBean> getList ( ) {
        mList = new ArrayList<>( );
        mList.add(new FragmentInfoBean(context.getString(R.string.all), RechargeAllFragment.class));
        mList.add(new FragmentInfoBean(context.getString(R.string.recharge_suc_title), RechargeSucFragment.class));
        mList.add(new FragmentInfoBean(context.getString(R.string.fail), RechargeFailFragment.class));
        mList.add(new FragmentInfoBean(context.getString(R.string.waite_checked), RechargeWaitCheckedFragment.class));
        return mList;
    }
}
