package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.HisAdapter;
import com.dading.ssqs.adapter.MyBettingAdapter;
import com.dading.ssqs.adapter.MyBettingAdapterNoFinish;
import com.dading.ssqs.adapter.MyBettingMAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.BettingMBean;
import com.dading.ssqs.bean.BettingTBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ThreadPoolUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 16:59
 * 描述	      ${投注机滤}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BettingRecordActivity extends BaseActivity implements ExpandableListView.OnGroupClickListener {
    private static final String TAG = "BettingRecordActivity";
    @Bind(R.id.betting_record_near)
    RadioButton mBettingRecordNear;
    @Bind(R.id.betting_record_gp)
    RadioGroup mBettingRecordGp;

    @Bind(R.id.betting_record_start_time)
    TextView mBettingRecordStartTime;
    @Bind(R.id.betting_record_end_time)
    TextView mBettingRecordEndTime;
    @Bind(R.id.betting_record_history_title_ly)
    LinearLayout mBettingRecordHistoryTitleLy;
    @Bind(R.id.betting_record_line)
    View mBettingRecordLine;

    @Bind(R.id.betting_record_by_time)
    CheckBox mBettingRecordByTime;
    @Bind(R.id.betting_record_by_play)
    CheckBox mBettingRecordByPlay;
    @Bind(R.id.betting_record_near_title_ly)
    LinearLayout mBettingRecordNearTitleLy;
    @Bind(R.id.betting_record_ex)
    ExpandableListView mBettingRecordExpand;
    @Bind(R.id.ex_no_data)
    RelativeLayout mBettingRecordENo;
    @Bind(R.id.data_empty)
    RelativeLayout mBettingRecordNo;
    @Bind(R.id.betting_record_listview)
    PullToRefreshListView listView;
    @Bind(R.id.betting_record_line_top2)
    View line1;
    @Bind(R.id.betting_record_fb_gp)
    RadioGroup mBettingRecordFbGp;
    private View mCanlendarView;
    private PopupWindow mPop;
    private TextView mClear;
    private TextView mClose;
    private TextView mToday;
    private CalendarView calendarView;
    private long mLongDate;
    private long mLongDateToday1;
    private SimpleDateFormat mSdf;
    private Date mDate;
    private String mTodayDate;
    private int mCanlendarTag;
    private String mSevenAgo;
    private String mStart;
    private String mEnd;
    private int mType;
    private Calendar mCalendar;

    @Override
    public void initView() {
        mCanlendarView = View.inflate(this, R.layout.pop_candlar, null);
        calendarView = (CalendarView) mCanlendarView.findViewById(R.id.betting_pop_calendar);
        mClear = (TextView) mCanlendarView.findViewById(R.id.betting_pop_calendar_clear);
        mClose = (TextView) mCanlendarView.findViewById(R.id.betting_pop_calendar_close);
        mToday = (TextView) mCanlendarView.findViewById(R.id.betting_pop_calendar_today);
        mPop = PopUtil.popuMakemm(mCanlendarView);
        this.listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        this.listView.setEmptyView(mBettingRecordNo);
        mBettingRecordExpand.setEmptyView(mBettingRecordENo);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_touzhu_record;
    }

    @Override
    protected void initData() {
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);
        mBettingRecordFbGp.check(b ? R.id.betting_record_football : R.id.betting_record_basketball);

        mCalendar = Calendar.getInstance();

        ThreadPoolUtils.getInstance().addTask(new Runnable() {
            @Override
            public void run() {
                // 获取网络时间，这里获取中科院的时间
                try {
                    URL url = new URL("http://www.ntsc.ac.cn");
                    URLConnection uc = url.openConnection();
                    final long networkTime = uc.getDate();

                    Date date = new Date(networkTime);
                    mCalendar.setTime(date);//校验时间
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mType = 1;
        mDate = new Date();
        //先把字符串转成Date类型
        String template = "yyyy-MM-dd";
        mSdf = new SimpleDateFormat(template, Locale.CHINA);
        int add = -90;
        int addSeven = -7;
        //三月前
        String threeAgo = DateUtils.getCurTimeAddND(add, template);
        mSevenAgo = DateUtils.getCurTimeAddND(addSeven, template);
        mTodayDate = DateUtils.getCurTime(template);

        mEnd = mTodayDate.replaceAll("-", "");
        mStart = mSevenAgo.replaceAll("-", "");

        mBettingRecordStartTime.setText(mSevenAgo);
        mBettingRecordEndTime.setText(mTodayDate);

        Date date = null;
        try {
            date = mSdf.parse(threeAgo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取毫秒数
        if (date != null)
            mLongDate = date.getTime();

        assert calendarView != null;
        calendarView.setMinDate(mLongDate);//设置最小时间毫秒

        // 预订结束日期
        mCalendar.set(Calendar.HOUR_OF_DAY, 23);
        mCalendar.set(Calendar.MINUTE, 59);
        mCalendar.set(Calendar.SECOND, 59);

        long lateTime = mCalendar.getTimeInMillis();
        calendarView.setMaxDate(lateTime);//设置最大时间

        calendarView.setWeekSeparatorLineColor(this.getResources().getColor(R.color.colorAccent));
        /**
         * /v1.0/payBall/recent/type/{type}
         b)	请求方式:get
         c)	请求参数说明：auth_token：登陆后加入请求头
         type：类型
         1 : 时间    2 : 场次    篮球:/v1.0/payBall/ball/recent/type/{type}
         */
        mBettingRecordByTime.setChecked(true);
        mBettingRecordByPlay.setChecked(false);
        mBettingRecordENo.setVisibility(View.GONE);
        getByTime();
        mBettingRecordNear.setChecked(true);
        mBettingRecordNearTitleLy.setVisibility(View.VISIBLE);
        mBettingRecordHistoryTitleLy.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        mBettingRecordExpand.setOnGroupClickListener(this);
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mBettingRecordStartTime.setClickable(true);
                mBettingRecordEndTime.setClickable(true);
            }
        });
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mType == 1) {
                    requestNear();
                } else if (mType == 2) {
                    requestNoFinish();
                } else if (mType == 3) {
                    getHistoryBetting();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                if (mCanlendarTag == 1) {
                    mBettingRecordStartTime.setText(date);
                    String[] splitend = date.split("-");
                    /**
                     * 转换成19910112类型
                     */
                    StringBuilder buffer = new StringBuilder();
                    for (String s : splitend) {
                        if (s.length() >= 2) {
                            buffer.append(s);
                        } else {
                            s = "0" + s;
                            buffer.append(s);
                        }
                    }
                    mStart = buffer.toString().replaceAll("-", "");
                } else if (mCanlendarTag == 2) {
                    mBettingRecordEndTime.setText(date);
                    String[] splitend = date.split("-");
                    StringBuilder buffer = new StringBuilder();
                    for (String s : splitend) {
                        if (s.length() >= 2) {
                            buffer.append(s);
                        } else {
                            s = "0" + s;
                            buffer.append(s);
                        }
                    }
                    mEnd = buffer.toString().replaceAll("-", "");
                }
                mPop.dismiss();
            }
        });
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanlendarTag == 1) {
                    mBettingRecordStartTime.setText("");
                    mStart = "";
                } else if (mCanlendarTag == 2) {
                    mBettingRecordEndTime.setText("");
                    mEnd = "";
                }
                mPop.dismiss();
            }
        });
        mToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setDate(mLongDateToday1);
                if (mCanlendarTag == 1) {
                    mBettingRecordStartTime.setText(mTodayDate);
                    mStart = mTodayDate.replaceAll("-", "");
                } else if (mCanlendarTag == 2) {
                    mBettingRecordEndTime.setText(mTodayDate);
                    mEnd = mTodayDate.replaceAll("-", "");
                }
                mPop.dismiss();
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanlendarTag == 1) {
                    mPop.dismiss();
                } else if (mCanlendarTag == 2) {
                    mPop.dismiss();
                }
            }
        });
        mBettingRecordFbGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.betting_record_football:
                        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
                        Logger.d(TAG, "投注记录------------------------------足球:");
                        break;
                    case R.id.betting_record_basketball:
                        UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, false);
                        Logger.d(TAG, "投注记录------------------------------篮球:");
                        break;
                    default:
                        break;
                }
                switch (mType) {
                    case 1:
                        requestNear();
                        break;
                    case 2:
                        requestNoFinish();
                        break;
                    case 3:
                        getHistoryBetting();
                        break;
                    case 4:
                        requestDate();
                        break;
                    default:
                        break;
                }
            }
        });
        mBettingRecordGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.betting_record_near:
                        mType = 1;
                        requestNear();
                        break;
                    case R.id.betting_record_no_finish:
                        mType = 2;
                        requestNoFinish();
                        break;
                    case R.id.betting_record_history:
                        mType = 3;
                        getHistoryBetting();
                        break;
                }
            }
        });
    }

    @OnClick({R.id.betting_record_back, R.id.betting_record_by_time, R.id.betting_record_by_play,
            R.id.betting_record_start_time, R.id.betting_record_end_time, R.id.betting_record_look_for})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.betting_record_back:
                finish();
                break;
            case R.id.betting_record_look_for:
                getHistoryBetting();
                break;
            case R.id.betting_record_by_time:
                mBettingRecordEndTime.setClickable(false);
                mBettingRecordByPlay.setClickable(true);
                getByTime();
                break;
            case R.id.betting_record_by_play:
                mBettingRecordEndTime.setClickable(true);
                mBettingRecordByPlay.setClickable(false);
                getByPlay();
                break;
            case R.id.betting_record_start_time:
                mBettingRecordEndTime.setClickable(false);
                mBettingRecordByPlay.setClickable(false);

                mCanlendarTag = 1;
                String sToaday = mBettingRecordStartTime.getText().toString();

                String[] split = sToaday.split("-");
                StringBuilder buffer = new StringBuilder();
                for (String s : split) {
                    if (s.length() >= 2) {
                        buffer.append(s);
                    } else {
                        s = "0" + s;
                        buffer.append(s);
                    }
                }
                if (!buffer.toString().equals("")) {
                    try {
                        Date date = mSdf.parse(sToaday);
                        mLongDateToday1 = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    mLongDateToday1 = mDate.getTime();
                }
                mPop.showAsDropDown(mBettingRecordLine, 0, 0);
                mBettingRecordStartTime.setClickable(false);
                mBettingRecordEndTime.setClickable(false);
                calendarView.setDate(mLongDateToday1);

                break;
            case R.id.betting_record_end_time:
                mCanlendarTag = 2;
                mBettingRecordStartTime.setClickable(false);
                Logger.d(TAG, "结束日期被点击了------------------------------:");
                String eToaday = mBettingRecordEndTime.getText().toString();
                String[] splitend = eToaday.split("-");
                StringBuilder bufferend = new StringBuilder();
                for (String s : splitend) {
                    if (s.length() >= 2) {
                        bufferend.append(s);
                    } else {
                        s = "0" + s;
                        bufferend.append(s);
                    }
                }
                if (!bufferend.toString().equals("")) {
                    try {
                        Date date = mSdf.parse(eToaday);
                        mLongDateToday1 = date.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    mLongDateToday1 = mDate.getTime();
                }
                mPop.showAsDropDown(mBettingRecordHistoryTitleLy, 0, 0);
                mBettingRecordStartTime.setClickable(false);
                mBettingRecordEndTime.setClickable(false);
                calendarView.setDate(mLongDateToday1);
                break;
            default:
                break;
        }
    }

    private void requestDate() {
        /**
         * /v1.0/payBall/history/startDate/{startDate}/endDate/{endDate}
         b)	请求方式:
         get
         c)	请求参数说明：
         startDate:开始时间
         endDate:结束时间
         时间格式{yyyyMMdd}
         */
        SSQSApplication.apiClient(classGuid).getMyPayBallHistoryList(mBettingRecordStartTime.getText().toString(), mBettingRecordEndTime.getText().toString(), new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<BettingTBean> items = (List<BettingTBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        processDataNoFinish(items);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(BettingRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(BettingRecordActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void requestNoFinish() {
        mBettingRecordHistoryTitleLy.setVisibility(View.GONE);
        mBettingRecordNearTitleLy.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        SSQSApplication.apiClient(classGuid).getMyPayBallUnChecked(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                listView.onRefreshComplete();

                if (result.isOk()) {
                    List<BettingTBean> items = (List<BettingTBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        processDataNoFinish(items);
                    } else {
                        mBettingRecordENo.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(BettingRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(BettingRecordActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void requestNear() {
        line1.setVisibility(View.VISIBLE);
        mBettingRecordNearTitleLy.setVisibility(View.VISIBLE);
        mBettingRecordHistoryTitleLy.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);

        if (mBettingRecordByTime.isChecked()) {
            getByTime();
        } else {
            getByPlay();
        }
    }

    private void getHistoryBetting() {
        mBettingRecordNearTitleLy.setVisibility(View.GONE);
        mBettingRecordExpand.setVisibility(View.GONE);
        mBettingRecordHistoryTitleLy.setVisibility(View.VISIBLE);

        SSQSApplication.apiClient(classGuid).getMyPayBallHistoryList(mStart, mEnd, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                listView.onRefreshComplete();

                if (result.isOk()) {
                    List<BettingTBean> items = (List<BettingTBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        processDataHis(items);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(BettingRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(BettingRecordActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processDataHis(List<BettingTBean> hisBean) {
        line1.setVisibility(View.VISIBLE);
        if (hisBean.size() >= 0) {
            mBettingRecordExpand.setAdapter(new MyBettingAdapterNoFinish(this, hisBean));
            mBettingRecordExpand.setVisibility(View.GONE);
            mBettingRecordENo.setVisibility(View.GONE);
            ArrayList<BettingTBean.PayDetailsEntity> list = new ArrayList<>();
            for (BettingTBean d : hisBean) {
                for (BettingTBean.PayDetailsEntity payDetailsEntity : d.payDetails) {
                    list.add(payDetailsEntity);
                }
            }
            listView.setAdapter(new HisAdapter(this, list));
            listView.setVisibility(View.VISIBLE);
        } else {
            mBettingRecordExpand.setVisibility(View.GONE);
        }

    }

    private void processDataNoFinish(List<BettingTBean> noBean) {
        line1.setVisibility(View.GONE);
        if (noBean.size() >= 0) {
            ArrayList<BettingTBean.PayDetailsEntity> list = new ArrayList<>();
            for (BettingTBean d : noBean) {
                for (BettingTBean.PayDetailsEntity payDetailsEntity : d.payDetails) {
                    list.add(payDetailsEntity);
                }
            }
            mBettingRecordExpand.setAdapter(new MyBettingAdapterNoFinish(this, noBean));
            mBettingRecordExpand.setVisibility(View.GONE);
            mBettingRecordENo.setVisibility(View.GONE);
            listView.setAdapter(new HisAdapter(this, list));
            listView.setVisibility(View.VISIBLE);
        } else {
            mBettingRecordExpand.setVisibility(View.GONE);
        }
    }

    private void getByTime() {
        SSQSApplication.apiClient(classGuid).getRecentType(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                listView.onRefreshComplete();
                if (result.isOk()) {
                    List<BettingTBean> items = (List<BettingTBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        processData(items);
                    } else {
                        mBettingRecordENo.setVisibility(View.VISIBLE);
                    }
                } else {
                    mBettingRecordENo.setVisibility(View.VISIBLE);
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(BettingRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(BettingRecordActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
        mBettingRecordByPlay.setChecked(false);
        mBettingRecordByTime.setChecked(true);
    }

    private void getByPlay() {
        SSQSApplication.apiClient(classGuid).getRecentType2(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                listView.onRefreshComplete();
                if (result.isOk()) {
                    List<BettingMBean> items = (List<BettingMBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        mBettingRecordENo.setVisibility(View.GONE);
                        processDataM(items);
                    } else {
                        mBettingRecordENo.setVisibility(View.VISIBLE);
                    }
                } else {
                    mBettingRecordENo.setVisibility(View.VISIBLE);
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(BettingRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(BettingRecordActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });

        mBettingRecordByPlay.setChecked(true);
        mBettingRecordByTime.setChecked(false);
    }

    private void processDataM(List<BettingMBean> bean) {
        line1.setVisibility(View.VISIBLE);
        mBettingRecordExpand.setVisibility(View.VISIBLE);
        mBettingRecordExpand.setAdapter(new MyBettingMAdapter(this, bean));

    }

    private void processData(List<BettingTBean> tBean) {
        line1.setVisibility(View.VISIBLE);
        mBettingRecordExpand.setVisibility(View.VISIBLE);
        mBettingRecordExpand.setAdapter(new MyBettingAdapter(this, tBean));
    }

    int mCurrPosition = -1;

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (mCurrPosition == -1) {
            //等于-1说明没有打开的组,就打开这个组expand
            mBettingRecordExpand.expandGroup(groupPosition);
            mCurrPosition = groupPosition;

        } else {
            //进来这里说明已经有组已经打开,判断是否再次点击的是同一个组
            if (mCurrPosition == groupPosition) {
                mBettingRecordExpand.collapseGroup(groupPosition);
                mCurrPosition = -1;
            } else {
                mBettingRecordExpand.expandGroup(groupPosition);
                mBettingRecordExpand.collapseGroup(mCurrPosition);
                //选择那个组让他在第一位
                mBettingRecordExpand.setSelectedGroup(groupPosition);
                mCurrPosition = groupPosition;
            }
        }
        return true;
    }
}
