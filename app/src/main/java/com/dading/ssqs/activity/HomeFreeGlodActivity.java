package com.dading.ssqs.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.adapter.FreeTaskAdapter;
import com.dading.ssqs.controllar.task.AchieveTaskControllar;
import com.dading.ssqs.controllar.task.FreeGlodTaskControllar;
import com.dading.ssqs.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tabindicator.TabIndicator;


/**
 * 创建者     ZCL
 * 创建时间   2016/7/15 12:08
 */
public class HomeFreeGlodActivity extends BaseActivity {
    
    @Bind(R.id.free_glod_indicator)
    TabIndicator mFreeGlodIndicator;
    @Bind(R.id.free_glod_viewpager)
    ViewPager mFreeGlodViewpager;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private FreeGlodTaskControllar mFreeGlodTask;
    private AchieveTaskControllar mMAchieveTask;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home_free_glod;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.free_get_glod));
        mFreeGlodTask = new FreeGlodTaskControllar();
        mMAchieveTask = new AchieveTaskControllar();
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(mFreeGlodTask);
        list.add(mMAchieveTask);
        ArrayList<String> listTab = new ArrayList<>();
        listTab.add("新手任务");
        listTab.add("成就奖励");

        mFreeGlodViewpager.setAdapter(new FreeTaskAdapter(getSupportFragmentManager(), list, listTab));
        mFreeGlodIndicator.setViewPager(mFreeGlodViewpager);
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
