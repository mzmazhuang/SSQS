package com.dading.ssqs.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.MyFollowAdapter;
import com.dading.ssqs.controllar.myfollow.MyFansControlalr;
import com.dading.ssqs.controllar.myfollow.MyFollowControlalr;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.tabindicator.TabIndicator;

public class MyCircleFollowActivity extends BaseActivity {

    private static final String TAG = "MyCircleFollowActivity";
    @Bind(R.id.my_follow_indicator)
    TabIndicator mMyFollowIndicator;
    @Bind(R.id.my_follow_vp)
    ViewPager mMyFollowVp;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private ArrayList<Fragment> mList;
    private ArrayList<String> mListTab;
    private MyFollowControlalr mFollowControlalr;
    private MyFansControlalr mFansControlalr;

    @Override
    protected void setUnDe() {
        try {
            UIUtils.UnReRecevice(mFollowControlalr.mRecevice);
            UIUtils.UnReRecevice(mFansControlalr.mRecevice);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_circle_follow;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.my_follow));
        mList = new ArrayList<>();
        mListTab = new ArrayList<>();
        mListTab.add("我的关注");
        mListTab.add("我的粉丝");

        mFollowControlalr = new MyFollowControlalr();
        mFansControlalr = new MyFansControlalr();
        mList.add(mFollowControlalr);
        mList.add(mFansControlalr);

        mMyFollowVp.setAdapter(new MyFollowAdapter(getSupportFragmentManager(), mList, mListTab));

        mMyFollowIndicator.setViewPager(mMyFollowVp);
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
