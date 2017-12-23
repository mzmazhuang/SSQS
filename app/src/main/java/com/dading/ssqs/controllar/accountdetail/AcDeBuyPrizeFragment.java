package com.dading.ssqs.controllar.accountdetail;

import com.dading.ssqs.bean.Constent;


/**
 * 创建者     zcl
 * 创建时间   2017/7/10 14:19
 * 描述	      ${賬戶明細中獎}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class AcDeBuyPrizeFragment extends BaseAccFragnment {

    @Override
    public int getType() {
        return 5;
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
