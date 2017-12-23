package com.dading.ssqs.controllar.withdrawdetail;

import com.dading.ssqs.bean.Constent;

/**
 * Created by lenovo on 2017/9/13.
 */
public class WithDrawFailFragment extends BaseWithDrawDetailFrament{

    @Override
    public int getType() {
        return 3;
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
