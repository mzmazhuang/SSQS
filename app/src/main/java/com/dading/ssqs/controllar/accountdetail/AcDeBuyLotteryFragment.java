package com.dading.ssqs.controllar.accountdetail;

import com.dading.ssqs.bean.Constent;

/**
 * 创建者     zcl
 * 创建时间   2017/7/10 14:20
 * 描述	      ${賬戶明細購彩}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class AcDeBuyLotteryFragment extends BaseAccFragnment {

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
