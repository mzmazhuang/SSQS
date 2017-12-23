package com.dading.ssqs.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.MyCollectVpAdapter;
import com.dading.ssqs.controllar.mycollect.MyCollectControllar;
import com.dading.ssqs.controllar.mycollect.MySnsCollectControllar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 17:01
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchCollectActivity extends BaseActivity {
    private static final String TAG = "MatchCollectActivity";
    @Bind(R.id.indicator)
    TabIndicator mIndicator;
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private int mPostion;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        mViewpager.setCurrentItem(mPostion);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_match_collect;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.match_collect));
        MyCollectControllar controllar = new MyCollectControllar();
        MySnsCollectControllar controllar1 = new MySnsCollectControllar();
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(controllar);
        list.add(controllar1);
        ArrayList<String> listTitle = new ArrayList<>();
        listTitle.add("我的收藏");
        listTitle.add("社区收藏");

        MyCollectVpAdapter vpAdapter = new MyCollectVpAdapter(getSupportFragmentManager(), list, listTitle);
        mViewpager.setAdapter(vpAdapter);
        mIndicator.setViewPager(mViewpager);
        mPostion = 0;
        mViewpager.setCurrentItem(mPostion);
    }

    @Override
    protected void initListener() {
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPostion = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
