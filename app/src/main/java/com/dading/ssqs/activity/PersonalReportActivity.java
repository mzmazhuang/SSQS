package com.dading.ssqs.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.PersonalReportAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.PersonalReportBean;
import com.dading.ssqs.bean.ReportInfoBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/9/11.
 */
public class PersonalReportActivity extends BaseActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
    @Bind(R.id.proxy_comision_time_start_time)
    TextView mBettingRecordStartTime;
    @Bind(R.id.proxy_comision_time_end_time)
    TextView mBettingRecordEndTime;
    @Bind(R.id.proxy_comision_time_pop)
    TextView mProxyComisionTimePop;
    @Bind(R.id.proxy_comision_time_ly)
    LinearLayout mProxyComisionTimeLy;

    @Bind(R.id.data_empty)
    RelativeLayout mProxyComisionEmpty;

    @Bind(R.id.personal_report_recycle)
    RecyclerView mRecycleview;
    @Bind(R.id.personal_report)
    RadioButton mPersonalReport;
    @Bind(R.id.team_report)
    RadioButton mTeamReport;
    @Bind(R.id.personal_report_radio_group)
    RadioGroup mPersonalReportRadioGroup;
    @Bind(R.id.loading_animal)
    LinearLayout mLoadingAnimal;

    private View mTodayPop;
    private TextView mToday;
    private TextView mYesterDay;
    private TextView mWeek;
    private TextView mHalfMonth;
    private int mCanlendarTag;
    private PopupWindow mPopWindowToady;
    private View mCanlendarView;
    private CalendarView calendarView;
    private TextView mCalendarYear;
    private TextView mCalendarMonthDay;
    private TextView mCalendarCancle;
    private TextView mCalendarConfirm;
    private PopupWindow mPop;
    private View mView;
    private String mCalendarDate;
    private String TAG = "PersonalReportActivity";
    private String mHalfMonthAgo;
    private SimpleDateFormat mSdf;
    private String mSevenAgo;
    private String mYesterDayAgo;
    private String mTodayDate;
    private String mStart;
    private String mEnd;
    private String mHalfMonthUrl;
    private String mSevenUrl;
    private String mYesterDayUrl;
    private String mTodayUrl;
    private Calendar mCalendar;

    @Override
    protected int setLayoutId() {
        mView = View.inflate(this, R.layout.activity_personal_report, null);

        mCanlendarView = View.inflate(this, R.layout.pop_candlar_proxy, null);
        calendarView = (CalendarView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_calendar);
        mCalendarYear = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_year);
        mCalendarMonthDay = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_month_day_week);
        mCalendarCancle = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_cancle);
        mCalendarConfirm = (TextView) mCanlendarView.findViewById(R.id.pop_calendar_proxy_confirm);
        mPop = PopUtil.popuMake(mCanlendarView);


        mTodayPop = View.inflate(this, R.layout.proxy_commission_today, null);
        mToday = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_today);
        mYesterDay = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_yesterday);
        mWeek = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_week);
        mHalfMonth = (TextView) mTodayPop.findViewById(R.id.proxy_commission_pop_month);
        mPopWindowToady = PopUtil.popuMakeWw(mTodayPop);

        return R.layout.activity_personal_report;
    }

    @Override
    protected void initData() {
        super.initData();

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

        mCalendar = Calendar.getInstance();
        //calendarView.setMinDate(mLongDate);//设置最小时间毫秒
        // 预订结束日期
        mCalendar.set(Calendar.HOUR_OF_DAY, 23);
        mCalendar.set(Calendar.MINUTE, 59);
        mCalendar.set(Calendar.SECOND, 59);

        long lateTime = mCalendar.getTimeInMillis();
        calendarView.setMaxDate(lateTime);//设置最大时间

        calendarView.setWeekSeparatorLineColor(this.getResources().getColor(R.color.colorAccent));

        getData();
    }

    private void getData() {
        mProxyComisionTimePop.setText(getString(R.string.today));
        mBettingRecordStartTime.setText(mTodayDate);
        mBettingRecordEndTime.setText(mTodayDate);

        /**
         * 36.代理佣金时间查询列表
         a)	请求地址：/v1.0/report/startDate/{startDate}/endDate/{endDate}
         b)	请求方式:get
         c)	请求参数说明
         字段名	类型	长度	是否必填	备注
         auth_token	string		是	token
         startDate	String		是	开始时间 格式为yyyyMMdd
         endDate	string		是	结束时间   格式为yyyyMMdd
         */
        volleyGet(mPersonalReport.isChecked(), mTodayUrl, mTodayUrl);
    }

    @Override
    protected void initListener() {
        super.initListener();
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
        mPersonalReportRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getData();
            }
        });
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

    private void setProxyCanlendarTop(String startTime) {
        Logger.d(TAG, "top时间------------------------------:" + startTime);
        String[] split = startTime.split("-");
        mCalendarYear.setText(split[0]);
        String aNew = DateUtils.getweekdayBystrNew(startTime);
        StringBuilder builder = new StringBuilder();
        builder.append(split[1]).append("月").append(split[2]).append("日").append("  " + aNew);
        mCalendarMonthDay.setText(builder.toString());
    }

    private void volleyGet(boolean isTeam, String startTime, String endTime) {
        mLoadingAnimal.setVisibility(View.VISIBLE);

        SSQSApplication.apiClient(classGuid).getReportList(isTeam, startTime, endTime, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mLoadingAnimal.setVisibility(View.GONE);
                mProxyComisionEmpty.setVisibility(View.GONE);

                if (result.isOk()) {
                    PersonalReportBean bean = (PersonalReportBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    mLoadingAnimal.setVisibility(View.GONE);
                    mProxyComisionEmpty.setVisibility(View.VISIBLE);
                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                }
            }
        });
    }

    private void processData(PersonalReportBean data) {
        if (data != null) {
            /**
             * wins	string	盈亏总额
             amount	string	投注总额
             rewards	string	派彩总额
             fees	string	佣金总额
             recharges	string	充值总额
             extracts	string	提现总额

             注：充值金额，提现金额为元单位，其他为金币
             */
            mProxyComisionEmpty.setVisibility(View.GONE);
            ArrayList<ReportInfoBean> list = new ArrayList<>();
            list.add(new ReportInfoBean(Constent.WINS, data.getWins()));
            list.add(new ReportInfoBean(Constent.AMOUNT, data.getAmount()));
            list.add(new ReportInfoBean(Constent.REWARDS, data.getRewards()));
            list.add(new ReportInfoBean(Constent.FEES, data.getFees()));
            list.add(new ReportInfoBean(Constent.RECHARGES, data.getRecharges()));
            list.add(new ReportInfoBean(Constent.EXTRACTS, data.getExtracts()));

            mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecycleview.setAdapter(new PersonalReportAdapter(this, list));
        } else {
            mProxyComisionEmpty.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.proxy_comision_time_start_time, R.id.proxy_comision_time_end_time,
            R.id.proxy_comision_time_pop, R.id.personal_top_back})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.personal_top_back:
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
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        mBettingRecordEndTime.setText(mTodayDate);
        switch (v.getId()) {
            case R.id.proxy_commission_pop_month:
                volleyGet(mPersonalReport.isChecked(), mHalfMonthUrl, mTodayUrl);
                mProxyComisionTimePop.setText(getString(R.string.half_month));
                mPopWindowToady.dismiss();
                mBettingRecordStartTime.setText(mHalfMonthAgo);
                break;
            case R.id.proxy_commission_pop_week:
                mProxyComisionTimePop.setText(getString(R.string.one_week));
                volleyGet(mPersonalReport.isChecked(), mSevenUrl, mTodayUrl);
                mPopWindowToady.dismiss();
                mBettingRecordStartTime.setText(mSevenAgo);
                break;
            case R.id.proxy_commission_pop_yesterday:
                mProxyComisionTimePop.setText(getString(R.string.yesterday));
                volleyGet(mPersonalReport.isChecked(), mYesterDayUrl, mTodayUrl);
                mPopWindowToady.dismiss();
                mBettingRecordStartTime.setText(mYesterDayAgo);
                break;
            case R.id.proxy_commission_pop_today:
                mProxyComisionTimePop.setText(getString(R.string.today));
                volleyGet(mPersonalReport.isChecked(), mTodayUrl, mTodayUrl);
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
                volleyGet(mPersonalReport.isChecked(), mStart, mEnd);
                mPop.dismiss();
                break;
        }
    }

    @Override
    public void onDismiss() {
        mBettingRecordStartTime.setClickable(true);
        mBettingRecordEndTime.setClickable(true);
    }
}
