package com.dading.ssqs.controllar.guessball;

import android.view.View;

import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.controllar.scores.BaseJs;
import com.dading.ssqs.utils.UIUtils;

/**
 * 创建者     zcl
 * 创建时间   2017/10/13$ 12:03$
 * 描述	      $${TODO}$$
 * <p>
 * 更新者     $$Author$$
 * 更新时间   $$Date$$
 * 更新描述   $${TODO}$$
 */
public class JSGuessControllar extends BaseJs {
    @Override
    public void setEmptyView ( ) {
        mJsList.getRefreshableView( ).setEmptyView(mEmptyGB);
        mEmpty.setVisibility(View.GONE);
        mEmptyGB.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSend ( ) {
        super.setSend( );
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_FOOTBALL);
    }
}
