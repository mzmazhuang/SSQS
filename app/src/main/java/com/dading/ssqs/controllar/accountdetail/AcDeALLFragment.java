package com.dading.ssqs.controllar.accountdetail;

import com.dading.ssqs.bean.Constent;


/**
 * 创建者     zcl
 * 创建时间   2017/7/7 11:26
 * 描述	      ${账户记-全部}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class AcDeALLFragment extends BaseAccFragnment {

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
