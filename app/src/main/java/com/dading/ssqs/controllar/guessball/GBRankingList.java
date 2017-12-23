package com.dading.ssqs.controllar.guessball;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyRankingListAdapter;
import com.dading.ssqs.adapter.MyRankingListAdapter2;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.RankingBean;
import com.dading.ssqs.bean.RankingBean2;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.CalendarUtil;

import java.util.ArrayList;
import java.util.List;

import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/7 11:40
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBRankingList extends BaseTabsContainer implements View.OnClickListener {

    private static final String TAG = "GBRankingList";
    private PullToRefreshListView mRankingList;
    private RadioGroup mRankingListRg;
    private ImageView mRankingListPic;
    private int mPage;
    private RadioButton mRankingRg1;
    private int mTotalPage;
    private List<RankingBean> mItems;
    private TextView mRankingGlodPrize;
    private TextView mRankingBt1;
    private TextView mRankingCalandar;
    private ImageView mRankingLeftArr;
    private ImageView mRankingRightArr;
    private LinearLayout mRankingCalandarLy;
    private ArrayList<String> mList;
    private int mDateTAG;
    private ArrayList<String> mListTime;
    private String mTime;
    private int mCheckID;

    private List<RankingBean2.OrdersEntity> mOrders;
    private LinearLayout mRankingLeftArrLy;
    private LinearLayout mRankingRightArrLy;
    private ImageView mRankingBack;
    private TextView mRankingTitle;
    private Runnable mTask;


    private void getData(final int page) {

        CalendarUtil util = new CalendarUtil();
        String next = util.getNextMonday();//周一
        Logger.d("ssss", "返回数据是------------------------------:" + next);
        String year = next.replace("年", "");
        String month = year.replace("月", ".");
        String day = month.replace("日", "");
        String nextMonday = day.substring(4);
        String[] s = nextMonday.split("\\.");
        Logger.d("ssss", "返回数据是--------:" + nextMonday + "-------" + s.length);


        if (s[0].length() < 2) {
            s[0] = "0" + s[0];
        }
        if (s[1].length() < 2) {
            s[1] = "0" + s[1];
        }

        String sDay = s[0] + "." + s[1];
        String mondayOFWeek = util.getMondayOFWeek("MM.dd");//周一
        String sundayTime = util.getSunday("MM.dd");//现在
        String pre1Weekday = util.getPreviousWeekday("MM.dd");//上周一
        String pre1WeekSunday = util.getPreviousWeekSunday("MM.dd");//上周日
        String pre2Weekday = util.getPre2Weekday("MM.dd");//上上周一
        String pre2WeekSunday = util.getPre2WeekSunday("MM.dd");//上上周日
        String pre3Weekday = util.getPre3Weekday("MM.dd");//上上上周一
        String pre3WeekSunday = util.getPre3WeekSunday("MM.dd");//上上上周日

        mList = new ArrayList<>();
        mListTime = new ArrayList<>();
        mListTime.add(mondayOFWeek + "-" + sDay);
        mListTime.add(pre1Weekday + "-" + mondayOFWeek);
        mListTime.add(pre2Weekday + "-" + pre2WeekSunday);
        mListTime.add(pre3Weekday + "-" + pre3WeekSunday);

        mList.add("本周(" + mondayOFWeek + " - " + sDay + ")");
        mList.add("上周(" + pre1Weekday + " - " + mondayOFWeek + ")");
        mList.add("(" + pre2Weekday + " - " + pre1Weekday + ")");
        mList.add("(" + pre3Weekday + " - " + pre2Weekday + ")");

        mTime = mondayOFWeek + "-" + pre1WeekSunday;
        mCheckID = 0;
        mListTime.add(mondayOFWeek);

        SSQSApplication.apiClient(0).getMyOrderList(page, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultRankingPage resultRankingPage = (CcApiResult.ResultRankingPage) result.getData();

                    if (resultRankingPage != null) {
                        mTotalPage = resultRankingPage.getTotalPage();
                        if (resultRankingPage.getItems() != null) {
                            mItems = resultRankingPage.getItems();
                            processData(resultRankingPage.getItems());
                        }
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "失败信息");
                }
            }
        });
        mRankingList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);//上拉加载
    }


    @Override
    protected void initListener() {
        mRankingBack.setOnClickListener(this);
        mRankingLeftArrLy.setOnClickListener(this);
        mRankingRightArrLy.setOnClickListener(this);
        mRankingListRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.guessball_ranking_list_rg_rb1:
                        mRankingList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                        mRankingCalandarLy.setVisibility(View.GONE);
                        mRankingBt1.setVisibility(View.GONE);

                        mRankingGlodPrize.setText("财富");

                        SSQSApplication.apiClient(0).getMyOrderList(1, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    CcApiResult.ResultRankingPage page = (CcApiResult.ResultRankingPage) result.getData();

                                    if (page != null) {
                                        mTotalPage = page.getTotalPage();
                                        if (page.getItems() != null) {
                                            mItems = page.getItems();
                                            processData(mItems);
                                        }
                                    } else {
                                        Logger.d(TAG, result.getMessage() + "失败信息");
                                    }
                                }
                            }
                        });
                        break;
                    case R.id.guessball_ranking_list_rg_rb2:
                        mRankingList.setMode(PullToRefreshBase.Mode.DISABLED);
                        mRankingBt1.setVisibility(View.VISIBLE);
                        mRankingBt1.setText(mContent.getResources().

                                getString(R.string.ranking_profit_bottom));
                        mCheckID = 2;
                        mRankingCalandarLy.setVisibility(View.VISIBLE);
                        mRankingGlodPrize.setText(mContent.getString(R.string.gain_num));


                        mDateTAG = 0;
                        mTime = mListTime.get(mDateTAG);
                        mRankingRightArr.setVisibility(View.GONE);
                        mRankingLeftArr.setVisibility(View.VISIBLE);
                        mRankingCalandar.setText(mList.get(mDateTAG));

                        /**
                         * 2:盈利榜 p3.新人王 4.胜率榜
                         * /v1.0/order/type/{type}/time/{time}
                         b)	请求方式:
                         get
                         c)	请求参数说明：
                         type：类型- 2:盈利榜3:新人王4：胜率榜5:每日新秀榜6：每日高手榜
                         7：每日英雄榜8：淘汰赛9：小组赛
                         time:	2-4 时间格式为MM.dd-MM.dd 如："08.22-08.29",
                         5-7时间格式为MM.dd 如："08.22"
                         auth_token：登陆后加入请求头
                         */
                        getYGXS(mCheckID, mTime);
                        break;
                    case R.id.guessball_ranking_list_rg_rb4:
                        mRankingBt1.setVisibility(View.VISIBLE);
                        mRankingBt1.setText(mContent.getResources().

                                getString(R.string.ranking_green_people_bottom));
                        mRankingList.setMode(PullToRefreshBase.Mode.DISABLED);
                        mRankingGlodPrize.setText(mContent.getString(R.string.gain_num));


                        mCheckID = 3;
                        mRankingGlodPrize.setText(mContent.getString(R.string.glod));
                        mRankingCalandarLy.setVisibility(View.VISIBLE);
                        mDateTAG = 0;
                        mTime = mListTime.get(mDateTAG);
                        mRankingRightArr.setVisibility(View.GONE);
                        mRankingLeftArr.setVisibility(View.VISIBLE);
                        mRankingCalandar.setText(mList.get(mDateTAG));

                        getYGXS(mCheckID, mTime);
                        break;
                    case R.id.guessball_ranking_list_rg_rb5:
                        mRankingBt1.setVisibility(View.VISIBLE);
                        mRankingList.setMode(PullToRefreshBase.Mode.DISABLED);
                        mRankingGlodPrize.setText(mContent.getString(R.string.win_lv_win));


                        mRankingBt1.setText(mContent.getResources().

                                getString(R.string.ranking_winning_bottom));
                        mCheckID = 4;
                        mRankingCalandarLy.setVisibility(View.VISIBLE);
                        mDateTAG = 0;
                        mTime = mListTime.get(mDateTAG);
                        mRankingRightArr.setVisibility(View.GONE);
                        mRankingLeftArr.setVisibility(View.VISIBLE);
                        mRankingCalandar.setText(mList.get(mDateTAG));

                        getYGXS(mCheckID, mTime);
                        break;
                    default:
                        break;
                }
            }
        });
        mRankingList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>()

        {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                if (mRankingRg1.isChecked()) {
                    if (mPage < mTotalPage) {
                        ++mPage;

                        SSQSApplication.apiClient(0).getMyOrderList(mPage, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                mRankingList.onRefreshComplete();

                                if (result.isOk()) {
                                    CcApiResult.ResultRankingPage page = (CcApiResult.ResultRankingPage) result.getData();

                                    if (page != null) {

                                        if (page.getItems() != null) {
                                            int size = mItems.size();

                                            mItems.addAll(page.getItems());
                                            mRankingList.setAdapter(new MyRankingListAdapter(mContent, mItems));
                                            mRankingList.getRefreshableView().setSelection(size);
                                            mRankingList.onRefreshComplete();
                                        }
                                    }
                                } else {
                                    ToastUtils.midToast(UIUtils.getContext(), "上拉加载失败", 0);
                                }
                            }
                        });
                    } else {
                        mTask = new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.midToast(mContent, "没有更多数据", 0);
                                mRankingList.onRefreshComplete();
                            }
                        };
                        UIUtils.postTaskDelay(mTask, 500);
                    }
                }
            }
        });
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.removeTask(mTask);
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mContenLy.setVisibility(View.GONE);
    }

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(mContent, R.layout.gb_ranking_list, null);
        mRankingList = (PullToRefreshListView) view.findViewById(R.id.guessball_ranking_list);
        mRankingBack = (ImageView) view.findViewById(R.id.top_back);
        mRankingTitle = (TextView) view.findViewById(R.id.top_title);

        View headView = View.inflate(mContent, R.layout.ranking_head, null);

        mRankingGlodPrize = (TextView) headView.findViewById(R.id.ranking_glod_prize);
        mRankingListPic = (ImageView) headView.findViewById(R.id.guessball_ranking_list_pic);//list上面活动图
        mRankingListRg = (RadioGroup) headView.findViewById(R.id.guessball_ranking_list_rg);
        mRankingRg1 = (RadioButton) headView.findViewById(R.id.guessball_ranking_list_rg_rb1);

        mRankingListPic.setVisibility(View.GONE);

        mRankingCalandarLy = (LinearLayout) headView.findViewById(R.id.ranking_calandar_ly);
        mRankingCalandar = (TextView) headView.findViewById(R.id.ranking_calandar_text);
        mRankingLeftArr = (ImageView) headView.findViewById(R.id.ranking_left_arr);
        mRankingRightArr = (ImageView) headView.findViewById(R.id.ranking_right_arr);
        mRankingLeftArrLy = (LinearLayout) headView.findViewById(R.id.ranking_left_arr_ly);
        mRankingRightArrLy = (LinearLayout) headView.findViewById(R.id.ranking_right_arr_ly);

        View footView = View.inflate(mContent, R.layout.ranking_foot, null);

        mRankingBt1 = (TextView) footView.findViewById(R.id.green_hand_bottom1);

        mRankingList.getRefreshableView().addHeaderView(headView);
        mRankingList.getRefreshableView().addFooterView(footView);

        return view;
    }

    @Override
    public void initData() {
        mRankingTitle.setText(mContent.getString(R.string.ranking_list));
        /**
         * a)	请求地址：
         /v1.0/order/type/{type}/time/{time}/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：类型- 2:盈利榜3:新人王4：胜率榜5:每日新秀榜6：每日高手榜
         7：每日英雄榜8：淘汰赛9：小组赛
         time:	2-4 时间格式为MM.dd-MM.dd 如："08.22-08.29",
         5-7时间格式为MM.dd 如："08.22"
         page:第几页
         count:页数
         auth_token：登陆后加入请求头
         */
        mDateTAG = 0;

        mTotalPage = 1;
        mPage = 1;
        getData(mPage);
        mRankingCalandarLy.setVisibility(View.GONE);
        mRankingRg1.setChecked(true);
        //抢占焦点
        mRankingList.setFocusable(false);
        mRankingList.setFocusableInTouchMode(true);
        mRankingList.requestFocus();
        mRankingList.scrollTo(0, 20);
    }

    private void getYGXS(final int checkID, String time) {

        SSQSApplication.apiClient(0).getMyYLlist(checkID, time, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    RankingBean2 bean = (RankingBean2) result.getData();

                    if (bean != null) {
                        mOrders = bean.orders;
                        mRankingList.setAdapter(new MyRankingListAdapter2(mContent, mOrders, checkID));
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "盈利榜失败信息");
                }
            }
        });
    }

    private void processData(List<RankingBean> bean) {
        mRankingRightArr.setVisibility(View.GONE);
        mRankingList.setAdapter(new MyRankingListAdapter(mContent, bean));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                UIUtils.SendReRecevice(Constent.LOADING_HOME);
                break;
            case R.id.ranking_right_arr_ly:
                Logger.d(TAG, "右边点击------------------------------:");
                mRankingLeftArr.setVisibility(View.VISIBLE);
                mRankingLeftArrLy.setClickable(true);
                if (mDateTAG <= 0) {
                    return;
                } else {
                    mDateTAG--;
                    mTime = mListTime.get(mDateTAG);
                    Logger.d(TAG, "第几个日期------------------------------:" + mDateTAG);
                    mRankingCalandar.setText(mList.get(mDateTAG));
                    getYGXS(mCheckID, mTime);
                    if (mDateTAG <= 0) {
                        mRankingRightArr.setVisibility(View.GONE);
                        mRankingRightArrLy.setClickable(false);
                    }
                }
                break;
            case R.id.ranking_left_arr_ly:
                mRankingRightArr.setVisibility(View.VISIBLE);
                mRankingRightArrLy.setClickable(true);
                Logger.d(TAG, "左边点击------------------------------:");
                if (mDateTAG >= 3) {
                    return;
                } else {
                    mDateTAG++;
                    mTime = mListTime.get(mDateTAG);
                    Logger.d(TAG, "第几个日期------------------------------:" + mDateTAG);
                    mRankingCalandar.setText(mList.get(mDateTAG));
                    getYGXS(mCheckID, mTime);
                    if (mDateTAG >= mList.size() - 1) {
                        mRankingLeftArr.setVisibility(View.GONE);
                        mRankingLeftArrLy.setClickable(false);
                    }
                }
                break;
        }
    }
}
