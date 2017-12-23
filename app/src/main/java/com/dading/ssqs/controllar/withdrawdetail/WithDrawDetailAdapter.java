package com.dading.ssqs.controllar.withdrawdetail;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.DetailAdapter;
import com.dading.ssqs.bean.FragmentInfoBean;
import com.dading.ssqs.controllar.recharge.RechargeAllFragment;
import com.dading.ssqs.controllar.recharge.RechargeFailFragment;
import com.dading.ssqs.controllar.recharge.RechargeSucFragment;
import com.dading.ssqs.controllar.recharge.RechargeWaitCheckedFragment;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/9/13.
 */
public class WithDrawDetailAdapter extends DetailAdapter {
    private ArrayList<FragmentInfoBean> mList;

    public WithDrawDetailAdapter (FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public ArrayList<FragmentInfoBean> getList ( ) {
        mList = new ArrayList<>( );
        mList.add(new FragmentInfoBean(context.getString(R.string.all), WithDrawAllFragment.class));
        mList.add(new FragmentInfoBean(context.getString(R.string.sucess), WithDrawSucFragment.class));
        mList.add(new FragmentInfoBean(context.getString(R.string.fail), WithDrawFailFragment.class));
        mList.add(new FragmentInfoBean(context.getString(R.string.waite_checked), WithDrawWaitCheckedFragment.class));
        return mList;
    }
}
