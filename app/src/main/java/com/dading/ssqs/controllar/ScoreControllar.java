package com.dading.ssqs.controllar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.MoreSettingActivity;
import com.dading.ssqs.adapter.GuessBallChoiceGvAdapter;
import com.dading.ssqs.adapter.ScrollAdapterInner;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.controllar.scores.GZScoreControllar;
import com.dading.ssqs.controllar.scores.JSScoreControllar;
import com.dading.ssqs.controllar.scores.SCScoreControllar;
import com.dading.ssqs.controllar.scores.SGScoreControllar;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.dading.ssqs.components.tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 16:51
 * 描述	      比分
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScoreControllar extends BaseTabsContainer {

    private static final String TAG = "ScoreControllar";
    private ViewPager mScrollViewpager;
    private TabIndicator mScrollIndicator;
    private ArrayList<Fragment> mDataView;
    private ArrayList<String> mDataIndicator;
    private View mChioceView;
    private GridView mChioceSetting;
    private PopupWindow mPopChioce;
    private LinearLayout mChioceLy;
    private RadioGroup mChioceGp;
    private RadioButton mChioceSettingRb1;
    private TextView mChioceConfirm;
    private RadioButton mChioceSettingRb2;
    private ArrayList<GusessChoiceBean.FilterEntity> mList;
    private GuessBallChoiceGvAdapter mAdapterSx;
    private int mType;
    private int mSubType;
    private RadioButton mChioceSettingTopRb1;
    private RadioGroup mChioceSettingTopRg;
    private String mleagueIDs;
    private String mDate;
    public JSScoreControllar mJsScoreControllar;
    public SGScoreControllar mSgScoreControllar;
    public SCScoreControllar mScScoreControllar;
    public GZScoreControllar mGzScoreControllar;
    public ScoreRecevice mRecevice;
    private int mPager;

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(context, R.layout.scorepager, null);
        mScrollViewpager = (ViewPager) view.findViewById(R.id.scroll_viewpager);
        mScrollIndicator = (TabIndicator) view.findViewById(R.id.scroll_indicator);

        mChioceView = View.inflate(mContent, R.layout.pop_chioce_view_score, null);
        mChioceSetting = (GridView) mChioceView.findViewById(R.id.score_chioce_setting);
        mChioceGp = (RadioGroup) mChioceView.findViewById(R.id.score_chioce_gp);
        mChioceSettingRb1 = (RadioButton) mChioceView.findViewById(R.id.score_chioce_rb1);
        mChioceSettingRb2 = (RadioButton) mChioceView.findViewById(R.id.score_chioce_rb2);
        mChioceSettingTopRg = (RadioGroup) mChioceView.findViewById(R.id.score_chioce_top_rg);
        mChioceSettingTopRb1 = (RadioButton) mChioceView.findViewById(R.id.score_chioce_rb1_top);
        mChioceLy = (LinearLayout) mChioceView.findViewById(R.id.score_chioce_ly);
        mChioceConfirm = (TextView) mChioceView.findViewById(R.id.score_chioce_confirm);

        mPopChioce = PopUtil.popuMake(mChioceView);

        mAdapterSx = new GuessBallChoiceGvAdapter(mContent);
        mChioceSetting.setAdapter(mAdapterSx);

        return view;
    }

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        setVisbilityViews(mListTitle, mScoreLy);
    }

    @Override
    public void initData() {
        mRecevice = new ScoreRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_ACTION);
        UIUtils.ReRecevice(mRecevice, Constent.HOME_SCORE);
        UIUtils.ReRecevice(mRecevice, Constent.GO_TO_BEFORE);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
        Calendar calendar = new GregorianCalendar();
        Date now = calendar.getTime();
        String jt = format.format(now);

        mDate = jt;

        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String mt = format.format(date);

        calendar.add(Calendar.DATE, -2);
        Date date1 = calendar.getTime();
        String zt = format.format(date1);

        Logger.d(TAG, "三個時間返回数据是------" + zt + "----" + jt + "----" + mt);

        UIUtils.getSputils().putString(Constent.JS_TIME, jt);
        UIUtils.getSputils().putString(Constent.SG_TIME, zt);
        UIUtils.getSputils().putString(Constent.SC_TIME, mt);

        mPager = 0;
        mType = 2;
        mSubType = 0;
        StringBuilder sb1 = new StringBuilder();
        mleagueIDs = sb1.toString();
        mDataIndicator = new ArrayList<>();
        mDataIndicator.add("即时");
        mDataIndicator.add("赛果");
        mDataIndicator.add("赛程");
        mDataIndicator.add("关注");

        mDataView = new ArrayList<>();

        mJsScoreControllar = new JSScoreControllar();
        mSgScoreControllar = new SGScoreControllar();
        mScScoreControllar = new SCScoreControllar();
        mGzScoreControllar = new GZScoreControllar();

        mDataView.add(mJsScoreControllar);
        mDataView.add(mSgScoreControllar);
        mDataView.add(mScScoreControllar);
        mDataView.add(mGzScoreControllar);

        mScrollViewpager.setAdapter(new ScrollAdapterInner(this.getChildFragmentManager(), mDataView, mDataIndicator));
        mScrollViewpager.setOffscreenPageLimit(4);
        mScrollIndicator.setViewPager(mScrollViewpager);

        mChioceSettingTopRb1.setChecked(true);
        mChioceSettingRb2.setChecked(true);
        mList = new ArrayList<>();
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
    }

    private interface OnDataDoneListener {
        void onDone();
    }

    private void getNetDataWork(int type, int subType, String date, final OnDataDoneListener listener) {
        /**
         * 获取比分筛选列表
         * /v1.0/match/filter/type/{type}/subType/{subType}/date/{date}
         *
         */

        SSQSApplication.apiClient(0).getMatchFilterList(type, subType, date, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<GusessChoiceBean> items = (List<GusessChoiceBean>) result.getData();

                    if (items != null) {
                        mList.clear();

                        for (int i = 0; i < items.size(); i++) {
                            GusessChoiceBean entity = items.get(i);
                            for (int j = 0; j < entity.filter.size(); j++) {
                                mList.add(entity.filter.get(j));
                            }
                        }

                        mAdapterSx.setList(mList);

                        if (listener != null) {
                            listener.onDone();
                        }
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "竞猜筛选失败信息");
                }
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();

        mChioceSettingTopRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.score_chioce_rb1_top:
                        mSubType = 0;
                        break;
                    case R.id.score_chioce_rb2_top:
                        mSubType = 1;
                        break;
                    default:
                        break;
                }
                UIUtils.getSputils().putString(Constent.SUBTYPE, mSubType + "");
                getNetDataWork(mType, mSubType, mDate, null);
            }
        });

        mScrollViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mPager = 0;
                        mType = 2;
                        mScoreChoice.setVisibility(View.VISIBLE);
                        mDate = UIUtils.getSputils().getString(Constent.JS_TIME, "2000-01-01");
                        Logger.d(TAG, "即時得到的時間返回数据是------------" + mDate);
                        break;
                    case 1:
                        mPager = 1;
                        mScoreChoice.setVisibility(View.VISIBLE);
                        mType = 3;
                        mDate = UIUtils.getSputils().getString(Constent.SG_TIME, "2000-01-01");
                        Logger.d(TAG, "賽果得到的時間返回数据是-------------" + mDate);
                        break;
                    case 2:
                        mPager = 2;
                        mScoreChoice.setVisibility(View.VISIBLE);
                        mDate = UIUtils.getSputils().getString(Constent.SC_TIME, "2000-01-01");
                        Logger.d(TAG, "賽程得到的時間返回数据是-------------" + mDate);
                        mType = 4;
                        break;
                    case 3:
                        mPager = 3;
                        mType = 1;
                        mScoreChoice.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                UIUtils.getSputils().putString(Constent.SCORE_TYPE, mType + "");
                UIUtils.SendReRecevice(Constent.JS_RECEVICE);
                UIUtils.SendReRecevice(Constent.SG_RECEVICE);
                UIUtils.SendReRecevice(Constent.SC_RECEVICE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mChioceSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChioceSettingRb1.setChecked(false);
                mChioceSettingRb2.setChecked(false);
                boolean checked = mList.get(position).checked;
                if (checked) {
                    mList.get(position).checked = false;
                } else {
                    mList.get(position).checked = true;
                }
                mAdapterSx.setList(mList);
            }
        });
        mChioceGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.score_chioce_rb1:
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).checked = true;
                        }
                        mAdapterSx.setList(mList);
                        break;
                    case R.id.score_chioce_rb2:
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).checked = false;
                        }
                        mAdapterSx.setList(mList);
                        break;
                    default:
                        break;
                }
            }
        });
        mChioceConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "pop消失前------------------------------:");
                mPopChioce.dismiss();
                Logger.d(TAG, "pop消失后------------------------------:");
                ArrayList<GusessChoiceBean.FilterEntity> listAdd = new ArrayList<>();
                List<GusessChoiceBean.FilterEntity> list = mAdapterSx.getList();
                for (int i = 0; i < list.size(); i++) {
                    GusessChoiceBean.FilterEntity entity = list.get(i);
                    if (entity.checked) {
                        listAdd.add(entity);
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < listAdd.size(); i++) {
                    GusessChoiceBean.FilterEntity ab = listAdd.get(i);
                    if (i == listAdd.size() - 1) {
                        sb.append(ab.id);
                    } else {
                        sb.append(ab.id).append(",");
                    }
                }
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).checked = false;
                }
                mleagueIDs = sb.toString();
                Logger.d(TAG, "篩選條件id是------------------------------:" + mleagueIDs);

                UIUtils.getSputils().putString(Constent.LEAGUEIDS, mleagueIDs);
                UIUtils.getSputils().putBoolean(Constent.CHIOCE, true);
                UIUtils.SendReRecevice(Constent.JS_SG_SC_FITTER);
               /* switch (mType) {
                    case 2:
                        break;
                    case 3:
                        UIUtils.SendReRecevice(Constent.SG_RECEVICE);
                        break;
                    case 4:
                        UIUtils.SendReRecevice(Constent.SC_RECEVICE);
                        break;
                    default:
                        break;
                }*/
            }
        });
        mChioceLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopChioce.dismiss();
            }
        });
        mScoreChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "比分筛选的時間是------------------------------:" + mDate);

                switch (UIUtils.getSputils().getString(Constent.SCORE_TYPE, "0")) {
                    case "2":
                        mDate = UIUtils.getSputils().getString(Constent.JS_TIME, "2000-01-01");
                        break;
                    case "3":
                        mDate = UIUtils.getSputils().getString(Constent.SG_TIME, "2000-01-01");
                        break;
                    case "4":
                        mDate = UIUtils.getSputils().getString(Constent.SC_TIME, "2000-01-01");
                        break;
                    case "1":
                        mScoreChoice.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
                /**
                 * *篮球赛前筛选列表
                 *请求地址：/v1.0/match/ball/filter
                 */

                if (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true)) {
                    getNetDataWork(mType, mSubType, mDate, new OnDataDoneListener() {
                        @Override
                        public void onDone() {
                            if (!mPopChioce.isShowing()) {
                                mPopChioce.showAsDropDown(mScoreLy, 0, 0);
                            }
                        }
                    });
                } else {
                    SSQSApplication.apiClient(0).getMatchBallFilterList(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                List<GusessChoiceBean> items = (List<GusessChoiceBean>) result.getData();

                                if (items != null) {
                                    mList.clear();

                                    for (int i = 0; i < items.size(); i++) {
                                        GusessChoiceBean entity = items.get(i);
                                        for (int j = 0; j < entity.filter.size(); j++) {
                                            mList.add(entity.filter.get(j));
                                        }
                                    }

                                    mAdapterSx.setList(mList);

                                    if (!mPopChioce.isShowing()) {
                                        mPopChioce.showAsDropDown(mScoreLy, 0, 0);
                                    }
                                }
                            } else {
                                ToastUtils.midToast(mContent, result.getMessage(), 0);
                            }
                        }
                    });
                }
            }
        });
        mScoreSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, MoreSettingActivity.class);
                mContent.startActivity(intent);
            }
        });
    }

    private class ScoreRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constent.GO_TO_BEFORE))
                mPager = 0;
            mScrollViewpager.setCurrentItem(mPager);
            boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);
            Logger.d("GBSS", "收到比分广播------------------------------:" + b);
            mScoreTitleRg.check(b ? R.id.content_title_score_title_f : R.id.content_title_score_title_b);
        }
    }
}
