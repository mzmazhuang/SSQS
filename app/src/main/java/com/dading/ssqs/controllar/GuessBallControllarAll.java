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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.GreenhandHelpActivity;
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.activity.OnLineServerActivity;
import com.dading.ssqs.adapter.BaseMePagerAdapter;
import com.dading.ssqs.adapter.HomeBasketballAdapter;
import com.dading.ssqs.adapter.HomeMatchAdapter;
import com.dading.ssqs.adapter.MyRankingListHAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.bean.HomeMessageBean;
import com.dading.ssqs.components.swipetoloadlayout.OnLoadMoreListener;
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener;
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.AutoVerticalScrollTextView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshScrollView;

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
    private ListView mHome_lv_information;
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
    private TextView mRankMore;
    private View mFootballView;
    private View mBasketballView;
    private ImageView mImRanking;
    private TextView mHome_vp_title;
    private ArrayList<String> mHVpTtile;

    private SwipeToLoadLayout swipeToLoadLayout;

    private HomeVpCircleAdapter homeVpCircleAdapter;
    private HomeMatchAdapter homeMatchAdapter;
    private MyRankingListHAdapter myRankingListHAdapter;

    private void processDataMessage(List<HomeMessageBean> bean) {
        listScrollDown();
        if (bean != null) {
            mData = bean;
            if (number < mData.size()) {
                mHome_win_text.setText(mData.get(number).content);
            }

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    listScrollOff();
                    UIUtils.postTaskDelay(run_scroll_up, 0);
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
            //mHandler.removeCallbacksAndMessages(null);
            UIUtils.removeTask(r);
            UIUtils.postTaskDelay(r, 1500);
        }
    }

    /**
     * des 只要继承了该container就能得到带数据的view视图
     * date 2016-4-7 下午4:19:22
     */
    @Override
    public View initContentView(Context content) {
        View view = View.inflate(mContent, R.layout.guessball_home_come, null);

        mImRanking = (ImageView) view.findViewById(R.id.home_ranking);
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
        mHome_lv_information = (ListView) view.findViewById(R.id.home_listView_information);//排行榜

        mFreeGlod = (LinearLayout) view.findViewById(R.id.home_four_free_glod);
        mLuckLotto = (LinearLayout) view.findViewById(R.id.home_four_luck_lotto);
        mYpRfer = (LinearLayout) view.findViewById(R.id.home_four_yp_refer);
        mRankWin = (LinearLayout) view.findViewById(R.id.home_four_rank_win);
        mGreenhandHelp = (LinearLayout) view.findViewById(R.id.home_four_greenhand_help);

        mMore = (TextView) view.findViewById(R.id.home_activity_more);
        mReferMore = (TextView) view.findViewById(R.id.home_referr_more);
        mRankMore = (TextView) view.findViewById(R.id.home_rank_more);
        mFootballView = view.findViewById(R.id.home_football_view);
        mBasketballView = view.findViewById(R.id.home_basketball_view);

        /**
         * 四组件
         */
        mHome_win_text = (AutoVerticalScrollTextView) view.findViewById(R.id.home_win_text);

        //防止跳转listview上
        mHome_sc_scrollView_ly.setFocusable(false);
        mHome_sc_scrollView_ly.setFocusableInTouchMode(true);
        mHome_sc_scrollView_ly.requestFocus();

        swipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swiptToLoadLayout);
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setRefreshEnabled(false);//初始先不能刷新

        homeVpCircleAdapter = new HomeVpCircleAdapter();
        mHome_vp_ciecle.setAdapter(homeVpCircleAdapter);

        homeMatchAdapter = new HomeMatchAdapter(mContent);
        mHome_lv_match.setAdapter(homeMatchAdapter);

        myRankingListHAdapter = new MyRankingListHAdapter(mContent);
        mHome_lv_information.setAdapter(myRankingListHAdapter);

        return view;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            handler.removeCallbacks(run_scroll_down);
            handler.removeCallbacks(run_scroll_up);
        }
    };

    /**
     * 向下滚动
     */
    public void listScrollDown() {
        listScrollOff();
        handler.postDelayed(run_scroll_down, 0);
    }

    /**
     * 停止滚动
     */
    public void listScrollOff() {
        handler.removeCallbacks(run_scroll_down);
        handler.removeCallbacks(run_scroll_up);
    }

    /**
     * 自身循环
     */
    Runnable run_scroll_up = new Runnable() {
        @Override
        public void run() {
            mHome_lv_information.smoothScrollBy(1, 10);
            handler.postDelayed(run_scroll_up, 10);
        }
    };

    Runnable run_scroll_down = new Runnable() {
        @Override
        public void run() {
            mHome_lv_information.smoothScrollBy(-1, 10);
            handler.postDelayed(run_scroll_down, 10);
        }
    };


    private void getData() {
        /**
         * 设置文字滑动
         * a)	请求地址：
         /v1.0/sysMessage/list
         b)	请求方式:
         get
         a)	请求地址：
         /v1.0/sysMessage/list
         b)	请求方式:
         get
         */

        SSQSApplication.apiClient(0).getSysMessageList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {

                swipeToLoadLayout.setRefreshing(false);
                swipeToLoadLayout.setRefreshEnabled(true);

                if (result.isOk()) {
                    List<HomeMessageBean> items = (List<HomeMessageBean>) result.getData();

                    if (items != null) {
                        processDataMessage(items);
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "中奖信息失败信息");
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
        /**
         * 正式数据
         */
        getData();
        boolean networkAvailable = AndroidUtilities.isNetworkAvailable(mContent);
        if (networkAvailable) {
            //首页活动请求数据
            getHOME(null);
        }
    }

    private void getHOME(final OnDoneListener listener) {
        mGuessFourLoading.setVisibility(UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false) ? View.GONE : View.VISIBLE);

        SSQSApplication.apiClient(0).getMainDataList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
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
                            TmtUtils.midToast(mContent, "请求失败,没有缓存数据,请检查网络拉取最新赛事", 0);
                        }
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
            for (final HomeBean.AdvertsBean bean : mAdverts) {
                if (bean != null) {
                    ImageView iv = new ImageView(mContent);
                    LogUtil.util(TAG, "图片地址:" + bean.getImageUrl());
                    SSQSApplication.glide.load(bean.getImageUrl()).error(R.mipmap.image_not).centerCrop().into(iv);

                    mHVP.add(iv);
                    mHVpTtile.add(bean.getName());
                }
            }

            homeVpCircleAdapter.setList(mHVP);

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
                    LogUtil.util(TAG, "home的viewpage标题-----" + mHVpTtile.get(0));
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
         * 排行榜
         */
        if (dataEntity.getOrders() != null) {
            List<HomeBean.OrdersBeanX.OrdersBean> orders = dataEntity.getOrders().getOrders();
            orders.addAll(orders);
            orders.addAll(orders);
            orders.addAll(orders);
            orders.addAll(orders);
            orders.addAll(orders);

            myRankingListHAdapter.setList(orders);

            listScrollOff();

            ListScrollUtil.setListViewHeightBasedOnChildren5(mHome_lv_information);
            UIUtils.postTaskDelay(run_scroll_up, 0);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        /**
         *  mFreeGlod  mLuckLotto  mYpRfer  mRankWin   mGreenhandHelp
         */
        mHome_lv_information.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIUtils.SendReRecevice(Constent.LOADING_RANKING);
            }
        });

        mImRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.SendReRecevice(Constent.LOADING_RANKING);
            }
        });
        mRankMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.SendReRecevice(Constent.LOADING_RANKING);
            }
        });
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
                LogUtil.util(TAG, "轮播图postion返回数据是------------------------------:" + position);
                for (int i = 0; i < mHVP.size(); i++) {
                    ImageView iv = (ImageView) mHome_vp_ciecle_dots.getChildAt(i);
                    if (iv != null)
                        iv.setImageResource(R.mipmap.er_cai);
                    if (i == position) {
                        if (iv != null)
                            iv.setImageResource(R.mipmap.grey);
                        mHome_vp_title.setText(mHVpTtile.get(position));
                        LogUtil.util(TAG, "home的viewpage标题-----" + mHVpTtile.get(position));
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
                Intent intent = new Intent(mContent, GreenhandHelpActivity.class);
                mContent.startActivity(intent);
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
        LogUtil.util("GBSS", "现在是-----:" + UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true));
        UIUtils.SendReRecevice(Constent.LOADING_GUESS_BALL);
    }

    private void setFootball() {
        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
        LogUtil.util("GBSS", "现在是-----:" + UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true));
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
                        LogUtil.util(TAG, "活动跳转返回数据id是----:" + mAdverts.get(position).getForwardID());
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
            LogUtil.util(TAG, "home接受到关注的通知------------------------:");
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
}

