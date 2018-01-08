package com.dading.ssqs.controllar.scores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.ScoreMatchAdapterSG;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseScoreControllar;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/29 9:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BaseSG extends BaseScoreControllar implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "BaseSG";

    public PullToRefreshListView mSgList;
    private ScoreMatchAdapterSG mAdapter;
    private int mPage = 1;
    private String mFormatData;
    private String mFormatData2;
    private String mWeek;
    private Calendar mCalendar;
    private SimpleDateFormat mSdf1;
    private SimpleDateFormat mSdf2;
    private ArrayList<String> mListData1;
    private ArrayList<String> mListData2;
    private View mView;
    private PopupWindow mPopupWindow;
    public String mDate;
    public SGRecevice mRecevice;
    private int mTotalCount;
    private LinearLayout mLoadAnimal;
    private Runnable mTask;
    private Runnable mTaskMore;
    public RelativeLayout mEmpty;
    public LinearLayout mEmptyGB;
    private ImageView mLoadAnimalIv;
    private AnimationDrawable mDrawable;

    private boolean isGetData = false;


    @Override
    // 预先显示本地数据
    public View initContentView(Context context) {
        mView = View.inflate(mContent, R.layout.scorepager_sg, null);
        mSgList = (PullToRefreshListView) mView.findViewById(R.id.score_vp_sg);
        mEmpty = (RelativeLayout) mView.findViewById(R.id.data_empty);
        mEmptyGB = (LinearLayout) mView.findViewById(R.id.guess_ball_no_data);
        mLoadAnimal = (LinearLayout) mView.findViewById(R.id.loading_anim);
        mLoadAnimalIv = (ImageView) mView.findViewById(R.id.loading_anim_iv);

        setEmptyView();

        mAdapter = new ScoreMatchAdapterSG(mContent, 1);
        mAdapter.setShowIcon(false);
        mSgList.setAdapter(mAdapter);

        return mView;
    }

    public void setEmptyView() {

    }

    private boolean hasInit = false;

    public void init() {
        if (!hasInit) {
            hasInit = true;

            if (!isGetData) {
                isGetData = true;

                mLoadAnimal.setVisibility(View.VISIBLE);
                mDrawable.start();

                mPage = 1;

                getNetDataWork(mFormatData, "0", "0", mPage, 10, true);
            }
        }
    }

    @Override
    public void initData() {
        mLoadAnimalIv.setImageResource(R.drawable.loading_anim);
        mDrawable = (AnimationDrawable) mLoadAnimalIv.getDrawable();

        mRecevice = new SGRecevice();
        setSend();

        mDate = mFormatData2;
        mCalendar = Calendar.getInstance();
        mSdf1 = new SimpleDateFormat("yyyyMMddHH:mm:ss", Locale.CHINA);
        mSdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        mListData1 = new ArrayList<>();
        mListData2 = new ArrayList<>();

        mCalendar.add(Calendar.DAY_OF_YEAR, -1);
        Date date = mCalendar.getTime();
        mFormatData = mSdf1.format(date);
        mFormatData2 = mSdf2.format(date);

        String data = AppendData(mFormatData2);
        mScoreWeekData.setText(data);

        mDate = mFormatData2.replaceAll("-", "");
        UIUtils.getSputils().putString(Constent.SG_TIME, mFormatData);

        mWeek = AppendData(mFormatData2);

        for (int i = 1; i < 8; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, -i);
            Date dates = calendar.getTime();
            String s1 = mSdf1.format(dates);
            String s2 = mSdf2.format(dates);
            String sData = AppendData(s2);
            mListData1.add(s1);
            mListData2.add(sData);
        }

        for (int i = 0; i < mListRB.size(); i++) {
            mListRB.get(i).setText(mListData2.get(i));
        }
    }

    private void getNetDataWork(String date, String subType, String leagusIds, int page, int limit, final boolean isRefresh) {
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        SSQSApplication.apiClient(0).getMatchBallOrTypeList(b, 3, date, subType, 0, leagusIds, page, limit, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mSgList.onRefreshComplete();
                mLoadAnimal.setVisibility(View.GONE);
                mDrawable.stop();

                if (result.isOk()) {
                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                    if (page != null) {
                        mTotalCount = page.getTotalCount();
                        if (page.getItems() != null) {
                            if (isRefresh) {
                                mAdapter.setData(page.getItems());
                            } else {
                                mAdapter.addData(page.getItems());
                            }
                        }
                    }
                    isGetData = false;
                } else {
                    isGetData = false;
                    Logger.INSTANCE.d(TAG, result.getMessage() + "失败信息");
                }
            }
        });
    }

    public void setSend() {
        UIUtils.ReRecevice(mRecevice, Constent.SG_RECEVICE);
        UIUtils.ReRecevice(mRecevice, Constent.SG_RECEVICE_CB);
    }

    @Override
    public void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
        UIUtils.removeTask(mTask);
        UIUtils.removeTask(mTaskMore);
    }

    @Override
    public void initScorecalendar() {
        mScoreWeekLayout.setVisibility(View.VISIBLE);
        String text = mScoreWeekData.getText().toString();
        Logger.INSTANCE.d(TAG, "日期是:" + text);
        mScoreWeekData.setText(mWeek);
        mScoreWeekLeft.setImageResource(R.mipmap.arrows_checked_2);
        mScoreWeekLeft.setClickable(true);
        mScoreWeekRight.setImageResource(R.mipmap.arrows_2);
        mScoreWeekRight.setClickable(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mScoreWeekLeft.setOnClickListener(this);
        mScoreWeekRight.setOnClickListener(this);
        mScoreWeekCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow = PopUtil.popuMake(mPopView);
                mPopupWindow.showAtLocation(mView, Gravity.CENTER, 0, 0);
            }
        });
        mPopLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mPopClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        mSgList.setMode(PullToRefreshBase.Mode.BOTH);
        mSgList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!isGetData) {
                    isGetData = true;

                    mTask = new Runnable() {

                        @Override
                        public void run() {
                            UIUtils.getSputils().putString(Constent.SUBTYPE, "0");
                            UIUtils.getSputils().putString(Constent.LEAGUEIDS, "0");

                            mPage = 1;

                            getNetDataWork(UIUtils.getSputils().getString(Constent.SG_TIME, "20000101"), "0", "0", 1, 10, true);
                        }
                    };
                    UIUtils.postTaskDelay(mTask, 500);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //关闭上啦加载的效果
                mTaskMore = new Runnable() {
                    @Override
                    public void run() {
                        ++mPage;
                        if (mPage > mTotalCount) {
                            mPage--;
                            ToastUtils.midToast(mContent, "已全部加载,无新数据!", 0);
                            mSgList.onRefreshComplete();
                        } else {
                            if (!isGetData) {
                                isGetData = true;
                                getNetDataWork(UIUtils.getSputils().getString(Constent.SG_TIME, "20000101"), UIUtils.getSputils().getString(Constent.SUBTYPE, "0"), UIUtils.getSputils().getString(Constent.LEAGUEIDS, "0"), mPage, 10, false);
                            }
                        }
                    }
                };
                UIUtils.postTaskDelay(mTaskMore, 500);
            }
        });
    }

    private String AppendData(String formatData2) {
        String s = DateUtils.getweekdayBystr(formatData2);
        String s1 = "(" + s + ")" + formatData2;
        return s1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.score_week_left:
                timeOperation(true);
                break;
            case R.id.score_week_right:
                timeOperation(false);
                break;
            default:
                break;
        }
    }

    private int day = 6;

    private void timeOperation(boolean isLeft) {
        if (day == 0 && isLeft || day >= 6 && !isLeft) {
            return;
        }
        mScoreWeekLeft.setImageResource(R.mipmap.arrows_checked_2);
        mScoreWeekLeft.setClickable(true);
        mScoreWeekRight.setImageResource(R.mipmap.arrows_checked);
        mScoreWeekRight.setClickable(true);

        if (isLeft) {
            day--;

            mCalendar.add(Calendar.DAY_OF_YEAR, -1);

            if (day == 0) {
                mScoreWeekLeft.setImageResource(R.mipmap.arrows);
                mScoreWeekLeft.setClickable(false);
                mScoreWeekRight.setImageResource(R.mipmap.arrows_checked);
                mScoreWeekRight.setClickable(true);
            }
        } else {
            day++;

            mCalendar.add(Calendar.DAY_OF_YEAR, 1);

            if (day == 6) {
                mScoreWeekLeft.setImageResource(R.mipmap.arrows_checked_2);
                mScoreWeekLeft.setClickable(true);
                mScoreWeekRight.setImageResource(R.mipmap.arrows_2);
                mScoreWeekRight.setClickable(false);
            }
        }

        Date date = mCalendar.getTime();
        String s = mSdf2.format(date);
        String data = AppendData(s);
        mScoreWeekData.setText(data);

        mDate = s.replaceAll("-", "");

        String netWorkDate = mSdf1.format(date);
        UIUtils.getSputils().putString(Constent.SC_TIME, netWorkDate);

        calendarVolley(netWorkDate);

        calendarData(day);
    }

    private void calendarData(int num) {
        mPage = 1;
        switch (num) {
            case 6:
                mRg.check(R.id.pop_calendar_rb1);
                break;
            case 5:
                mRg.check(R.id.pop_calendar_rb2);
                break;
            case 4:
                mRg.check(R.id.pop_calendar_rb3);
                break;
            case 3:
                mRg.check(R.id.pop_calendar_rb4);
                break;
            case 2:
                mRg.check(R.id.pop_calendar_rb5);
                break;
            case 1:
                mRg.check(R.id.pop_calendar_rb6);
                break;
            case 0:
                mRg.check(R.id.pop_calendar_rb7);
                break;
            default:
                break;
        }
    }

    private void calendarVolley(String rightLeft) {
        if (!isGetData) {
            isGetData = true;

            mPage = 1;

            getNetDataWork(rightLeft, "0", "0", mPage, 10, true);
        }
    }

    @Override
    public void checkInitListner() {
        mPage = 1;
        switch (mCalendarPostion) {
            case 0:
                day = 6;
                break;
            case 1:
                day = 5;
                break;
            case 2:
                day = 4;
                break;
            case 3:
                day = 3;
                break;
            case 4:
                day = 2;
                break;
            case 5:
                day = 1;
                break;
            case 6:
                day = 0;
                break;
            default:
                break;
        }
        CalendarClick((mCalendarPostion + 1));
        String s1 = mListData1.get(mCalendarPostion);
        mFormatData = s1;
        calendarVolley(s1);
        if (mPopupWindow == null) {
            return;
        } else {
            mPopupWindow.dismiss();
        }
    }

    private void CalendarClick(int num) {
        mCalendar.setTime(new Date());
        mCalendar.add(Calendar.DAY_OF_YEAR, -num);
        Date date = mCalendar.getTime();
        String s = mSdf2.format(date);
        String data = AppendData(s);
        mDate = s.replaceAll("-", "");
        UIUtils.getSputils().putString(Constent.SG_TIME, mSdf1.format(date));
        mScoreWeekData.setText(data);
        switch (num) {
            case 1:
                mScoreWeekLeft.setImageResource(R.mipmap.arrows_checked_2);
                mScoreWeekLeft.setClickable(true);
                mScoreWeekRight.setImageResource(R.mipmap.arrows_2);
                mScoreWeekRight.setClickable(false);
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                mScoreWeekLeft.setImageResource(R.mipmap.arrows_checked_2);
                mScoreWeekLeft.setClickable(true);
                mScoreWeekRight.setImageResource(R.mipmap.arrows_checked);
                mScoreWeekRight.setClickable(true);
                break;
            case 7:
                mScoreWeekLeft.setImageResource(R.mipmap.arrows);
                mScoreWeekLeft.setClickable(false);
                mScoreWeekRight.setImageResource(R.mipmap.arrows_checked);
                mScoreWeekRight.setClickable(true);
                break;
        }
    }

    public String getTime() {
        return mDate;
    }

    private class SGRecevice extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.INSTANCE.d("GBSS", "接到广播赛果------------------------------:");

            String s = UIUtils.getSputils().getString(Constent.LEAGUEIDS, "0");
            String action = intent.getAction();//用于筛选

            if (!isGetData) {
                isGetData = true;

                mPage = 1;

                getNetDataWork(UIUtils.getSputils().getString(Constent.SG_TIME, "20000101"), "0", (action.equals(Constent.JS_SG_SC_FITTER) ? s : "0"), mPage, 10, true);
            }
        }
    }
}

