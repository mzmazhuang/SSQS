package com.dading.ssqs.controllar.recharge;

/**
 * Created by lenovo on 2017/9/11.
 */
public class RechargeAllFragment extends BaseRechargeFragnment {

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public int getLimit() {
        return 10;
    }
}
