package com.dading.ssqs.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.adapter.AccountDetailVpAdapter;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.tabindicator.TabIndicator;

/**
 * 创建者     zcl
 * 创建时间   2017/7/10 14:18
 * 描述	      ${賬戶明細}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class AccountDetailActivity extends BaseActivity {

    @Bind(R.id.indicator)
    TabIndicator mIndicator;
    @Bind(R.id.viewpager)
    ViewPager    mViewpager;

    @Bind(R.id.top_title)
    TextView  mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_account_detail;
    }

    @Override
    public void initData ( ) {
        mTopIcon.setImageResource(R.mipmap.shuaxin);
        mTopIcon.setVisibility(View.VISIBLE);
        mTopTitle.setText(getString(R.string.account_detail));
        mViewpager.setAdapter(new AccountDetailVpAdapter(getSupportFragmentManager( ), this));
        mViewpager.setOffscreenPageLimit(6);
        mIndicator.setViewPager(mViewpager);
    }


    @OnClick({R.id.top_back, R.id.top_icon})
    public void OnClik (View v) {
        switch (v.getId( )) {
            case R.id.top_back:
                finish( );
                break;
            case R.id.top_icon:
                initData( );
                break;
        }
    }
}
