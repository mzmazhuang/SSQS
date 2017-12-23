package com.dading.ssqs.controllar.recharge;

/**
 * Created by lenovo on 2017/9/11.
 */
public class RechargeSucFragment extends BaseRechargeFragnment {
    @Override
    public int getType() {
        return 2;
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
