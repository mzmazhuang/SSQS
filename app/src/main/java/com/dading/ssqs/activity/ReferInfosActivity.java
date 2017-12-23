package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.SavantInfoVpIndicatorAdapter;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.controllar.savantrefer.ReferInfoAllResult;
import com.dading.ssqs.controllar.savantrefer.ReferInfoHBigSmall;
import com.dading.ssqs.controllar.savantrefer.ReferInfoHLost;
import com.dading.ssqs.controllar.savantrefer.ReferInfoHResult;
import com.dading.ssqs.controllar.savantrefer.ReferInfoNowLost;
import com.dading.ssqs.controllar.savantrefer.ReferInfoSmallBig;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/9 16:24
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ReferInfosActivity extends BaseActivity {
    @Bind(R.id.savant_info_refers_indicator)
    TabIndicator mSavantInfoRefersIndicator;
    @Bind(R.id.savant_info_refers_viewpager)
    ViewPager    mSavantInfoRefersViewpager;
    @Bind(R.id.top_title)
    TextView     mTopTitle;
    private ArrayList<String> mTabs;
    private ArrayList<View> mList;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_savant_info_refers;
    }

    @Override
    protected void initData() {

        mTopTitle.setText(getString(R.string.referr));
        Intent intent = getIntent();
        String savantID = intent.getStringExtra(Constent.SAVANT_ID);

        // String[] tabs = { "全场赛果"，"当前让球", "全场大小","半场赛果","半场让球","半场大小"};
        mTabs = new ArrayList<>();
        mTabs.add("全场赛果");
        mTabs.add("当前让球");
        mTabs.add("全场大小");
        mTabs.add("半场赛果");
        mTabs.add("半场让球");
        mTabs.add("半场大小");

        mList = new ArrayList<>();

        mList.add(new ReferInfoAllResult(this, savantID).mRootView);
        mList.add(new ReferInfoNowLost(this, savantID).mRootView);
        mList.add(new ReferInfoSmallBig(this, savantID).mRootView);
        mList.add(new ReferInfoHResult(this, savantID).mRootView);
        mList.add(new ReferInfoHLost(this, savantID).mRootView);
        mList.add(new ReferInfoHBigSmall(this, savantID).mRootView);


        mSavantInfoRefersViewpager.setAdapter(new SavantInfoVpIndicatorAdapter(this, mList, mTabs));
        mSavantInfoRefersIndicator.setViewPager(mSavantInfoRefersViewpager);
    }

     @OnClick({R.id.top_back})
     public void OnClik(View v){
         finish();
     }
}
