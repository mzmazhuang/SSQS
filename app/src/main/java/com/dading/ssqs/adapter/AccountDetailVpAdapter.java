package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.FragmentInfoBean;
import com.dading.ssqs.controllar.accountdetail.AcDeALLFragment;
import com.dading.ssqs.controllar.accountdetail.AcDeBuyLotteryFragment;
import com.dading.ssqs.controllar.accountdetail.AcDeBuyPrizeFragment;
import com.dading.ssqs.controllar.accountdetail.AcDeComsFragment;
import com.dading.ssqs.controllar.accountdetail.AcDeOtherFragment;
import com.dading.ssqs.controllar.accountdetail.AcDeRechageFragment;
import com.dading.ssqs.controllar.accountdetail.AcDeWithDrawFragment;

import java.util.ArrayList;

/**
 * 创建者     zcl
 * 创建时间   2017/7/7 11:27
 * 描述	      ${账户明细适配器}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class AccountDetailVpAdapter extends DetailAdapter {
    private static final String TAG = "AccountDetailVpAdapter";

    public AccountDetailVpAdapter (FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public ArrayList<FragmentInfoBean> getList ( ) {
    ArrayList<FragmentInfoBean> Fragments = new ArrayList<>( );
        Fragments.add(new FragmentInfoBean(context.getString(R.string.all), AcDeALLFragment.class));
        Fragments.add(new FragmentInfoBean(context.getString(R.string.recharge), AcDeRechageFragment.class));
        Fragments.add(new FragmentInfoBean(context.getString(R.string.with_draw), AcDeWithDrawFragment.class));
        Fragments.add(new FragmentInfoBean(context.getString(R.string.lottery_buy), AcDeBuyLotteryFragment.class));
        Fragments.add(new FragmentInfoBean(context.getString(R.string.win_prize), AcDeBuyPrizeFragment.class));
        Fragments.add(new FragmentInfoBean(context.getString(R.string.commission), AcDeComsFragment.class));
        Fragments.add(new FragmentInfoBean(context.getString(R.string.other), AcDeOtherFragment.class));
        return Fragments;
    }

}
