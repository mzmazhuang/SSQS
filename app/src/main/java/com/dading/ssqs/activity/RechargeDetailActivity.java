package com.dading.ssqs.activity;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.RechargeDetailAdapter;

import butterknife.Bind;
import butterknife.OnClick;
import tabindicator.TabIndicator;

/**
 * Created by lenovo on 2017/9/11.
 */
public class RechargeDetailActivity extends BaseActivity {
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Bind(R.id.indicator)
    TabIndicator mIndicator;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recharge_detail;
    }

    @Override
    protected void initData() {
        super.initData();
        mTopTitle.setText(getString(R.string.recharge_detail));
        mViewpager.setAdapter(new RechargeDetailAdapter(getSupportFragmentManager(), this));
        mIndicator.setViewPager(mViewpager);
        mViewpager.setOffscreenPageLimit(3);
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
