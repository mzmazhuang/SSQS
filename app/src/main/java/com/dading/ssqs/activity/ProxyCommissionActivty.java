package com.dading.ssqs.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.ProxyCommissionLvAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.ProxyCmmsionsBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/8/8.
 */
public class ProxyCommissionActivty extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
    private static final String TAG = "ProxyCommissionActivty";
    @Bind(R.id.top_back)
    ImageView mTopBack;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;

    /*@Bind(R.id.templete_recycleview)
    RecyclerView mProxyCommissionLv;*/

    @Bind(R.id.proxy_comision_time_start_time)
    TextView mBettingRecordStartTime;
    @Bind(R.id.proxy_comision_time_end_time)
    TextView mBettingRecordEndTime;
    @Bind(R.id.proxy_comision_time_pop)
    TextView mProxyComisionTimePop;

    @Bind(R.id.data_empty)
    RelativeLayout mProxyComisionEmpty;

    @Bind(R.id.proxy_commission_lv)
    ListView mProxyCommissionLv;

    private View mCanlendarView;
    private CalendarView calendarView;
    private PopupWindow mPop;
    private int mCanlendarTag;
    private SimpleDateFormat mSdf;
    private String mStart;
    private String mEnd;
    private String mSevenAgo;
    private String mTodayDate;
    private View mView;
    private TextView mCalendarYear;
    private TextView mCalendarMonthDay;
    private TextView mCalendarCancle;
    private TextView mCalendarConfirm;
    private String mCalendarDate;
    private TextView mHeadType;
    private TextView mHeadCommNum;
    private TextView mHeadFreeNum;
    private View mHeadview;
    private Calendar mCalendar;
    private View mTodayPop;
    private TextView mToday;
    private TextView mYesterDay;
    private TextView mWeek;
    private TextView mHalfMonth;
    private String mTodayUrl;
    private String mSevenUrl;
    private String mHalfMonthAgo;
    private String mHalfMonthUrl;
    private String mYesterDayAgo;
    private String mYesterDayUrl;
    private PopupWindow mPopWindowToady;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_proxy_commission;
    }

    @Override
    protected void initView() {
        mCanlendarView = View.inflate(this, R.layout.pop_candlar_proxy, null);
        calendarView = (CalendarView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_calendar);
        mCalendarYear = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_year);
        mCalendarMonthDay = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_month_day_week);
        mCalendarCancle = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_cancle);
        mCalendarConfirm = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_confirm);
        mPop = PopUtil.popuMake(mCanlendarView);


        mHeadview = View.inflate(this, R.layout.proxy_commison_head, null);

        mHeadType = (TextView) mHeadview.findViewById(R.id.proxy_comision_head_type);
        mHeadCommNum = (TextView) mHeadview.findViewById(R.id.proxy_comision_head_comm_num);
        mHeadFreeNum = (TextView) mHeadview.findViewById(R.id.proxy_comision_head_fee_num);

        mTodayPop = View.inflate(this, R.layout.proxy_commission_today, null);
        mToday = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_today);
        mYesterDay = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_yesterday);
        mWeek = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_week);
        mHalfMonth = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_month);

        mPopWindowToady = PopUtil.popuMakeWw(mTodayPop);

    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.proxy_commission));
        //先把字符串转成Date类型
        String template = "yyyy-MM-dd";
        mSdf = new SimpleDateFormat(template, Locale.CHINA);

        int oneAgo = -1;
        int sevenAgo = -7;
        int monthAgo = -15;

        mHalfMonthAgo = DateUtils.getCurTimeAddND(monthAgo, template);
        mSevenAgo = DateUtils.getCurTimeAddND(sevenAgo, template);
        mYesterDayAgo = DateUtils.getCurTimeAddND(oneAgo, template);
        mTodayDate = DateUtils.getCurTime(template);

        mStart = mSevenAgo.replaceAll("-", "");
        mEnd = mTodayDate.replaceAll("-", "");

        mHalfMonthUrl = mHalfMonthAgo.replaceAll("-", "");
        mSevenUrl = mStart;
        mYesterDayUrl = mYesterDayAgo.replaceAll("-", "");
        mTodayUrl = mEnd;


        mBettingRecordStartTime.setText(mTodayDate);
        mBettingRecordEndTime.setText(mTodayDate);

        mCalendar = Calendar.getInstance();
        //calendarView.setMinDate(mLongDate);//设置最小时间毫秒
        // 预订结束日期
        mCalendar.set(Calendar.HOUR_OF_DAY, 23);
        mCalendar.set(Calendar.MINUTE, 59);
        mCalendar.set(Calendar.SECOND, 59);
        long lateTime = mCalendar.getTimeInMillis();
        calendarView.setMaxDate(lateTime);//设置最大时间

        calendarView.setWeekSeparatorLineColor(this.getResources().getColor(R.color.colorAccent));
        // mProxyCommissionLv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        /**
         * 36.代理佣金时间查询列表
         a)	请求地址：/v1.0/agent/check/startDate/{startDate}/endDate/{endDate}
         b)	请求方式:get
         c)	请求参数说明
         字段名	类型	长度	是否必填	备注
         auth_token	string		是	token
         startDate	String		是	开始时间 格式为yyyyMMdd
         endDate	string		是	结束时间   格式为yyyyMMdd
         */
        volleyGet(mTodayUrl, mTodayUrl);
    }

    @Override
    protected void initListener() {
        mToday.setOnClickListener(this);
        mYesterDay.setOnClickListener(this);
        mWeek.setOnClickListener(this);
        mHalfMonth.setOnClickListener(this);
        mCalendarCancle.setOnClickListener(this);
        mCalendarConfirm.setOnClickListener(this);
        mPop.setOnDismissListener(this);
        mPopWindowToady.setOnDismissListener(this);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mCalendarDate = year + "-"
                        + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-"
                        + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
            }
        });
    }

    @OnClick({R.id.proxy_comision_time_start_time, R.id.proxy_comision_time_end_time,
            R.id.proxy_comision_time_pop, R.id.top_back})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.proxy_comision_time_start_time:
                mCanlendarTag = 1;
                clickTime(mBettingRecordStartTime);
                break;
            case R.id.proxy_comision_time_end_time:
                mCanlendarTag = 2;
                clickTime(mBettingRecordEndTime);
                break;
            case R.id.proxy_comision_time_pop:
                setClickAbleTime();
                PopUtil.closePop(mPopWindowToady);
                mPopWindowToady.showAsDropDown(mProxyComisionTimePop, 0, 7);
                break;
        }
    }

    private void clickTime(TextView time) {
        PopUtil.closePop(mPop);
        mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
        StringBuilder buffer = getStringBuilder(time);
        setProxyCanlendarTop(buffer.toString());
        setClickAbleTime();
    }

    private void setClickAbleTime() {
        mBettingRecordStartTime.setClickable(false);
        mBettingRecordEndTime.setClickable(false);
    }

    /**
     * 格式化日期1992-09-10
     *
     * @param bettingRecordTime
     * @return
     */
    @NonNull
    private StringBuilder getStringBuilder(TextView bettingRecordTime) {
        String time;
        String[] splitend;
        StringBuilder buffer;
        time = bettingRecordTime.getText().toString();
        splitend = time.split("-");
        buffer = new StringBuilder();
        for (String s : splitend) {
            if (s.length() >= 2) {
                if (s.length() == 4)
                    buffer.append(s);
                else
                    buffer.append("-" + s);
            } else {
                s = "0" + s;
                buffer.append(s);
            }
        }
        mCalendarDate = buffer.toString().replace("-", "");
        return buffer;
    }

    private void setProxyCanlendarTop(String startTime) {
        Logger.INSTANCE.d(TAG, "top时间------------------------------:" + startTime);
        String[] split = startTime.split("-");
        mCalendarYear.setText(split[0]);
        String aNew = DateUtils.getweekdayBystrNew(startTime);
        StringBuilder builder = new StringBuilder();
        builder.append(split[1]).append("月").append(split[2]).append("日").append("  " + aNew);
        mCalendarMonthDay.setText(builder.toString());
    }

    private void volleyGet(String startTime, String endTime) {

        SSQSApplication.apiClient(classGuid).agentCheckList(startTime, endTime, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mProxyComisionEmpty.setVisibility(View.GONE);

                    ProxyCmmsionsBean bean = (ProxyCmmsionsBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }

                } else {
                    mProxyComisionEmpty.setVisibility(View.VISIBLE);
                    ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                }
            }
        });
    }

    private void processData(ProxyCmmsionsBean data) {
        mHeadCommNum.setText(data.getFee());
        mHeadFreeNum.setText(data.getAmount());
        mHeadType.setText(mStart);

        List<ProxyCmmsionsBean.UsersBean> users = data.getUsers();
        if (users == null || users.size() == 0) {
            mProxyComisionEmpty.setVisibility(View.VISIBLE);
            return;
        }
        // mProxyCommissionLv.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL, 1, R.color.gray_e));
        //ProxyCommissionAdapter adapter = new ProxyCommissionAdapter(R.layout.item_proxy_commission, users);
        ProxyCommissionLvAdapter adapter = new ProxyCommissionLvAdapter(this, users);
        if (mProxyCommissionLv.getHeaderViewsCount() > 0)
            mProxyCommissionLv.removeHeaderView(mHeadview);
        mProxyCommissionLv.addHeaderView(mHeadview);
        mProxyCommissionLv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        mBettingRecordEndTime.setText(mTodayDate);
        switch (v.getId()) {
            case R.id.proxy_commission_pop_month:
                volleyGet(mHalfMonthUrl, mTodayUrl);
                mProxyComisionTimePop.setText(getString(R.string.half_month));
                mPopWindowToady.dismiss();
                mBettingRecordStartTime.setText(mHalfMonthAgo);
                break;
            case R.id.proxy_commission_pop_week:
                mProxyComisionTimePop.setText(getString(R.string.one_week));
                volleyGet(mSevenUrl, mTodayUrl);
                mPopWindowToady.dismiss();
                mBettingRecordStartTime.setText(mSevenAgo);
                break;
            case R.id.proxy_commission_pop_yesterday:
                mProxyComisionTimePop.setText(getString(R.string.yesterday));
                volleyGet(mYesterDayUrl, mTodayUrl);
                mPopWindowToady.dismiss();
                mBettingRecordStartTime.setText(mYesterDayAgo);
                break;
            case R.id.proxy_commission_pop_today:
                mProxyComisionTimePop.setText(getString(R.string.today));
                volleyGet(mTodayUrl, mTodayUrl);
                mBettingRecordStartTime.setText(mTodayDate);
                mPopWindowToady.dismiss();
                break;
            case R.id.pop_calendar_proxy_cancle:
                mPopWindowToady.dismiss();
                break;
            case R.id.pop_calendar_proxy_confirm:
                if (mCanlendarTag == 1) {
                    mBettingRecordStartTime.setText(mCalendarDate);
                    mStart = mCalendarDate.replaceAll("-", "");
                } else if (mCanlendarTag == 2) {
                    mBettingRecordEndTime.setText(mCalendarDate);
                    mEnd = mCalendarDate.replaceAll("-", "");
                }
                volleyGet(mStart, mEnd);
                mPop.dismiss();
                break;
        }
    }

    @Override
    public void onDismiss() {
        mBettingRecordStartTime.setClickable(true);
        mBettingRecordEndTime.setClickable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
