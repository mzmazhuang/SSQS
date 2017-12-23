package com.dading.ssqs.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.controllar.withdrawdetail.WithDrawDetailAdapter;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/28 9:57
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WithDrawDentailActivity extends BaseActivity {
    private static final String TAG = "WithDrawDentailActivity";

    @Bind(R.id.top_title)
    TextView     mTopTitle;

    @Bind(R.id.indicator)
    TabIndicator mIndicator;
    @Bind(R.id.viewpager)
    ViewPager    mViewpager;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_recharge_detail;
    }

    @Override
    protected void initData ( ) {
        super.initData( );
        mTopTitle.setText(getString(R.string.with_draw_detail));
        mViewpager.setAdapter(new WithDrawDetailAdapter(getSupportFragmentManager(),this));
        mIndicator.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(3);
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v){
        finish();
    }

}
