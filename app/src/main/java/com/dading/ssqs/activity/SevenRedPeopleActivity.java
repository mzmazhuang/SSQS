package com.dading.ssqs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.RedPeopleAdapter;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.controllar.redpeople.RedPeopleAllResult;
import com.dading.ssqs.controllar.redpeople.RedPeopleHBigSmall;
import com.dading.ssqs.controllar.redpeople.RedPeopleHLost;
import com.dading.ssqs.controllar.redpeople.RedPeopleHResult;
import com.dading.ssqs.controllar.redpeople.RedPeopleNowLost;
import com.dading.ssqs.controllar.redpeople.RedPeopleSmallBig;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 10:13
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SevenRedPeopleActivity extends BaseActivity {
    private static final String TAG = "SevenRedPeopleActivity";
    @Bind(R.id.seven_red_prople_indicator)
    TabIndicator mSevenRedPropleIndicator;
    @Bind(R.id.seven_red_prople_viewpager)
    ViewPager mSevenRedPropleViewpager;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private int mPageID = 0;
    private SavantFollowRecevice mRecevice;
    private ArrayList<Fragment> mList;
    private String[] mTabs;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_seven_red_people;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.seven_red_people));
        mRecevice = new SavantFollowRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.SAVANT_FOLLOW_TAG);
        /**
         * a)	请求地址：
         /v1.0/expert/day/hot/{type}/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         Type:赔率类型ID
         1-	全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
         auth_token：登陆后加入请求头
         */
        mTabs = new String[]{"全场赛果", "当前让球", "全场大小", "半场赛果", "半场让球", "半场大小"};
        mList = new ArrayList<>();
        mList.add(new RedPeopleAllResult());
        mList.add(new RedPeopleNowLost());
        mList.add(new RedPeopleSmallBig());
        mList.add(new RedPeopleHResult());
        mList.add(new RedPeopleHLost());
        mList.add(new RedPeopleHBigSmall());

        mSevenRedPropleViewpager.setAdapter(new RedPeopleAdapter(getSupportFragmentManager(), mTabs, mList));
        mSevenRedPropleIndicator.setViewPager(mSevenRedPropleViewpager);
        mSevenRedPropleViewpager.setCurrentItem(mPageID);
    }

    @Override
    protected void initListener() {
        mSevenRedPropleViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPageID = position;
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


    private class SavantFollowRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.util(TAG, "进入七天红人广播------------------------------");
            initData();
        }
    }

}
