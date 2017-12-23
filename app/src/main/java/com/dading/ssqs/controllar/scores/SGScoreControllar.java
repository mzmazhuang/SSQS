package com.dading.ssqs.controllar.scores;

import android.view.View;

import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.UIUtils;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/29 9:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SGScoreControllar extends BaseSG {
    @Override
    public void setEmptyView ( ) {
        mSgList.getRefreshableView( ).setEmptyView(mEmpty);
        mEmpty.setVisibility(View.VISIBLE);
        mEmptyGB.setVisibility(View.GONE);
    }

    @Override
    public void setSend ( ) {
        super.setSend( );
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_FOOTBALL_SCORE);
        UIUtils.ReRecevice(mRecevice, Constent.JS_SG_SC_FITTER);
    }
}

