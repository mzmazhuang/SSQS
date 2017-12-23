package com.dading.ssqs.controllar.withdrawdetail;

/**
 * Created by lenovo on 2017/9/13.
 */
public class WithDrawSucFragment extends BaseWithDrawDetailFrament {

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public int getPgae() {
        return mPage;
    }

    @Override
    public int getLimit() {
        return 10;
    }
}
