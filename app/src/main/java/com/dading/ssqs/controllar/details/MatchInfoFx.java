package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.FXCupAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.FXBean;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.view.SoftSpaceStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/14 17:49
 * 描述	      分析
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoFx {
    private static final String TAG = "MatchInfoFx";
    private final Context context;
    public final View mRootView;
    private final int matchId;
    private final String away;
    private final String home;
    private final String AImageUrl;
    private final String HImageUrl;
    private TextView mSynthesizeMain;
    private TextView mSynthesizeMiddle;
    private TextView mSynthesizeSecond;
    private SoftSpaceStyle mSynthesizeProgress;
    private TextView mPowerMainZj;
    private TextView mPowerMiddle;
    private SoftSpaceStyle mPowerProgress;
    private TextView mMainSecondMiddle;
    private SoftSpaceStyle mMainSecondProgress;
    private TextView mattackMain;
    private TextView mattackMiddle;
    private TextView mattackSecond;
    private SoftSpaceStyle mattackProgress;
    private TextView mdefendMain;
    private TextView mdefendMiddle;
    private TextView mdefendSecond;
    private SoftSpaceStyle mdefendProgress;
    private LinearLayout mIntegralMainFrameLayout;
    private TextView mIntegralSecondTeam;
    private LinearLayout mIntegralSecondFrameLayout;
    private TextView mhistoryMain;
    private TextView mhistoryMiddle;
    private TextView mhistorySecond;
    private SoftSpaceStyle mhistoryProgress;
    private TextView mIntegralMainTeam;
    private TextView mPowerMainJf;
    private TextView mPowerSecondJf;
    private TextView mPowerSecondZj;
    private TextView mMainSecondMainJf;
    private TextView mMainSecondMainZj;
    private TextView mMainSecondSecondJf;
    private TextView mMainSecondSecondZj;
    private ImageView mIntegralSecondTeamIcon;
    private ImageView mIntegralMainTeamIcon;
    private String[] mTopS;
    private LinearLayout.LayoutParams mParams;
    private ListView mFXCupLV;
    private String[] mTopSCup;
    private RelativeLayout mNodata;
    private ScrollView mScoredata;
    private TextView mRecomMain;
    private TextView mRecomSecond;
    private TextView mRecomMain2;
    private TextView mRecomSecond2;
    private SoftSpaceStyle mRecomProgress;

    public MatchInfoFx(Context context, int matchId, String home, String away, String AImageUrl, String HImageUrl) {
        this.home = home;
        this.away = away;
        this.context = context;
        this.matchId = matchId;
        this.AImageUrl = AImageUrl;
        this.HImageUrl = HImageUrl;
        mRootView = initView(context);
        initData();
    }

    private View initView(Context context) {
        View view = View.inflate(context, R.layout.lv_analyse, null);
        mNodata = (RelativeLayout) view.findViewById(R.id.data_empty);
        mScoredata = (ScrollView) view.findViewById(R.id.fx_scroll_data);
        mSynthesizeMain = (TextView) view.findViewById(R.id.fx_synthesize_main);
        mSynthesizeMiddle = (TextView) view.findViewById(R.id.fx_synthesize_middle);
        mSynthesizeSecond = (TextView) view.findViewById(R.id.fx_synthesize_second);
        mSynthesizeProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_synthesize_progress);

        mPowerMainZj = (TextView) view.findViewById(R.id.fx_power_main_zj);
        mPowerMainJf = (TextView) view.findViewById(R.id.fx_power_main_jf);
        mPowerMiddle = (TextView) view.findViewById(R.id.fx_power_middle);
        mPowerSecondJf = (TextView) view.findViewById(R.id.fx_power_second_jf);
        mPowerSecondZj = (TextView) view.findViewById(R.id.fx_power_second_zj);
        mPowerProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_power_progress);

        mMainSecondMainJf = (TextView) view.findViewById(R.id.fx_mian_second_main_jf);
        mMainSecondMainZj = (TextView) view.findViewById(R.id.fx_mian_second_main_zj);
        mMainSecondMiddle = (TextView) view.findViewById(R.id.fx_mian_second_middle);
        mMainSecondSecondJf = (TextView) view.findViewById(R.id.fx_mian_second_second_jf);
        mMainSecondSecondZj = (TextView) view.findViewById(R.id.fx_mian_second_second_zj);
        mMainSecondProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_mian_second_progress);

        mattackMain = (TextView) view.findViewById(R.id.fx_attack_main);
        mattackMiddle = (TextView) view.findViewById(R.id.fx_attack_middle);
        mattackSecond = (TextView) view.findViewById(R.id.fx_attack_second);
        mattackProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_attack_progress);

        mdefendMain = (TextView) view.findViewById(R.id.fx_defend_main);
        mdefendMiddle = (TextView) view.findViewById(R.id.fx_defend_middle);
        mdefendSecond = (TextView) view.findViewById(R.id.fx_defend_second);
        mdefendProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_defend_progress);

        mIntegralMainTeamIcon = (ImageView) view.findViewById(R.id.fx_integral_main_team_icon);
        mFXCupLV = (ListView) view.findViewById(R.id.fx_cup_lv);
        mIntegralMainTeam = (TextView) view.findViewById(R.id.fx_integral_main_team);
        mIntegralMainFrameLayout = (LinearLayout) view.findViewById(R.id.fx_integral_main_framelayout);

        mIntegralSecondTeamIcon = (ImageView) view.findViewById(R.id.fx_integral_second_team_icon);
        mIntegralSecondTeam = (TextView) view.findViewById(R.id.fx_integral_second_team);
        mIntegralSecondFrameLayout = (LinearLayout) view.findViewById(R.id.fx_integral_second_framelayout);

        mhistoryMain = (TextView) view.findViewById(R.id.fx_history_main);
        mhistoryMiddle = (TextView) view.findViewById(R.id.fx_history_middle);
        mhistorySecond = (TextView) view.findViewById(R.id.fx_history_second);
        mhistoryProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_history_progress);

        mRecomMain = (TextView) view.findViewById(R.id.fx_recom_main);
        mRecomSecond = (TextView) view.findViewById(R.id.fx_recom_second);
        mRecomMain2 = (TextView) view.findViewById(R.id.fx_recom_main2);
        mRecomSecond2 = (TextView) view.findViewById(R.id.fx_recom_second2);
        mRecomProgress = (SoftSpaceStyle) view.findViewById(R.id.fx_recom_progress);
        return view;
    }

    private void initData() {
        /**
         * a)请求地址：/v1.0/matchAnaly/all/matchID/{matchID}
         b)	请求方式Get
         c)	请求参数说明：id：比赛ID
         */

        SSQSApplication.apiClient(0).getMatchAnalyAll(matchId, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    FXBean bean = (FXBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    mScoredata.setVisibility(View.GONE);
                    mNodata.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void processData(FXBean fxBean) {

        if (fxBean != null) {
            mScoredata.setVisibility(View.VISIBLE);
            mNodata.setVisibility(View.GONE);
            mSynthesizeMain.setText(String.valueOf(fxBean.hOverall));
            mSynthesizeMiddle.setText("综合");
            mSynthesizeSecond.setText(String.valueOf(fxBean.aOverall));
            mSynthesizeProgress.setProgressMax(100);
            mSynthesizeProgress.setProgress(fxBean.hOverall);

            mPowerMainJf.setText(String.valueOf(fxBean.hZlPoint));
            mPowerMainZj.setText(String.valueOf(fxBean.homeZl));
            mPowerSecondJf.setText(String.valueOf(fxBean.aZlPoint));
            mPowerSecondZj.setText(String.valueOf(fxBean.awayZl));
            mPowerMiddle.setText("战力");
            mPowerProgress.setProgressMax(100);
            mPowerProgress.setProgress(fxBean.homeZlPer);


            mMainSecondMainJf.setText(String.valueOf(fxBean.hZlPoint));
            mMainSecondMainZj.setText(fxBean.homeZk);
            mMainSecondSecondJf.setText(fxBean.aZkPoint);
            mMainSecondSecondZj.setText(fxBean.awayZk);
            mMainSecondMiddle.setText("主客");

            mMainSecondProgress.setProgressMax(100);
            mMainSecondProgress.setProgress(fxBean.homeZkPer);

            mattackMain.setText(fxBean.hAvGetBall);
            mattackMiddle.setText("攻击");
            mattackSecond.setText(fxBean.aAvGetBall);
            mattackProgress.setProgressMax(100);
            mattackProgress.setProgress(fxBean.hAvGetBallPer);

            mdefendMain.setText(fxBean.hAvLostBall);
            mdefendMiddle.setText("防守");
            mdefendSecond.setText(fxBean.aAvLostBall);
            mdefendProgress.setProgressMax(100);
            mdefendProgress.setProgress(fxBean.hAvLostBallPer);

            SSQSApplication.glide.load(AImageUrl).error(R.mipmap.icon_fail).centerCrop().into(mIntegralSecondTeamIcon);

            SSQSApplication.glide.load(HImageUrl).error(R.mipmap.icon_fail).centerCrop().into(mIntegralMainTeamIcon);

            mIntegralMainTeam.setText(home);
            mIntegralSecondTeam.setText(away);

            FXBean.HistoryEntity history = fxBean.history;
            String main = "主胜" + history.homeCount + "场";
            mhistoryMain.setText(main);
            String draw = "平局" + history.drawCount + "场";
            mhistoryMiddle.setText(draw);
            String away = "客胜" + history.awayCount + "场";
            mhistorySecond.setText(away);
            //一胜三平一负
            mhistoryProgress.setProgressMax(history.homeCount + history.drawCount + history.awayCount);
            mhistoryProgress.setProgress(history.homeCount);
            mhistoryProgress.setProgresSecondsMax(history.drawCount);
            ArrayList<LinearLayout> layoutMainList = new ArrayList<>();
            getMainLy(layoutMainList, mIntegralMainFrameLayout);
            ArrayList<LinearLayout> layoutSecondList = new ArrayList<>();
            getMainLy(layoutSecondList, mIntegralSecondFrameLayout);

            /**
             * 积分排名textview赋值
             */
            mTopS = new String[]{"", "排名", "已赛", "胜", "平", "负", "得/失", "积分", "胜率"};
            mTopSCup = new String[]{"排名", "队名", "已赛", "胜", "平", "负", "得/失", "积分", "胜率"};


            List<FXBean.HOrderEntity> mHOrder = fxBean.hOrder;
            List<FXBean.AOrderEntity> mAOrder = fxBean.aOrder;
            /**
             * 判断是否是杯赛 1连2杯
             */
            switch (fxBean.matchType) {
                case 1:
                    mIntegralMainTeamIcon.setVisibility(View.VISIBLE);
                    mIntegralMainTeam.setVisibility(View.VISIBLE);
                    mIntegralMainFrameLayout.setVisibility(View.VISIBLE);

                    mIntegralSecondTeamIcon.setVisibility(View.VISIBLE);
                    mIntegralSecondTeam.setVisibility(View.VISIBLE);
                    mIntegralSecondFrameLayout.setVisibility(View.VISIBLE);

                    mIntegralMainFrameLayout.removeAllViews();
                    mIntegralSecondFrameLayout.removeAllViews();

                    layoutMainList.clear();
                    layoutSecondList.clear();

                    getMainLy(layoutMainList, mIntegralMainFrameLayout);
                    getMainLy(layoutSecondList, mIntegralSecondFrameLayout);
                    setTvData(layoutMainList, mHOrder);
                    setTvDataScond(layoutSecondList, mAOrder);
                    mFXCupLV.setVisibility(View.GONE);
                    break;
                case 2:
                    mIntegralMainTeamIcon.setVisibility(View.GONE);
                    mIntegralMainTeam.setVisibility(View.GONE);
                    mIntegralMainFrameLayout.setVisibility(View.GONE);

                    mIntegralSecondTeamIcon.setVisibility(View.GONE);
                    mIntegralSecondTeam.setVisibility(View.GONE);
                    mIntegralSecondFrameLayout.setVisibility(View.GONE);

                    mFXCupLV.setVisibility(View.VISIBLE);
                    mFXCupLV.setAdapter(new FXCupAdapter(context, fxBean.hOrder, mTopSCup));
                    ListScrollUtil.setListViewHeightBasedOnChildren(mFXCupLV);
            }
            mRecomMain.setText(fxBean.record.hWinDesc);
            mRecomSecond.setText(fxBean.record.aWinDesc);
            mRecomMain2.setText(fxBean.record.hRate);
            mRecomSecond2.setText(fxBean.record.aRate);
            mRecomProgress.setProgressMax(fxBean.record.homeZl + fxBean.record.awayZl);
            mRecomProgress.setProgress(fxBean.record.homeZl);
        }
    }

    private void getMainLy(ArrayList<LinearLayout> layoutList, LinearLayout frameLayout) {
        for (int i = 0; i < 5; i++) {
            LinearLayout layout = getLinearLayout();
            int height = DensityUtil.dip2px(context, 30);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    height);
            int width = DensityUtil.dip2px(context, 5);
            params.leftMargin = width;
            params.rightMargin = width;
            layout.setLayoutParams(params);
            layout.setGravity(Gravity.CENTER_VERTICAL);
            switch (i) {
                case 0:
                    layout.setBackgroundResource(R.mipmap.background_selectrect);
                    break;
                case 1:
                case 3:
                    layout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    break;
                case 2:
                    layout.setBackgroundColor(context.getResources().getColor(R.color.yellow_light2));
                    break;
                case 4:
                    layout.setBackgroundColor(context.getResources().getColor(R.color.blue_Light2));
                    break;
                default:
                    break;
            }
            layoutList.add(layout);
            frameLayout.addView(layout);
        }
    }

    private void setTvDataScond(ArrayList<LinearLayout> layoutList, List<FXBean.AOrderEntity> aOrder) {
        for (int j = 0; j < layoutList.size(); j++) {
            for (int i = 0; i <= 8; i++) {
                if (j == 0) {
                    TextView tv = getTextView(mTopS[i]);
                    tv.setTextSize(13);
                    layoutList.get(j).addView(tv);
                } else {
                    if (aOrder != null) {
                        FXBean.AOrderEntity entity = aOrder.get(j - 1);
                        String ho = entity.nums.get(i);
                        TextView tv = getTextView(ho);
                        if (i == 0) {
                            tv.setTextSize(12);
                        } else {
                            tv.setTextSize(11);
                        }
                        LinearLayout layout = layoutList.get(j);
                        layout.addView(tv);
                    }
                }
            }
        }
    }

    private void setTvData(ArrayList<LinearLayout> layoutList, List<FXBean.HOrderEntity> HOrder) {
        for (int j = 0; j < layoutList.size(); j++) {
            for (int i = 0; i <= 8; i++) {
                if (j == 0) {
                    TextView tv = getTextView(mTopS[i]);
                    tv.setTextSize(12);
                    layoutList.get(j).addView(tv);
                } else {
                    FXBean.HOrderEntity entity = HOrder.get(j - 1);
                    String ho = entity.nums.get(i);
                    TextView tv = getTextView(ho);
                   /* if (i == 0) {
                        tv.setTextSize(12);
                    } else {
                        tv.setTextSize(11);
                    }*/
                    tv.setTextSize(11);
                    LinearLayout layout = layoutList.get(j);
                    layout.addView(tv);
                }
            }
        }
    }


    @NonNull
    private LinearLayout getLinearLayout() {
        LinearLayout layout = new LinearLayout(context);

        int diffH = DensityUtil.dip2px(context, 30);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, diffH);
        int diffx = DensityUtil.dip2px(context, 5);
        params.setMargins(diffx, 0, diffx, 0);
        layout.setLayoutParams(params);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    @NonNull
    private TextView getTextView(String ho) {

        int diffx = DensityUtil.dip2px(context, 3);
        int diffw = DensityUtil.dip2px(context, 18);
        TextView tv = new TextView(context);
        mParams = new LinearLayout.LayoutParams(diffw, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        mParams.setMargins(0, diffx, diffx, diffx);
        tv.setMinWidth(diffw);
        tv.setLayoutParams(mParams);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setTextColor(context.getResources().getColor(R.color.black));
        String s = "名";
        if (ho.equals(s)) {
            tv.setText("");
        } else {
            tv.setText(ho);
        }
        return tv;
    }
}
