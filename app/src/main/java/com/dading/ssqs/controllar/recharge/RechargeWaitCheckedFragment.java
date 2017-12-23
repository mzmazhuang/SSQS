package com.dading.ssqs.controllar.recharge;

/**
 * Created by lenovo on 2017/9/11.
 */
public class RechargeWaitCheckedFragment extends BaseRechargeFragnment {
    @Override
    public int getType() {
        return 4;
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