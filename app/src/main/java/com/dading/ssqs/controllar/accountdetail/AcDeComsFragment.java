package com.dading.ssqs.controllar.accountdetail;

/**
 * Created by lenovo on 2017/7/10.
 */
public class AcDeComsFragment extends BaseAccFragnment {

    @Override
    public int getType() {
        return 6;
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
