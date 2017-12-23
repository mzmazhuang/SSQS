package com.dading.ssqs.controllar.recharge;

/**
 * Created by lenovo on 2017/9/11.
 */
public class RechargeFailFragment extends BaseRechargeFragnment {
    @Override
    public int getType() {
        return 3;
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
