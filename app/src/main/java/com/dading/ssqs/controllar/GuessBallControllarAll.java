package com.dading.ssqs.controllar;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/19 9:34
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.OnLineServerActivity;
import com.dading.ssqs.activity.RankingListActivity;
import com.dading.ssqs.activity.WebActivity;
import com.dading.ssqs.adapter.BaseMePagerAdapter;
import com.dading.ssqs.adapter.HomeBasketballAdapter;
import com.dading.ssqs.adapter.HomeMatchAdapter;
import com.dading.ssqs.adapter.newAdapter.RankingAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.bean.HomeMessageBean;
import com.dading.ssqs.bean.SevenPopBean;
import com.dading.ssqs.bean.SignResultBean;
import com.dading.ssqs.components.RankingView;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Constants;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.AutoVerticalScrollTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 16:43
 * 描述	      首页
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 */

public class GuessBallControllarAll extends BaseTabsContainer implements OnRefreshListener {

    private static final String TAG = "GuessBallControllarAll";
    private ListView mHome_recy_savant;
    private ListView mHome_lv_match;
    private ViewPager mHome_vp_ciecle;
    private LinearLayout mHome_vp_ciecle_dots;
    private int number = 0;
    private ArrayList<View> mHVP;
    private AutoVerticalScrollTextView mHome_win_text;
    private LinearLayout mHome_sc_scrollView_ly;
    private LinearLayout mFreeGlod;
    private LinearLayout mLuckLotto;
    private LinearLayout mRankWin;
    private LinearLayout mGreenhandHelp;
    private AutoScrollTask mAutoScrollTask;
    private TextView mMore;
    private TextView mReferMore;
    private List<HomeMessageBean> mData;
    private List<HomeBean.AdvertsBean> mAdverts;
    public HomeControllarRecevice mRecevice;
    private Handler mHandler;
    private LinearLayout mYpRfer;
    private ImageView mGuessFourShop;
    private TextView mGuessFourLoading;
    private LinearLayout mHome_football_title;
    private LinearLayout mHome_basketball_title;
    private View mFootballView;
    private View mBasketballView;
    private TextView mHome_vp_title;
    private ArrayList<String> mHVpTtile;
    private View view;

    private SwipeToLoadLayout swipeToLoadLayout;

    private HomeVpCircleAdapter homeVpCircleAdapter;
    private HomeMatchAdapter homeMatchAdapter;

    private RankingView rankingView;

    private void processDataMessage(List<HomeMessageBean> bean) {
        mData = bean;
        if (number < mData.size()) {
            mHome_win_text.setText(mData.get(number).content);
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (number < mData.size()) {
                    mHome_win_text.setText(mData.get(number).content);
                    ++number;
                } else {
                    number = 0;
                    mHome_win_text.setText(mData.get(number).content);
                }
                UIUtils.postTaskDelay(this, 1500);
            }
        };
        UIUtils.removeTask(r);
        UIUtils.postTaskDelay(r, 1500);
    }

    /**
     * des 只要继承了该container就能得到带数据的view视图
     * date 2016-4-7 下午4:19:22
     */
    @Override
    public View initContentView(Context content) {
        view = View.inflate(mContent, R.layout.guessball_home_come, null);

        mGuessFourShop = (ImageView) view.findViewById(R.id.guess_four_shop);
        mGuessFourLoading = (TextView) view.findViewById(R.id.guess_four_loading);
        mHome_sc_scrollView_ly = (LinearLayout) view.findViewById(R.id.home_scrollview_ly);//scrollview

        mHome_vp_ciecle = (ViewPager) view.findViewById(R.id.home_vp_circle_pic);//轮转图
        mHome_vp_title = (TextView) view.findViewById(R.id.home_vp_circle_title);//轮转图
        mHome_vp_ciecle_dots = (LinearLayout) view.findViewById(R.id.home_rl_all_dots);//小红点容器
        mHome_lv_match = (ListView) view.findViewById(R.id.home_listView_match);//比赛列表
        mHome_football_title = (LinearLayout) view.findViewById(R.id.home_football_title);//足球标题

        mHome_basketball_title = (LinearLayout) view.findViewById(R.id.home_basketball_title);//篮球标题
        mHome_recy_savant = (ListView) view.findViewById(R.id.home_recy_btn);//专家

        mFreeGlod = (LinearLayout) view.findViewById(R.id.home_four_free_glod);
        mLuckLotto = (LinearLayout) view.findViewById(R.id.home_four_luck_lotto);
        mYpRfer = (LinearLayout) view.findViewById(R.id.home_four_yp_refer);
        mRankWin = (LinearLayout) view.findViewById(R.id.home_four_rank_win);
        mGreenhandHelp = (LinearLayout) view.findViewById(R.id.home_four_greenhand_help);

        mMore = (TextView) view.findViewById(R.id.home_activity_more);
        mReferMore = (TextView) view.findViewById(R.id.home_referr_more);
        mFootballView = view.findViewById(R.id.home_football_view);
        mBasketballView = view.findViewById(R.id.home_basketball_view);

        LinearLayout rankingLayout = (LinearLayout) view.findViewById(R.id.ranking);

        //添加排行榜
        rankingView = new RankingView(content, new RankingAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(mContent, RankingListActivity.class);
                startActivity(intent);
            }
        });
        rankingView.setVisibility(View.GONE);
        rankingLayout.addView(rankingView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 500));

        mHome_win_text = (AutoVerticalScrollTextView) view.findViewById(R.id.home_win_text);

        //防止跳转listview上
        mHome_sc_scrollView_ly.setFocusable(false);
        mHome_sc_scrollView_ly.setFocusableInTouchMode(true);
        mHome_sc_scrollView_ly.requestFocus();

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新

        homeVpCircleAdapter = new HomeVpCircleAdapter();
        mHome_vp_ciecle.setAdapter(homeVpCircleAdapter);

        homeMatchAdapter = new HomeMatchAdapter(mContent);
        mHome_lv_match.setAdapter(homeMatchAdapter);

        return view;
    }

    public void resume() {
        if (rankingView != null) {
            rankingView.resume();
        }
        if (mAutoScrollTask != null) {
            mAutoScrollTask.start();
        }
    }

    public void pause() {
        if (rankingView != null) {
            rankingView.pause();
        }
        if (mAutoScrollTask != null) {
            mAutoScrollTask.stop();
        }
    }

    private void getData() {
        SSQSApplication.apiClient(0).getSysMessageList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {

                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    List<HomeMessageBean> items = (List<HomeMessageBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        processDataMessage(items);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "中奖信息失败信息");
                }
            }
        });
    }

    /**
     * 接收或者加载数据
     * 绑定数据
     * 2016-4-7 下午4:21:14
     */
    @Override
    public void initData() {
        mRecevice = new HomeControllarRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_ACTION);
        mAutoScrollTask = new AutoScrollTask();
        mAutoScrollTask.start();

        //文字滑动
        getData();
        //首页活动请求数据
        getHOME(null);
        //是否签到
        getUserIsSign();
    }

    private void getHOME(final OnDoneListener listener) {
        mGuessFourLoading.setVisibility(UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false) ? View.GONE : View.VISIBLE);

        SSQSApplication.apiClient(0).getMainDataList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    HomeBean homeBean = (HomeBean) result.getData();

                    if (homeBean != null) {
                        Gson gson = new Gson();

                        UIUtils.getSputils().putString(Constent.HOME_CACHE, gson.toJson(homeBean, homeBean.getClass()));
                        processData(homeBean);
                    }

                    if (listener != null) {
                        listener.onDone();
                    }
                } else {
                    if (!AndroidUtilities.checkIsLogin(result.getErrno(), mContent)) {
                        //缓存数据
                        String s = UIUtils.getSputils().getString(Constent.HOME_CACHE, "");
                        if (!TextUtils.isEmpty(s)) {
                            try {
                                HomeBean homeBean = JSON.parseObject(s, HomeBean.class);
                                processData(homeBean);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.midToast(mContent, "请求失败,没有缓存数据,请检查网络拉取最新赛事", 0);
                        }
                    } else {
                        Toast.makeText(mContent, result.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void processData(HomeBean dataEntity) {
        if (mHVP == null && mHVpTtile == null) {
            /**
             * 设置轮转图
             */

            mHVP = new ArrayList<>();
            mHVpTtile = new ArrayList<>();
            mAdverts = dataEntity.getAdverts();
            if (mAdverts != null) {
                for (final HomeBean.AdvertsBean bean : mAdverts) {
                    if (bean != null) {
                        ImageView iv = new ImageView(mContent);
                        SSQSApplication.glide.load(bean.getImageUrl()).error(R.mipmap.image_not).centerCrop().into(iv);

                        mHVP.add(iv);
                        mHVpTtile.add(bean.getName());
                    }
                }

                homeVpCircleAdapter.setList(mHVP);
            }
            /**
             * 为 mIndicatorContainer点的容器动态添加具体的孩子
             */
            mHome_vp_ciecle_dots.removeAllViews();
            for (int i = 0; i < mHVP.size(); i++) {//根据界面数确定点数
                ImageView iv = new ImageView(mContent);
                iv.setImageResource(R.mipmap.er_cai);
                if (i == 0) {
                    iv.setImageResource(R.mipmap.grey);
                    mHome_vp_title.setText(mHVpTtile.get(0));
                    Logger.d(TAG, "home的viewpage标题-----" + mHVpTtile.get(0));
                }

                int width = DensityUtil.dip2px(mContent, 4);
                int margen = DensityUtil.dip2px(mContent, 8);

                // 设置宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(margen, margen);
                // 控制间距
                params.leftMargin = width;
                params.rightMargin = width;
                mHome_vp_ciecle_dots.addView(iv, params);
            }
        }
        /**
         * 比赛信息
         */
        List<HomeBean.MatchsBean> matchs = dataEntity.getMatchs();
        if (matchs == null || matchs.size() == 0) {
            mHome_football_title.setVisibility(View.GONE);
            mHome_lv_match.setVisibility(View.GONE);
            mFootballView.setVisibility(View.GONE);
        } else {
            mHome_football_title.setVisibility(View.VISIBLE);
            mHome_lv_match.setVisibility(View.VISIBLE);
            mFootballView.setVisibility(View.VISIBLE);

            homeMatchAdapter.setList(matchs);
        }


        /**
         * 获取籃球信息
         */
        List<HomeBean.BasketBallsBean> basketBalls = dataEntity.getBasketBalls();

        if (basketBalls == null || basketBalls.size() == 0) {
            mHome_basketball_title.setVisibility(View.GONE);
            mHome_recy_savant.setVisibility(View.GONE);
            mBasketballView.setVisibility(View.GONE);
        } else {
            mHome_recy_savant.setVisibility(View.VISIBLE);
            mHome_basketball_title.setVisibility(View.VISIBLE);
            mBasketballView.setVisibility(View.VISIBLE);

            mHome_recy_savant.setAdapter(new HomeBasketballAdapter(mContent, basketBalls));

            ListScrollUtil.setListViewHeightBasedOnChildren(mHome_recy_savant);
        }

        /**
         * 中奖名单
         */
        HomeBean.OrdersBeanX ordersBeanX = dataEntity.getOrders();

        if (ordersBeanX != null && ordersBeanX.getSysMessge() != null && ordersBeanX.getSysMessge().size() >= 1) {
            rankingView.setVisibility(View.VISIBLE);
            rankingView.setData(ordersBeanX.getSysMessge());
        } else {
            rankingView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mGuessFourShop.setOnClickListener(new View.OnClickListener() {
            private Intent mIntent;

            @Override
            public void onClick(View v) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                    mIntent = new Intent(mContent, NewRechargeActivity.class);
                else
                    mIntent = new Intent(mContent, LoginActivity.class);
                mContent.startActivity(mIntent);
            }
        });
        mGuessFourLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLoading = new Intent(mContent, LoginActivity.class);
                mContent.startActivity(intentLoading);
            }
        });

        mHome_vp_ciecle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mHVP.size(); i++) {
                    ImageView iv = (ImageView) mHome_vp_ciecle_dots.getChildAt(i);
                    if (iv != null)
                        iv.setImageResource(R.mipmap.er_cai);
                    if (i == position) {
                        if (iv != null)
                            iv.setImageResource(R.mipmap.grey);
                        mHome_vp_title.setText(mHVpTtile.get(position));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        /**
         * 四活动设置点击效果
         */
        mFreeGlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFootball();
            }
        });
        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFootball();
            }
        });
        mReferMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBasketball();
            }
        });
        mRankWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBasketball();
            }
        });
        mYpRfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.SendReRecevice(Constent.LOADING_CASINO);
            }
        });
        mLuckLotto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, OnLineServerActivity.class);
                startActivity(intent);
            }
        });
        mGreenhandHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, WebActivity.class);
                intent.putExtra("url", Constants.NEWPEOPLEHELPURL);
                startActivity(intent);
            }
        });

        // 设置切换效果
        mHome_vp_ciecle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mAutoScrollTask.start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mAutoScrollTask.stop();
                        break;
                }
                return false;
            }
        });
    }

    private void setBasketball() {
        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, false);
        UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
    }

    private void setFootball() {
        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
        UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
    }

    public class AutoScrollTask implements Runnable {

        public AutoScrollTask() {
            mHandler = new Handler();
        }

        /**
         * 开始轮播
         * 2016-4-8 下午2:42:26
         */
        public void start() {
            //移除置空
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, 5000);
        }

        /**
         * 停止轮播
         * 2016-4-8 下午2:42:32
         */
        public void stop() {
            mHandler.removeCallbacks(this);
        }

        @Override
        public void run() {
            // 完成viewpager的切换
            int currentItem = mHome_vp_ciecle.getCurrentItem();
            if (mHome_vp_ciecle.getAdapter() != null && currentItem == mHome_vp_ciecle.getAdapter().getCount() - 1)
                currentItem = 0;
            else
                currentItem++;
            //切换
            mHome_vp_ciecle.setCurrentItem(currentItem);
            //再次调用
            start();
        }
    }


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mContenLy.setVisibility(View.GONE);//所以都消失
    }

    /**
     * 轮转图适配器
     */
    class HomeVpCircleAdapter extends BaseMePagerAdapter {

        private final ArrayList<View> mDataView;

        public HomeVpCircleAdapter() {
            this.mDataView = new ArrayList<>();
        }

        public void setList(ArrayList<View> mHVP) {
            if (mHVP != null) {
                this.mDataView.clear();
                this.mDataView.addAll(mHVP);
                notifyDataSetChanged();
            }
        }

        @Override
        protected int setSize() {
            return mDataView == null ? 0 : mDataView.size();
        }

        @Override
        protected View setView(final int position) {
            View iv = mDataView.get(position);
            //加入容器
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContent, HomeViewPagerActivity.class);
                    if (mAdverts != null) {
                        intent.putExtra("infoId", mAdverts.get(position).getForwardID());
                        Logger.d(TAG, "活动跳转返回数据id是----:" + mAdverts.get(position).getForwardID());
                    } else {
                        intent.putExtra("infoId", position);
                    }
                    mContent.startActivity(intent);
                }
            });
            return iv;
        }
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
    }

    private class HomeControllarRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "home接受到关注的通知------------------------:");
            getHOME(null);
        }
    }

    @Override
    public void onRefresh() {
        getHOME(new OnDoneListener() {
            @Override
            public void onDone() {
                getData();
            }
        });

    }

    private interface OnDoneListener {
        void onDone();
    }

    private void getUserIsSign() {
        SSQSApplication.apiClient(0).getTaskDay(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    SevenPopBean bean = (SevenPopBean) result.getData();
                    if (bean != null && bean.isSign == 0) {
                        popData(bean);
                    }
                }
            }
        });
    }

    //签到弹窗
    private PopupWindow mPop;
    private ImageView mSevenDayRedBet1;
    private ImageView mSevenDayRedBet11;
    private ImageView mSevenDayRedBet2;
    private ImageView mSevenDayRedBet21;
    private ImageView mSevenDayRedBet3;
    private ImageView mSevenDayRedBet31;
    private ImageView mSevenDayRedBet4;
    private ImageView mSevenDayRedBet41;
    private ImageView mSevenDayRedBet5;
    private ImageView mSevenDayRedBet51;
    private ImageView mSevenDayRedBet6;
    private ImageView mSevenDayRedBet61;
    private ImageView mSevenDayRedBet7;
    private ImageView mSevenDayGdIv7;
    private ImageView mSevenDayGdIv6;
    private ImageView mSevenDayGdIv5;
    private ImageView mSevenDayGdIv4;
    private ImageView mSevenDayGdIv3;
    private ImageView mSevenDayGdIv2;
    private ImageView mSevenDayGdIv1;
    private TextView mSevenDayAdd7;
    private TextView mSevenDayAdd6;
    private TextView mSevenDayAdd5;
    private TextView mSevenDayAdd4;
    private TextView mSevenDayAdd3;
    private TextView mSevenDayAdd2;
    private TextView mSevenDayAdd1;
    private ImageView mSignButton;
    private ImageView mSigncLose;
    private RelativeLayout mSignLyOut;
    private String mS;
    private RelativeLayout mSignSucLy;
    private TextView mSignSucNum;
    private ImageView mSignSucClose;
    private RelativeLayout mSevenDaySignLy;

    private void popData(final SevenPopBean popBean) {
        View view = View.inflate(mContent, R.layout.activity_seven_day, null);
        mSevenDaySignLy = ButterKnife.findById(view, R.id.seven_day_sign_ly);
        mSevenDayRedBet1 = ButterKnife.findById(view, R.id.seven_day_red_bet1);
        mSevenDayRedBet11 = ButterKnife.findById(view, R.id.seven_day_red_bet11);
        mSevenDayRedBet2 = ButterKnife.findById(view, R.id.seven_day_red_bet2);
        mSevenDayRedBet21 = ButterKnife.findById(view, R.id.seven_day_red_bet21);
        mSevenDayRedBet3 = ButterKnife.findById(view, R.id.seven_day_red_bet3);
        mSevenDayRedBet31 = ButterKnife.findById(view, R.id.seven_day_red_bet31);
        mSevenDayRedBet4 = ButterKnife.findById(view, R.id.seven_day_red_bet4);
        mSevenDayRedBet41 = ButterKnife.findById(view, R.id.seven_day_red_bet41);
        mSevenDayRedBet5 = ButterKnife.findById(view, R.id.seven_day_red_bet5);
        mSevenDayRedBet51 = ButterKnife.findById(view, R.id.seven_day_red_bet51);
        mSevenDayRedBet6 = ButterKnife.findById(view, R.id.seven_day_red_bet6);
        mSevenDayRedBet61 = ButterKnife.findById(view, R.id.seven_day_red_bet61);
        mSevenDayRedBet7 = ButterKnife.findById(view, R.id.seven_day_red_bet7);

        mSevenDayGdIv1 = ButterKnife.findById(view, R.id.seven_day_gd_iv1);
        mSevenDayGdIv2 = ButterKnife.findById(view, R.id.seven_day_gd_iv2);
        mSevenDayGdIv3 = ButterKnife.findById(view, R.id.seven_day_gd_iv3);
        mSevenDayGdIv4 = ButterKnife.findById(view, R.id.seven_day_gd_iv4);
        mSevenDayGdIv5 = ButterKnife.findById(view, R.id.seven_day_gd_iv5);
        mSevenDayGdIv6 = ButterKnife.findById(view, R.id.seven_day_gd_iv6);
        mSevenDayGdIv7 = ButterKnife.findById(view, R.id.seven_day_gd_iv7);

        mSevenDayAdd1 = ButterKnife.findById(view, R.id.seven_day_add1);
        mSevenDayAdd2 = ButterKnife.findById(view, R.id.seven_day_add2);
        mSevenDayAdd3 = ButterKnife.findById(view, R.id.seven_day_add3);
        mSevenDayAdd4 = ButterKnife.findById(view, R.id.seven_day_add4);
        mSevenDayAdd5 = ButterKnife.findById(view, R.id.seven_day_add5);
        mSevenDayAdd6 = ButterKnife.findById(view, R.id.seven_day_add6);
        mSevenDayAdd7 = ButterKnife.findById(view, R.id.seven_day_add7);

        mSignButton = ButterKnife.findById(view, R.id.seven_sign_button);
        mSigncLose = ButterKnife.findById(view, R.id.seven_day_close);
        mSignLyOut = ButterKnife.findById(view, R.id.seven_day_ly_out);

        mSignSucLy = ButterKnife.findById(view, R.id.seven_sign_suc_ly);
        mSignSucNum = ButterKnife.findById(view, R.id.get_glod_num);
        mSignSucClose = ButterKnife.findById(view, R.id.seven_day_suc_close);

        mSevenDaySignLy.setVisibility(View.VISIBLE);
        mSignSucLy.setVisibility(View.GONE);

        mSignButton.setClickable(true);

        SevenPopBean data = popBean;
        final int dayCount = data.dayCount;
        List<SevenPopBean.TasksEntity> tasks = data.tasks;
        for (int i = 1; i <= tasks.size(); i++) {
            int status = tasks.get(i - 1).status;
            String text = "+" + tasks.get(i - 1).banlance;
            if (status == 1) {
                switch (i) {
                    case 1:
                        mSevenDayRedBet1.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv1.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd1.setText(text);
                        mSevenDayAdd1.setTextColor(mContent.getResources().getColor(R.color.orange));
                        break;
                    case 2:
                        mSevenDayRedBet2.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayGdIv2.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd2.setText(text);
                        mSevenDayAdd2.setTextColor(mContent.getResources().getColor(R.color.orange));
                        break;
                    case 3:
                        mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet3.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv3.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd3.setText(text);
                        mSevenDayAdd3.setTextColor(mContent.getResources().getColor(R.color.orange));
                        break;
                    case 4:
                        mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet4.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv4.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd4.setText(text);
                        mSevenDayAdd4.setTextColor(mContent.getResources().getColor(R.color.orange));
                        break;
                    case 5:
                        mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet5.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv5.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd5.setText(text);
                        mSevenDayAdd5.setTextColor(mContent.getResources().getColor(R.color.orange));
                        break;
                    case 6:
                        mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                        mSevenDayRedBet6.setImageResource(R.mipmap.s_have_checked_in_sel);
                        mSevenDayGdIv6.setImageResource(R.mipmap.s_gold_sel);
                        mSevenDayAdd6.setText(text);
                        mSevenDayAdd6.setTextColor(mContent.getResources().getColor(R.color.orange));
                        break;
                    case 7:
                        //7不会出现只会在点击后出现
                        mSevenDayRedBet7.setImageResource(R.mipmap.s_have_checked_in_sel);
                        break;
                    default:
                        break;
                }
            } else {
                switch (i) {
                    case 1:
                        mSevenDayAdd1.setText(text);
                        break;
                    case 2:
                        mSevenDayAdd2.setText(text);
                        break;
                    case 3:
                        mSevenDayAdd3.setText(text);
                        break;
                    case 4:
                        mSevenDayAdd4.setText(text);
                        break;
                    case 5:
                        mSevenDayAdd5.setText(text);
                        break;
                    case 6:
                        mSevenDayAdd6.setText(text);
                        break;
                    case 7:
                        mSevenDayAdd7.setText(text);
                        break;
                    default:
                        break;
                }
            }
        }
        switch (dayCount) {
            case 0:
                mSevenDayRedBet1.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv1.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd1.setTextColor(mContent.getResources().getColor(R.color.orange));
                mS = data.tasks.get(0).banlance + "";
                break;
            case 1:
                mSevenDayRedBet2.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv2.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd2.setTextColor(mContent.getResources().getColor(R.color.orange));
                mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(1).banlance + "";
                break;
            case 2:
                mSevenDayRedBet3.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv3.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd3.setTextColor(mContent.getResources().getColor(R.color.orange));
                mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(2).banlance + "";
                break;
            case 3:
                mSevenDayRedBet4.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv4.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd4.setTextColor(mContent.getResources().getColor(R.color.orange));
                mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(3).banlance + "";
                break;
            case 4:
                mSevenDayRedBet5.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv5.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd5.setTextColor(mContent.getResources().getColor(R.color.orange));
                mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(4).banlance + "";
                break;
            case 5:
                mSevenDayRedBet6.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv6.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd6.setTextColor(mContent.getResources().getColor(R.color.orange));
                mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(5).banlance + "";
                break;
            case 6:
                mSevenDayRedBet7.setImageResource(R.mipmap.s_unclaimed);
                mSevenDayGdIv7.setImageResource(R.mipmap.s_gold_sel);
                mSevenDayAdd7.setTextColor(mContent.getResources().getColor(R.color.orange));
                mSevenDayRedBet61.setImageResource(R.mipmap.s_vertical_bar_sel);
                mS = data.tasks.get(6).banlance + "";
                break;
            default:
                break;
        }

        UIUtils.getSputils().putString(Constent.SIGN_GLOD, mS);

        mPop = PopUtil.popuMake(view);

        mPop.showAtLocation(view, Gravity.CENTER, 0, 0);

        mSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SSQSApplication.apiClient(0).userSign(new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            SignResultBean bean = (SignResultBean) result.getData();

                            if (bean != null) {
                                switch (bean.dayCount) {
                                    case 1:
                                        mSevenDayRedBet1.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 2:
                                        mSevenDayRedBet11.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet2.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 3:
                                        mSevenDayRedBet21.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet3.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 4:
                                        mSevenDayRedBet31.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet4.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 5:
                                        mSevenDayRedBet41.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet5.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 6:
                                        mSevenDayRedBet51.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet6.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    case 7:
                                        mSevenDayRedBet61.setImageResource(R.mipmap.s_vertical_bar_sel);
                                        mSevenDayRedBet7.setImageResource(R.mipmap.s_have_checked_in_sel);
                                        break;
                                    default:
                                        break;
                                }
                                int banlance = popBean.tasks.get(bean.dayCount).banlance;

                                UIUtils.getSputils().putBoolean(Constent.SIGN_SUC, true);

                                UIUtils.getSputils().putInt("banlance", banlance);

                                UIUtils.SendReRecevice(Constent.TASK_TAG);

                                UIUtils.SendReRecevice(Constent.REFRESH_MONY);

                                String s = "+" + banlance + "元";
                                mSignSucNum.setText(s);
                                mSignSucLy.setVisibility(View.VISIBLE);
                                mSevenDaySignLy.setVisibility(View.GONE);
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        ((HomeFreeGlodActivity) mContent).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mPop.dismiss();
                                            }
                                        });
                                    }
                                }, 2000);
                                mSignButton.setImageResource(R.mipmap.s_parcel);
                            }
                        } else {
                            if (!AndroidUtilities.checkIsLogin(result.getErrno(), mContent)) {
                                ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
            }
        });
        mSigncLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mSignLyOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });

        mSignSucClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
    }
}

