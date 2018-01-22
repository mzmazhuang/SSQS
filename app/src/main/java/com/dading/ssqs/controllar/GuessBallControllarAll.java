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
import com.dading.ssqs.activity.AccountDetailActivity;
import com.dading.ssqs.activity.BettingRecordActivity;
import com.dading.ssqs.activity.HomeFreeGlodActivity;
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MoreSettingActivity;
import com.dading.ssqs.activity.MyMessageActivity;
import com.dading.ssqs.activity.NewBindBankCardActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.OnLineServerActivity;
import com.dading.ssqs.activity.ProxyCenterActivity;
import com.dading.ssqs.activity.RankingListActivity;
import com.dading.ssqs.activity.RechargeDetailActivity;
import com.dading.ssqs.activity.SuggestionActivity;
import com.dading.ssqs.activity.WebActivity;
import com.dading.ssqs.activity.WithDrawActivity;
import com.dading.ssqs.activity.WithDrawDentailActivity;
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
import com.dading.ssqs.components.AutoRecyclerView;
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
    private Handler mHandler;
    private LinearLayout mYpRfer;
    private ImageView mGuessFourShop;
    private TextView mGuessFourLoading;
    private LinearLayout mHome_football_title;
    private LinearLayout mHome_basketball_title;
    private View mFootballView;
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

        LinearLayout rankingLayout = (LinearLayout) view.findViewById(R.id.ranking);

        //添加排行榜
        rankingView = new RankingView(content, new AutoRecyclerView.OnAutoRecyclerClickListener() {
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
                    Logger.INSTANCE.d(TAG, result.getMessage() + "中奖信息失败信息");
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
        mAutoScrollTask = new AutoScrollTask();
        mAutoScrollTask.start();

        //文字滑动
        getData();
        //首页活动请求数据
        getHOME(null);
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
                        processData(homeBean);
                    }

                    if (listener != null) {
                        listener.onDone();
                    }
                } else {
                    if (!AndroidUtilities.INSTANCE.checkIsLogin(result.getErrno(), mContent)) {
                        ToastUtils.midToast(mContent, "请求失败,请检查网络拉取最新赛事", 0);
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
                    Logger.INSTANCE.d(TAG, "home的viewpage标题-----" + mHVpTtile.get(0));
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
        } else {
            mHome_recy_savant.setVisibility(View.VISIBLE);
            mHome_basketball_title.setVisibility(View.VISIBLE);

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
                setFootball(1);
            }
        });
        mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFootball(1);
            }
        });
        mReferMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBasketball(1);
            }
        });
        mRankWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBasketball(1);
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

    private void setBasketball(int type) {
        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, false);
        UIUtils.getSputils().putInt("guessball_type", type);
        UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
    }

    private void setFootball(int type) {
        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
        UIUtils.getSputils().putInt("guessball_type", type);
        UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
    }

    private void setScore(int pageType, int titleType) {
        UIUtils.getSputils().putInt("score_page_type", pageType);
        UIUtils.getSputils().putInt("score_title_type", titleType);
        UIUtils.SendReRecevice(Constent.LOADING_SCORE);
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
                    HomeBean.AdvertsBean bean = mAdverts.get(position);
                    if (bean != null) {
                        if (!TextUtils.isEmpty(bean.getForwardName())) {//子模块
                            if ("篮球比分".equals(bean.getFunsactionName()) && "即时".equals(bean.getForwardName())) {
                                setScore(1, 2);
                            } else if ("篮球比分".equals(bean.getFunsactionName()) && "赛果".equals(bean.getForwardName())) {
                                setScore(2, 2);
                            } else if ("篮球比分".equals(bean.getFunsactionName()) && "赛程".equals(bean.getForwardName())) {
                                setScore(3, 2);
                            } else if ("篮球比分".equals(bean.getFunsactionName()) && "关注".equals(bean.getForwardName())) {
                                setScore(4, 2);
                            } else if ("篮球猜球".equals(bean.getFunsactionName()) && "滚球".equals(bean.getForwardName())) {
                                setBasketball(1);
                            } else if ("篮球猜球".equals(bean.getFunsactionName()) && "今日赛事".equals(bean.getForwardName())) {
                                setBasketball(2);
                            } else if ("篮球猜球".equals(bean.getFunsactionName()) && "早盘".equals(bean.getForwardName())) {
                                setBasketball(3);
                            } else if ("足球比分".equals(bean.getFunsactionName()) && "即时".equals(bean.getForwardName())) {
                                setScore(1, 1);
                            } else if ("足球比分".equals(bean.getFunsactionName()) && "赛果".equals(bean.getForwardName())) {
                                setScore(2, 1);
                            } else if ("足球比分".equals(bean.getFunsactionName()) && "赛程".equals(bean.getForwardName())) {
                                setScore(3, 1);
                            } else if ("足球比分".equals(bean.getFunsactionName()) && "关注".equals(bean.getForwardName())) {
                                setScore(4, 1);
                            } else if ("足球猜球".equals(bean.getFunsactionName()) && "滚球".equals(bean.getForwardName())) {
                                setFootball(1);
                            } else if ("足球猜球".equals(bean.getFunsactionName()) && "今日赛事".equals(bean.getForwardName())) {
                                setFootball(2);
                            } else if ("足球猜球".equals(bean.getFunsactionName()) && "早盘".equals(bean.getForwardName())) {
                                setFootball(3);
                            } else if ("娱乐活动".equals(bean.getFunsactionName()) || "优惠活动".equals(bean.getFunsactionName())) {
                                UIUtils.SendReRecevice(Constent.PREFERENTIAL_ACTIVITIES);
                            }
                        } else if (!TextUtils.isEmpty(bean.getFunsactionName())) {//主模块
                            if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                                intentLogin();
                            } else if ("设置中心".equals(bean.getFunsactionName())) {
                                Intent intent = new Intent(mContent, MoreSettingActivity.class);
                                startActivity(intent);
                            } else {
                                boolean isTourist = UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false);//是否是试玩

                                Intent intent = null;
                                if ("账户明细".equals(bean.getFunsactionName())) {
                                    if (isTourist) {
                                        ToastUtils.midToast(mContent, "试玩账号不能查看账户明细!", 0);
                                        return;
                                    }
                                    intent = new Intent(mContent, AccountDetailActivity.class);
                                } else if ("投注记录".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, BettingRecordActivity.class);
                                } else if ("充值记录".equals(bean.getFunsactionName())) {
                                    if (isTourist) {
                                        ToastUtils.midToast(mContent, "试玩账号不能查看充值记录!", 0);
                                        return;
                                    }
                                    intent = new Intent(mContent, RechargeDetailActivity.class);
                                } else if ("提款记录".equals(bean.getFunsactionName())) {
                                    if (isTourist) {
                                        ToastUtils.midToast(mContent, "试玩账号不能查看提款记录!", 0);
                                        return;
                                    }
                                    intent = new Intent(mContent, WithDrawDentailActivity.class);
                                } else if ("代理中心".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, ProxyCenterActivity.class);
                                } else if ("意见反馈".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, SuggestionActivity.class);
                                } else if ("消息中心".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, MyMessageActivity.class);
                                } else if ("新手帮助".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, WebActivity.class);
                                    intent.putExtra("url", Constants.NEWPEOPLEHELPURL);
                                } else if ("在线客服".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, OnLineServerActivity.class);
                                } else if ("领币".equals(bean.getFunsactionName())) {
                                    intent = new Intent(mContent, HomeFreeGlodActivity.class);
                                } else if ("充值".equals(bean.getFunsactionName())) {
                                    if (isTourist) {
                                        ToastUtils.midToast(mContent, "试玩账号不能进行充值!", 0);
                                        return;
                                    }
                                    intent = new Intent(mContent, NewRechargeActivity.class);
                                } else if ("提现".equals(bean.getFunsactionName())) {
                                    if (isTourist) {
                                        ToastUtils.midToast(mContent, "试玩账号不能进行提现!", 0);
                                        return;
                                    }
                                    if (UIUtils.getSputils().getBoolean(Constent.IS_BIND_CARD, false)) {
                                        intent = new Intent(mContent, WithDrawActivity.class);
                                    } else {
                                        intent = new Intent(mContent, NewBindBankCardActivity.class);
                                    }
                                }

                                if (intent != null) {
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }
            });
            return iv;
        }
    }

    private void intentLogin() {
        Intent mIntent = new Intent(mContent, LoginActivity.class);
        startActivity(mIntent);
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
}

