package com.dading.ssqs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MatchInfoContentAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceMatchBallElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.MatchInfoBean;
import com.dading.ssqs.controllar.details.MatchInfoFx;
import com.dading.ssqs.controllar.details.MatchInfoJc;
import com.dading.ssqs.controllar.details.MatchInfoLq;
import com.dading.ssqs.controllar.details.MatchInfoPl;
import com.dading.ssqs.controllar.details.MatchInfoSk;
import com.dading.ssqs.controllar.details.MatchInfoTj;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.GlideCircleTransform;
import com.umeng.analytics.MobclickAgent;


import java.util.ArrayList;
import java.util.Date;

import com.dading.ssqs.components.tabindicator.TabIndicator;


/**
 * 创建者     ZCL
 * 创建时间   2016/7/13 17:56
 * 描述	      比赛详情   分析、赔率、推荐、实况、聊球、竞猜
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$I
 * 更新描述   ${TODO}
 */
public class MatchInfoActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MatchInfoActivity";
    ImageView mMatchInfoLiving;
    ImageView mMatchInfoBack;

    TabIndicator mMatchInfoIndicator;
    ViewPager mMatchInfoViewPager;

    TextView mMatchInfoType;
    CheckBox mMatchInfoCollect1;
    TextView mMatchInfoOpenTime;
    TextView mMatchInfoTimer;
    TextView mMatchInfoTimericon;
    ImageView mMatchInfoMainIcon;
    TextView mMatchInfoMain;
    TextView mMatchInfoScore;
    TextView mMatchInfoScoreNo;
    ImageView mMatchInfoSecondIcon;
    TextView mMatchInfoSecond;
    TextView mMatchUpHalfscore;
    LinearLayout mLivingVideoLy;
    LinearLayout mMatchInfoBottom;
    TextView mMatchInfoMainRank;
    TextView mMatchInfoSecondRank;
    LinearLayout mMatchInfoLoading;


    private ArrayList<View> mDataView;
    private ArrayList<String> mDataIndicatorMatchContentTitle;
    private long mI;
    private int mH;
    private long mPlayTimes;
    GestureDetector mGestureDetector;
    private int minTopPadding;
    private int minBottomPadding;
    private float mInitTopPadding;
    private int mTop;
    private int mBottom;
    private int mMatchId;
    private String mWhere;
    public String mToken;
    public JCLoadingReciver mReceiver;
    private MatchInfoBean mBean;
    public int mIsOver;
    public View mMatchView;
    public MatchInfoBean mData;
    private int mIsFouce;
    private MatchInfoJc mJc;
    private MatchInfoLq mMatchInfoLq;
    private AlphaAnimation mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
    private boolean mIsLoading;
    public boolean mIsDraw;
    private String mUrl;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mIsDraw = true;
            initData();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIUtils.removeTaskAll(null);
        if (mReceiver != null)
            UIUtils.UnReRecevice(mReceiver);
        if (mJc != null && mJc.mRecevice != null) {
            UIUtils.UnReRecevice(mJc.mRecevice);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        getHeadData();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        Intent intent = new Intent();
        intent.setAction(Constent.IS_SHOW2);
        UIUtils.getContext().sendBroadcast(intent);
    }

    @Override
    protected void initListener() {
        mMatchInfoBack.setOnClickListener(this);
        mMatchInfoLiving.setOnClickListener(this);
        mMatchInfoCollect1.setOnClickListener(this);
        mMatchInfoViewPager.addOnPageChangeListener(this);
        mMatchInfoIndicator.setOnPageChangeListener(this);
    }

    @Override
    protected void initView() {
        mMatchInfoCollect1 = (CheckBox) findViewById(R.id.match_info_collect_1);
        mMatchInfoLiving = (ImageView) findViewById(R.id.match_info_living);
        mMatchInfoBack = (ImageView) findViewById(R.id.match_info_back);
        mMatchInfoSecondIcon = (ImageView) findViewById(R.id.match_info_second_icon);
        mMatchInfoMainIcon = (ImageView) findViewById(R.id.match_info_main_icon);

        mMatchInfoIndicator = (TabIndicator) findViewById(R.id.match_info_tabindicator);
        mMatchInfoViewPager = (ViewPager) findViewById(R.id.match_info_viewpager);

        mMatchInfoType = (TextView) findViewById(R.id.match_info_type);
        mMatchInfoOpenTime = (TextView) findViewById(R.id.match_info_open_time);
        mMatchInfoTimer = (TextView) findViewById(R.id.match_info_timer);
        mMatchInfoMain = (TextView) findViewById(R.id.match_info_main);

        mMatchInfoTimericon = (TextView) findViewById(R.id.match_info_timericon);
        mMatchInfoScore = (TextView) findViewById(R.id.match_info_score);
        mMatchInfoScoreNo = (TextView) findViewById(R.id.match_info_score_no);
        mMatchInfoSecond = (TextView) findViewById(R.id.match_info_second);
        mMatchUpHalfscore = (TextView) findViewById(R.id.match_up_halfscore);
        mLivingVideoLy = (LinearLayout) findViewById(R.id.living_video_ly);
        mMatchInfoBottom = (LinearLayout) findViewById(R.id.match_info_bottom);
        mMatchInfoMainRank = (TextView) findViewById(R.id.match_info_main_rank);
        mMatchInfoSecondRank = (TextView) findViewById(R.id.match_info_second_rank);
        mMatchInfoLoading = (LinearLayout) findViewById(R.id.loading_animal);
        mMatchInfoCollect1 = (CheckBox) findViewById(R.id.match_info_collect_1);


        mMatchView = View.inflate(this, R.layout.activity_match_info_content, null);

        Intent intent = getIntent();
        mMatchId = intent.getIntExtra(Constent.MATCH_ID, 0);
        Logger.INSTANCE.d(TAG, "得到的赛事ID是------------------------------:" + mMatchId);
        mWhere = intent.getStringExtra(Constent.INTENT_FROM);
        Logger.INSTANCE.d(TAG, "来自------------------------------:" + mWhere);
        mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);

        if (mIsLoading) {
        } else {
            mMatchInfoCollect1.setChecked(false);
        }
        mToken = UIUtils.getSputils().getString(Constent.TOKEN, "");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_match_info_content;
    }

    private void processData(MatchInfoBean bean) {
        mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
        setHead(bean);

        mDataIndicatorMatchContentTitle = new ArrayList<>();
        mDataView = new ArrayList<>();

        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);
        if (!b) {
            mDataIndicatorMatchContentTitle.add("聊球");
            mDataIndicatorMatchContentTitle.add("竞猜");
            mMatchInfoLq = new MatchInfoLq(this, mMatchId);
            mDataView.add(mMatchInfoLq.mRootView);
            mJc = new MatchInfoJc(this, mMatchId, mIsOver, mIsDraw);
            mDataView.add(mJc.mRootView);

            mMatchInfoViewPager.setAdapter(new MatchInfoContentAdapter(mDataView, mDataIndicatorMatchContentTitle, this));
            mMatchInfoIndicator.setViewPager(mMatchInfoViewPager);

            mMatchInfoViewPager.setCurrentItem(1);
            mLivingVideoLy.setBackgroundResource(R.mipmap.basketball_bg);
        } else {

            mLivingVideoLy.setBackgroundResource(R.mipmap.football_bg);

            mDataIndicatorMatchContentTitle.add("分析");
            mDataIndicatorMatchContentTitle.add("赔率");
            mDataIndicatorMatchContentTitle.add("推荐");
            mDataIndicatorMatchContentTitle.add("实况");

            mDataIndicatorMatchContentTitle.add("聊球");
            mDataIndicatorMatchContentTitle.add("竞猜");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView iv = new ImageView(this);
            iv.setImageResource(R.mipmap.background_image);
            iv.setLayoutParams(params);
            if (bean != null) {
                mDataView.add(new MatchInfoFx(this, mMatchId, bean.home, bean.away, bean.aImageUrl, bean.hImageUrl).mRootView);
            } else {
                mDataView.add(new MatchInfoFx(this, mMatchId, "", "", "", "").mRootView);
            }
            MatchInfoPl matchInfoPl = new MatchInfoPl(this, mMatchId);
            MatchInfoTj matchInfoTj = new MatchInfoTj(this, mMatchId);
            MatchInfoSk matchInfoSk = new MatchInfoSk(this, mMatchId);
            mDataView.add(matchInfoPl.mRootView);
            mDataView.add(matchInfoTj.mRootView);
            mDataView.add(matchInfoSk.mRootView);

            mMatchInfoLq = new MatchInfoLq(this, mMatchId);
            mDataView.add(mMatchInfoLq.mRootView);
            mJc = new MatchInfoJc(this, mMatchId, mIsOver, mIsDraw);
            mDataView.add(mJc.mRootView);

            mMatchInfoViewPager.setAdapter(new MatchInfoContentAdapter(mDataView, mDataIndicatorMatchContentTitle, this));
            mMatchInfoIndicator.setViewPager(mMatchInfoViewPager);
            //根据跳转的来源判断显示哪个界面
            switch (mWhere) {
                case "home":
                case "match_before":
                case "JS":
                case "SC":
                case "GQ":
                case "5":
                case "0":
                    mMatchInfoViewPager.setCurrentItem(5);
                    break;
                case "1":
                    mMatchInfoViewPager.setCurrentItem(1);
                    break;
                case "2":
                    mMatchInfoViewPager.setCurrentItem(2);
                    break;
                case "SG":
                case "3":
                    mMatchInfoViewPager.setCurrentItem(3);
                    break;
                case "4":
                    mMatchInfoViewPager.setCurrentItem(4);
                    break;
            }
        }
    }

    private void setHead(MatchInfoBean bean) {
        if (bean != null) {
            mData = bean;
            if (mData != null)
                mIsOver = mData.isOver;
            if (mMatchInfoCollect1 != null)
                switch (mData.isFouce) {
                    case 0:
                        mMatchInfoCollect1.setChecked(false);
                        break;
                    case 1:
                        mMatchInfoCollect1.setChecked(true);
                        break;
                    default:
                        break;
                }
            String hRankING = mData.hOrderFrom + "排名[" + mData.hOrder + "]";
            String aRankING = mData.aOrderFrom + "排名[" + mData.aOrder + "]";


            if (mMatchInfoMainRank != null)
                mMatchInfoMainRank.setText(hRankING);
            if (mMatchInfoSecondRank != null)
                mMatchInfoSecondRank.setText(aRankING);


            mMatchInfoType.setText(mData.leagueName);

            mMatchInfoMain.setText(mData.home);
            mMatchInfoSecond.setText(mData.away);
            Logger.INSTANCE.d(TAG, "主队标记返回数据是------------------------------:" + mData.aImageUrl);
            SSQSApplication.glide.load(mData.hImageUrl).error(R.mipmap.fail).transform(new GlideCircleTransform(this)).into(mMatchInfoMainIcon);

            SSQSApplication.glide.load(mData.aImageUrl).error(R.mipmap.fail).transform(new GlideCircleTransform(this)).into(mMatchInfoSecondIcon);

            String text = mData.hScore + "-" + mData.aScore;

            mMatchInfoScore.setText(text);//现在比分
            Logger.INSTANCE.d(TAG, "比分返回数据是------------------------------:" + mData.hScore + "-" + mData.aScore);

            //半场比分
            if ("".equals(mData.hHalfScore)) {
                mMatchUpHalfscore.setVisibility(View.GONE);
            } else {
                mMatchUpHalfscore.setVisibility(View.VISIBLE);
                String s = mData.hHalfScore + ":" + mData.aHalfScore;
                mMatchUpHalfscore.setText(s);//现在比分
            }

            switch (mData.isOver) {
                case 0:
                    /**
                     * 获取毫秒值
                     */
                    try {
                        long nowTimers = new Date().getTime();
                        Logger.INSTANCE.d(TAG, "nowTimers------------------------------:" + nowTimers);
                        long starTimes = DateUtils.formatDate(mData.openTime).getTime();
                        Logger.INSTANCE.d(TAG, "startTime数据是------------------------------:" + starTimes);
                        mPlayTimes = nowTimers - starTimes;
                        Logger.INSTANCE.d(TAG, "现在进行的时间是：" + mPlayTimes / 1000 / 60);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                    mAlphaAnimation.setDuration(500);
                    mAlphaAnimation.setRepeatMode(Animation.REVERSE);
                    mAlphaAnimation.setRepeatCount(Animation.INFINITE);//无线循环
                    mMatchInfoTimericon.setAnimation(mAlphaAnimation);

                    if (TextUtils.isEmpty(mData.protime)) {
                        if (mPlayTimes > 0) {
                            setStartShow();
                            /**
                             * 现在时间-开始时间得到毫秒值
                             */

                            mI = mPlayTimes % 1000 % 60;
                            mH = (int) (mPlayTimes / 1000 / 60);
                            Logger.INSTANCE.d(TAG, "现在进行的时间是：" + mH + "分" + mI + "秒" + mPlayTimes);
                            if (mH > 64) {
                                int i = mH - 15;
                                mMatchInfoTimer.setText(String.valueOf(i));
                            } else {
                                mMatchInfoTimer.setText(String.valueOf(mH));
                            }
                            mAlphaAnimation.start();
                        } else {
                            if (mData.openTime != null && mData.openTime.length() >= 16) {
                                String openTime = mData.openTime;
                                String s = openTime.substring(0, 16);
                                mMatchInfoOpenTime.setText(s);
                                mAlphaAnimation.cancel();
                            }
                        }
                    } else {
                        if (!"半场".equals(mData.protime)) {
                            String[] split = mData.protime.split(" ");
                            if (split.length > 1) {//判断是篮球还是足球
                                //判断是否要减15分钟
                                Logger.INSTANCE.d(TAG, "分割时间------------------------------:" + split[1]);
                                if (!TextUtils.isEmpty(split[1]))
                                    mMatchInfoTimer.setText(split[0] + " " + split[1]);
                                else
                                    mMatchInfoTimer.setText(split[0]);
                            } else {
                                if (Integer.parseInt(mData.protime) > 64) {
                                    mMatchInfoTimer.setText(String.valueOf(Integer.parseInt(mData.protime) - 15));
                                } else if (Integer.parseInt(mData.protime) >= 0) {
                                    mMatchInfoTimer.setText(mData.protime);
                                }
                            }
                            mMatchInfoTimericon.setVisibility(View.VISIBLE);
                            mAlphaAnimation.start();
                            setStartShow();
                        } else {
                            mMatchInfoTimer.setText(String.valueOf(mData.protime));
                            mAlphaAnimation.cancel();
                            mMatchInfoTimericon.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 1:
                    mAlphaAnimation.cancel();
                    mMatchInfoScoreNo.setVisibility(View.GONE);
                    mMatchInfoScore.setVisibility(View.VISIBLE);
                    mMatchInfoScore.setTextColor(Color.RED);
                    mMatchInfoTimer.setText("完场");
                    mMatchInfoTimericon.setVisibility(View.GONE);
                    break;
                case 2:
                    mAlphaAnimation.cancel();
                    mMatchInfoScoreNo.setVisibility(View.VISIBLE);
                    mMatchInfoScore.setVisibility(View.GONE);
                    mMatchInfoTimer.setText("中断");
                    mMatchInfoTimericon.setVisibility(View.GONE);
                    break;
                case 3:
                    mMatchInfoScoreNo.setVisibility(View.VISIBLE);
                    mMatchInfoScore.setVisibility(View.GONE);
                    mMatchInfoTimer.setText("半场");
                    mMatchInfoTimericon.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(mData.protime)) {
                        if (!"半场".equals(mData.protime)) {
                            String[] split = mData.protime.split(" ");
                            if (split.length > 1) {//判断是篮球还是足球
                                //判断是否要减15分钟
                                Logger.INSTANCE.d(TAG, "分割时间------------------------------:" + split[1]);
                                if (!TextUtils.isEmpty(split[1]) && Integer.parseInt(split[1]) > 64)
                                    mMatchInfoTimer.setText(split[0] + " " + String.valueOf(Integer.parseInt(split[1]) - 15));
                                else
                                    mMatchInfoTimer.setText(split[1]);
                            } else {
                                if (Integer.parseInt(mData.protime) > 64) {
                                    mMatchInfoTimer.setText(String.valueOf(Integer.parseInt(mData.protime) - 15));
                                } else if (Integer.parseInt(mData.protime) >= 0) {
                                    mMatchInfoTimer.setText(mData.protime);
                                }
                            }
                            mMatchInfoTimericon.setVisibility(View.VISIBLE);
                            mAlphaAnimation.start();
                        } else {
                            mMatchInfoTimer.setText(String.valueOf(mData.protime));
                            mAlphaAnimation.cancel();
                        }
                    }
                    break;
            }
        }
    }

    private void setStartShow() {
        mMatchInfoTimer.setVisibility(View.VISIBLE);
        mMatchInfoTimericon.setVisibility(View.VISIBLE);
        mMatchInfoOpenTime.setVisibility(View.GONE);
        mMatchInfoScoreNo.setVisibility(View.GONE);
        mMatchInfoScore.setVisibility(View.VISIBLE);
        mMatchInfoTimer.setTextColor(Color.RED);
    }

    @Override
    protected void initData() {
        getData();
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                if (mMatchInfoLoading != null)
                    mMatchInfoLoading.setVisibility(View.GONE);
            }
        }, 2000);
        UIUtils.ReRecevice(mReceiver, Constent.LOADING_ACTION);

        mAlphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnimation.setDuration(500);
        mAlphaAnimation.setRepeatMode(Animation.REVERSE);
        mAlphaAnimation.setRepeatCount(Animation.INFINITE);//无线循环
        mMatchInfoTimericon.setAnimation(mAlphaAnimation);
        mAlphaAnimation.start();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                getHeadData();
                UIUtils.postTaskDelay(this, 10000);
            }
        };
        UIUtils.postTaskDelay(r, 10000);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                if (e1 != null && e2 != null) {
                    float x1 = e1.getX();
                    float x2 = e2.getX();

                    float y1 = e1.getY();
                    float y2 = e2.getY();
                    // 上下滑动
                    int f = (int) (y1 - y2 + 0.5f);
                    // 左右滑动绝对长度
                    float absY = Math.abs(f);
                    float absX = Math.abs(x1 - x2);
                    //判断左右
                    if (absY < absX) {
                        return false;
                    }
                    //设置最小padding
                    minTopPadding = -mLivingVideoLy.getHeight() / 4;
                    minBottomPadding = -mLivingVideoLy.getHeight() / 4;

                    int[] postions = new int[2];
                    mLivingVideoLy.getLocationInWindow(postions);

                    mInitTopPadding = postions[1];
                    Logger.INSTANCE.d(TAG, "原来控件x值:" + postions[0] + "原控件Y值" + mInitTopPadding);

                    Logger.INSTANCE.d(TAG, "原来控件高度值:" + mLivingVideoLy.getHeight() + "滑动Y值:" + f);
                    // f位负数时为下拉动作,f位正数时为上推动作

                    if (f >= 0 && mInitTopPadding > minTopPadding) {
                        Logger.INSTANCE.d(TAG, "上推压缩");
                        if (f > -mInitTopPadding) {
                            mTop = minTopPadding;
                        } else {
                            mTop = -f;
                        }
                        if (f > -minBottomPadding) {
                            mBottom = minBottomPadding;
                        } else {
                            mBottom = -f;
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, mTop, 0, mBottom);
                        mLivingVideoLy.setLayoutParams(params);
                        mLivingVideoLy.invalidate();
                    }

                    if (f < 0 && mTop >= minTopPadding && mTop <= 0) {
                        Logger.INSTANCE.d(TAG, "下拉伸展");
                        if (f <= mTop) {
                            mTop = 0;
                        } else {
                            mTop = mTop - f;
                        }
                        if (f <= mBottom) {
                            mBottom = 0;
                        } else {
                            mBottom = mBottom - f;
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, mTop, 0, mBottom);
                        mLivingVideoLy.setLayoutParams(params);
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }


    private void getData() {
        //通过比赛id获取比赛两队的球队信息
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        SSQSApplication.apiClient(classGuid).getMathchData(b, mMatchId + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mBean = (MatchInfoBean) result.getData();

                    if (mBean != null) {
                        processData(mBean);
                    }
                    if (mMatchInfoLoading != null) {
                        mMatchInfoLoading.setVisibility(View.GONE);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(MatchInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void getHeadData() {
        //通过比赛id获取比赛两队的球队信息
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        SSQSApplication.apiClient(classGuid).getMathchData(b, mMatchId + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    if (mMatchInfoLoading != null) {
                        mMatchInfoLoading.setVisibility(View.GONE);
                    }
                    setHead(mBean);
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(MatchInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//把当前的事件交个 手势识别去处理
        if (mGestureDetector != null)
            mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.match_info_back:
                Logger.INSTANCE.d(TAG, "点击返回按钮------------------------------:");
                finish();
                break;
            case R.id.match_info_living:
               /* if (TextUtils.isEmpty(mData.liveCastUrl)) {
                    livingnotv.setVisibility(View.GONE);
                } else {
                    livingnotv.setVisibility(View.GONE);
                    livingwb.setInitialScale(25);//为25%，最小缩放等级
                    WebSettings setting = livingwb.getSettings();
                    //支持javascript
                    setting.setJavaScriptEnabled(true);
                    // 设置可以支持缩放
                    setting.setSupportZoom(true);
                    // 设置可以支持缩放
                    setting.setBuiltInZoomControls(false);
                    //扩大比例的缩放
                    setting.setUseWideViewPort(true);
                    //播放视频
                    setting.setPluginState(WebSettings.PluginState.ON);
                    //自适应屏幕
                    setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                    setting.setLoadWithOverviewMode(true);

                    setting.setSavePassword(true);
                    setting.setSaveFormData(true);// 保存表单数据
                    setting.setJavaScriptEnabled(true);

                    setting.setDomStorageEnabled(true);
                    setting.setSupportMultipleWindows(true);// 新加

                    setting.setUseWideViewPort(true); // 关键点
                    setting.setAllowFileAccess(true); // 允许访问文件
                    setting.setSupportZoom(true); // 支持缩放

                    setting.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不加载缓存内容
                    livingwb.setWebChromeClient(new WebChromeClient());
                    livingwb.setWebViewClient(new WebViewClient());
                    //livingwb.loadUrl("http://www.mgtv.com/b/310102/3818687.html?cxid=94n3624ea");
                    livingwb.loadUrl("http://v.youku.com/v_show/id_XMTU5NzMxNzI4.html?from=y1.2-2.2");
                }*/
                Intent intent = new Intent(this, LivingActivity.class);
               /* if (mData!=null&&mIsStart){*/
                intent.putExtra(Constent.LIVING, mData.liveCastUrl);
                startActivity(intent);
               /* }else{
                    ToastUtils.midToast(MatchInfoActivity.this,"直播还未开始,请稍后!",0);
                }*/
                //mPopLiving.showAtLocation(mView, Gravity.CENTER,0,0);

                break;
            case R.id.match_info_collect_1:
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intentLoad = new Intent(MatchInfoActivity.this, LoginActivity.class);
                    intentLoad.putExtra(Constent.MATCH_INFO_TAG, "match_info_tag");
                    startActivity(intentLoad);
                } else {
                    if (mData == null) {
                        return;
                    }
                    ///v1.0/fouceMatch/ball
                    FouceMatchBallElement element = new FouceMatchBallElement();
                    element.setMatchID(String.valueOf(mMatchId));
                    element.setStatus(mIsFouce == 0 ? "1" : "0");

                    SSQSApplication.apiClient(classGuid).fouceMatchBall(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    Intent intent = new Intent();
                                    if (mIsFouce == 0) {
                                        mIsFouce = 1;
                                        mData.isFouce = 1;
                                    } else {
                                        mIsFouce = 0;
                                        mData.isFouce = 0;
                                    }
                                    intent.putExtra(Constent.ISFOUCE, 1);
                                    setResult(RESULT_OK, intent);

                                    UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
                                    UIUtils.SendReRecevice(Constent.GQ_RECEVICE);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(MatchInfoActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // ToastUtils.midToast(MatchInfoActivity.this, bean.msg, 0);
                                }
                            }
                        }
                    });

                    Logger.INSTANCE.d(TAG, "这是比赛闲情josn------------------------------:");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Intent intent = new Intent();
        intent.setAction(Constent.IS_SHOW2);
        UIUtils.getContext().sendBroadcast(intent);
    }


    private class JCLoadingReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constent.LOADING_ACTION)) {
                mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                Logger.INSTANCE.d(TAG, "接受到广播后登陆状态是------------------------------:" + mIsLoading);

                if (mIsLoading) {
                    if (mIsFouce == 0) {
                        mMatchInfoCollect1.setChecked(false);
                    } else {
                        mMatchInfoCollect1.setChecked(true);
                    }
                } else {
                    mMatchInfoCollect1.setChecked(false);
                }
            }
        }
    }
}
