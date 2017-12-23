package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.adapter.FansAdapter;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.controllar.savantfans.HeFansControllar;
import com.dading.ssqs.controllar.savantfans.HeFollowControllar;
import com.dading.ssqs.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/8 14:27
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FansInfoActivity extends BaseActivity {
    private static final String TAG = "FansInfoActivity";
    @Bind(R.id.fans_info_indicator)
    TabIndicator mFansInfoIndicator;
    @Bind(R.id.fans_info_viewpager)
    ViewPager mFansInfoViewpager;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private ArrayList<String> mListName;
    private ArrayList<View> mList;
    private int mPageId = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.fans_info_activity;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.he_follow));
        Intent intent = getIntent();

        String savantId = intent.getStringExtra(Constent.SAVANT_ID);

        mListName = new ArrayList<>();
        mListName.add("他的关注");
        mListName.add("他的粉丝");

        mList = new ArrayList<>();

        HeFollowControllar heFollowControllar = new HeFollowControllar(this, savantId);
        mList.add(heFollowControllar.mRootView);
        HeFansControllar heFansControllar = new HeFansControllar(this, savantId);
        mList.add(heFansControllar.mRootView);

        FansAdapter adapter = new FansAdapter(this, mList, mListName);
        mFansInfoViewpager.setAdapter(adapter);
        mFansInfoIndicator.setViewPager(mFansInfoViewpager);
        mFansInfoViewpager.setCurrentItem(mPageId);
    }

    @Override
    protected void initListener() {
        mFansInfoViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPageId = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            default:
                break;
        }
    }
}
