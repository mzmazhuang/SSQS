package com.dading.ssqs.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.CasionActivity;
import com.dading.ssqs.adapter.MyPagerAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.controllar.GuessBallControllarAll;
import com.dading.ssqs.controllar.ScoreControllar;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 12:32
 * 描述	      ${}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${}
 */
public class MainContentFragement extends BaseFragnment implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "MainContentFragement";
    @Bind(R.id.noscroeViewpager)
    NoScrollViewPager mNoScoreViewpager;
    @Bind(R.id.main_home)
    RadioButton mHome;
    @Bind(R.id.main_score)
    RadioButton mScore;
    @Bind(R.id.main_guessball)
    RadioButton mGuessBall;
    @Bind(R.id.main_referr)
    RadioButton mReferr;
    @Bind(R.id.main_my)
    RadioButton mMy;
    @Bind(R.id.fragment_bottom_rg)
    RadioGroup mRg;

    private ArrayList<Fragment> mBaseDataControllar;
    public int mMCurrButtonId = 0;
    private HomeRecevice mRecevice;
    private MyPagerAdapter mAdapter;
    public PreferentialActivitiesFragment mPreferentialActivitiesFragment;
    public GuessTheBallFragment guessTheBallFragment;
    public ScoreFragment scoreFragment;
    public MyFragment myFragment;
    private GuessBallControllarAll mGuessBallControllarAll;
    private ArrayList<RadioButton> mList;

    private LoadingBean bean;

    @Override
    protected int setLayout() {
        return R.layout.fragment_content;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        try {
            UIUtils.UnReRecevice(mRecevice);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        mRecevice = new HomeRecevice();

        UIUtils.ReRecevice(mRecevice, Constent.LOADING_HOME);
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_GUESS_BALL);
        UIUtils.ReRecevice(mRecevice, Constent.PREFERENTIAL_ACTIVITIES);
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_MY);
        UIUtils.ReRecevice(mRecevice, Constent.HOME_BALL);
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_CASINO);
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_SCORE);

        mList = new ArrayList<>();
        mList.add(mHome);
        mList.add(mScore);
        mList.add(mGuessBall);
        mList.add(mReferr);
        mList.add(mMy);

        setData();
        //让首页默认为首页界面
        mNoScoreViewpager.setCurrentItem(0);
        mNoScoreViewpager.setOffscreenPageLimit(5);
        //设置默认选中首页图标
        mHome.setChecked(true);
        super.initData();
    }

    private void setData() {
        //猜球界面
        guessTheBallFragment = new GuessTheBallFragment();
        //优惠
        mPreferentialActivitiesFragment = new PreferentialActivitiesFragment();
        //比分
        scoreFragment = new ScoreFragment();
        //我的
        myFragment = new MyFragment();
        //首页
        mGuessBallControllarAll = new GuessBallControllarAll();

        mBaseDataControllar = new ArrayList<>();
        mBaseDataControllar.add(mGuessBallControllarAll);
        mBaseDataControllar.add(mPreferentialActivitiesFragment);
        mBaseDataControllar.add(guessTheBallFragment);
        mBaseDataControllar.add(scoreFragment);
        mBaseDataControllar.add(myFragment);

        //为viewpager赋值
        mAdapter = new MyPagerAdapter(this.getFragmentManager(), mBaseDataControllar);
        mNoScoreViewpager.setAdapter(mAdapter);
    }

    /**
     * 設置按鈕底部監聽
     */
    @Override
    public void initListener() {
        super.initListener();
        mRg.setOnCheckedChangeListener(this);
    }


    private void isUserFul(final boolean isRefreshGlods) {
        SSQSApplication.apiClient(0).getUserInfo(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    bean = (LoadingBean) result.getData();

                    if (bean != null && result.getStatus()) {
                        UIUtils.getSputils().putString(Constent.GLODS, bean.banlance + "");

                        if (isRefreshGlods) {
                            UIUtils.SendReRecevice(Constent.REFRESH_MONY);
                        } else {
                            UIUtils.getSputils().putString(Constent.TOKEN, bean.authToken);

                            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);
                            UIUtils.getSputils().putInt(Constent.IS_VIP, bean.isVip);
                            UIUtils.getSputils().putBoolean(Constent.USER_TYPE, bean.userType == 3);

                            UIUtils.getSputils().putString(Constent.DIAMONDS, bean.diamond + "");

                            //发送广播
                            UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        }
                    }
                } else {
                    if (!AndroidUtilities.INSTANCE.checkIsLogin(result.getErrno(), mContent)) {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected void setUnDe() {
        UIUtils.UnReRecevice(mRecevice);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mRg.setClickable(false);
        if (guessTheBallFragment != null) {
            guessTheBallFragment.fragmentPause();
        }
        if (mGuessBallControllarAll != null) {
            mGuessBallControllarAll.pause();
        }

        switch (checkedId) {
            case R.id.main_home:
                setChecked(0);
                mMCurrButtonId = 0;
                if (mGuessBallControllarAll != null) {
                    mGuessBallControllarAll.resume();
                }
                break;
            case R.id.main_referr:
                mMCurrButtonId = 1;
                setChecked(3);
                break;
            case R.id.main_guessball:
                if (guessTheBallFragment != null) {
                    guessTheBallFragment.fragmentResume();
                }
                mMCurrButtonId = 2;
                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                UIUtils.SendReRecevice(Constent.HOME_BALL);
                UIUtils.SendReRecevice(Constent.GQ_RECEVICE);
                setChecked(2);
                break;
            case R.id.main_score:
                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                UIUtils.SendReRecevice(Constent.HOME_SCORE);
                UIUtils.SendReRecevice(Constent.JS_RECEVICE);
                setChecked(1);
                mMCurrButtonId = 3;
                break;
            case R.id.main_my:
                mMCurrButtonId = 4;
                setChecked(4);
                if (bean != null) {
                    isUserFul(true);
                } else {
                    isUserFul(false);
                }
                break;
            default:
                break;
        }
        mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
    }

    public boolean isBack() {
        if (mMCurrButtonId == 1 || mMCurrButtonId == 2 || mMCurrButtonId == 3 || mMCurrButtonId == 4 || mMCurrButtonId == 5) {
            mMCurrButtonId = 0;
            setChecked(0);
            mHome.setChecked(true);

            mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
            return false;
        }
        return true;
    }

    private void setChecked(int check) {

        for (int i = 0; i < 5; i++) {
            if (check == i) {
                mList.get(i).setTextColor(ContextCompat.getColor(mContent, R.color.home_top_blue));
            } else {
                mList.get(i).setTextColor(ContextCompat.getColor(mContent, R.color.gray28));
            }
        }
        mRg.setClickable(true);
    }

    private class HomeRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constent.LOADING_HOME:
                    mRg.check(mHome.getId());
                    mMCurrButtonId = 0;
                    mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
                    break;
                case Constent.PREFERENTIAL_ACTIVITIES:
                    mRg.check(mReferr.getId());
                    mMCurrButtonId = 1;
                    mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
                    break;
                case Constent.LOADING_GUESS_BALL:
                    if (guessTheBallFragment != null) {

                        int type = UIUtils.getSputils().getInt("guessball_type", 0);

                        if (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true)) {//足球
                            guessTheBallFragment.setType(1, type);
                        } else {//篮球
                            guessTheBallFragment.setType(2, type);
                        }
                    }

                    mRg.check(mGuessBall.getId());
                    mMCurrButtonId = 2;
                    mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
                    break;
                case Constent.LOADING_MY:
                    mRg.check(mMy.getId());
                    mMCurrButtonId = 4;
                    mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
                    break;
                case Constent.LOADING_CASINO:
                    Intent casionIntent = new Intent(context, CasionActivity.class);
                    startActivity(casionIntent);
                    break;
                case Constent.LOADING_SCORE:
                    if (scoreFragment != null) {
                        int type = UIUtils.getSputils().getInt("score_title_type", 0);

                        int pageType = UIUtils.getSputils().getInt("score_page_type", 0);

                        scoreFragment.setTitleType(type, pageType);
                    }

                    mRg.check(mScore.getId());
                    mMCurrButtonId = 3;
                    mNoScoreViewpager.setCurrentItem(mMCurrButtonId, false);
                    break;
                default:
                    break;
            }
        }
    }
}
