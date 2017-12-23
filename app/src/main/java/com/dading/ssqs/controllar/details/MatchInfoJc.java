package com.dading.ssqs.controllar.details;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.BettingRecordActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.activity.NewRechargeActivity;
import com.dading.ssqs.adapter.HotMatchInfoLAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.apis.elements.PayBallScoreElement;
import com.dading.ssqs.bean.BetBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCScorebean;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.interfaces.MyItemClickListern;
import com.dading.ssqs.interfaces.MyItemSonCloseClickListern;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ThreadPoolUtils;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.MyGridView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/14 17:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoJc implements View.OnClickListener/*, CompoundButton.OnCheckedChangeListener */ {
    private static final String TAG = "MatchInfoJc";
    private final Context context;
    public final View mRootView;
    private final int matchId;
    private final int isOver;

    private TextView mPlayHelp;
    private TextView mBettingHistory;
    private ImageView mIvClose;
    private View mPopView;
    public View mView;
    private LinearLayout mLyOut;
    private boolean mIsLoading;
    private LinearLayout mHistoryLyOut;
    private ImageView mHistoryIvClose;
    private View mPopView2;
    private LinearLayout mBettingLy;
    private TextView mBettingNum;
    public View mBettingPopHeaderView;
    public PopupWindow mBettingHeadPop;
    private View mPopBodyView;
    public PopupWindow mPopBettingBody;
    private ImageView mBettingBodyClose;
    private TextView mBettingBodyLoading;
    private TextView mBettingBodyDel;
    private TextView mBettingBodyNum;
    private ListView mBettingBodyLv;
    private TextView mBettingBodyUpload;
    private TextView mBettingBodyGoldBalance;
    private PopBettingBodyAdapter mAdapterHot;
    private LinearLayout.LayoutParams mParams;
    private List<JCbean> mData;
    public HashMap<Integer, BetBean> mCbBean;
    private LinearLayout mJcNoData;
    private ScrollView mJcHaveData;
    private double mDownGolod;
    private LinearLayout mBettingAllResult;
    private LinearLayout mBettingNowLost;
    private LinearLayout mBettingAllSb;
    private LinearLayout mBettingHalfResult;
    private LinearLayout mBettingHalfLost;
    private LinearLayout mBettingHalfSb;
    public boolean mIsShow;
    public JCRecevice mRecevice;
    private MyHandle mMyHandle;
    private LinearLayout mBettingHotLy;
    private LinearLayout mBettingScoreLy;
    private LinearLayout mBettingScoreIntervalLy;
    private LinearLayout mBettingScoreAllResultlLy;
    private TextView mBettingScoreAllResultlWin;
    private TextView mBettingScoreAllResultlDraw;
    private TextView mBettingScoreAllResultlLost;
    private LinearLayout mBettingScoreHalfResult;
    private LinearLayout mBettingScoreAllSingleDouble;
    private LinearLayout mBettingScoreHalfSingleDouble;
    private MyGridView mGvInterval;
    private MyGridView mGvWin;
    private MyGridView mGvDraw;
    private MyGridView mGvLost;
    private MyGridView mGvHalfResult;
    private MyGridView mGvSingleDouble;
    private MyGridView mGvHalfSingleDouble;
    private MyGridView mGvOther;
    private TextView mBettingScoreAllOther;
    private RadioGroup mJcCheckGp;
    private RadioButton mJcCheckHot;
    private JCScoreIntervalAdapter mAdapterInterVal;
    private JCScoreIntervalAdapter mAdapterWin;
    private JCScoreIntervalAdapter mAdapterDraw;
    private JCScoreIntervalAdapter mAdapterLost;
    private JCScoreIntervalAdapter mAdapterOther;
    private JCScoreIntervalAdapter mAdapterhalfAllResult;
    private JCScoreIntervalAdapter mAdapterAllSingeDouble;
    private JCScoreIntervalAdapter mAdapterHalfSingeDouble;
    private HashMap<Integer, HashMap<Integer, JCScorebean.ListEntity.ItemsEntity>> mMapChecks;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsInterval;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsWin;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsDraw;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsLost;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsOther;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsHalfAll;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsAllSingeDouble;
    private List<JCScorebean.ListEntity.ItemsEntity> mItemsHalfSingleDouble;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksInterVal;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksWin;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksHalfAllResult;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksAllSingleDouble;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksHalfSingleDouble;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksDraw;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksLost;
    private HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> mMapChecksOther;
    public boolean mHotChecked;
    private int mCount;
    private PopBettingBodyScoreAdapter mPopBettingBodyScoreAdapter;
    private ArrayList<JCScorebean.ListEntity.ItemsEntity> mListScore;
    private boolean mIsShowScore;
    private LinearLayout.LayoutParams mParamsW;
    private View mPopFloatView;
    private LinearLayout mFLoatPlay;
    private LinearLayout mFLoatRecord;
    private Button mFLoatClose;
    private PopupWindow mPopFloat;
    private Button mMain;
    private PopupWindow mPopupWindowPlay;
    private RelativeLayout mFLoatJC;
    private ListView mHotRecycle;
    public HashMap<Integer, JCbean> mChecked;
    private int mI;
    private HotMatchInfoLAdapter mHotAdapterL;
    private String mHomeVsAway;
    private boolean mIsFootBall;

    private class MyHandle extends Handler {
        private final WeakReference<MatchInfoActivity> activity;

        MyHandle(MatchInfoActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public MatchInfoJc(Context context, int matchId, int isOver, boolean isDraw) {
        this.context = context;
        this.matchId = matchId;
        this.isOver = isOver;
        mRootView = initView(context);
        initData();
        initListener();
    }

    private void initData() {
        mIsFootBall = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        mHomeVsAway = ((MatchInfoActivity) context).mData.home + "vs" + ((MatchInfoActivity) context).mData.away;

        mCbBean = new HashMap<>();
        mMapChecks = new HashMap<>();
        mMapChecksInterVal = new HashMap<>();
        mMapChecksWin = new HashMap<>();
        mMapChecksDraw = new HashMap<>();
        mMapChecksLost = new HashMap<>();
        mMapChecksOther = new HashMap<>();
        mMapChecksHalfAllResult = new HashMap<>();
        mMapChecksAllSingleDouble = new HashMap<>();
        mMapChecksHalfSingleDouble = new HashMap<>();
        mListScore = new ArrayList<>();
        mMyHandle = new MyHandle((MatchInfoActivity) context);

        mRecevice = new JCRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.IS_SHOW2);
        UIUtils.ReRecevice(mRecevice, Constent.HOT_DATA);

        mBettingAllResult.setVisibility(View.GONE);
        mBettingNowLost.setVisibility(View.GONE);
        mBettingAllSb.setVisibility(View.GONE);
        mBettingHalfResult.setVisibility(View.GONE);
        mBettingHalfLost.setVisibility(View.GONE);
        mBettingHalfSb.setVisibility(View.GONE);

        mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);

        if (isOver == 2) {
            mJcNoData.setVisibility(View.VISIBLE);
        } else {
            mJcHaveData.setVisibility(View.VISIBLE);
        }

        mJcCheckHot.setChecked(true);
        mBettingHotLy.setVisibility(View.VISIBLE);
        mBettingScoreLy.setVisibility(View.GONE);

        getNetDataWork();
        /**
         * 篮球
         * 8.	根据比赛ID获取篮球赔率列表接口（优化后的）
         a)	请求地址：
         /v1.0/pay/ball/matchID/{matchID}
         */

        SSQSApplication.apiClient(0).getPayListById(UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true), matchId + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCbean> items = (List<JCbean>) result.getData();

                    if (items != null) {
                        processData(items);
                    }
                } else {
                    mJcCheckGp.setVisibility(View.GONE);
                    TmtUtils.midToast(UIUtils.getContext(), "赔率请求失败" + result.getMessage(), 0);
                    Logger.d(TAG, result.getMessage() + "失败信息");
                }
            }
        });
    }

    private void getNetDataWork() {
        SSQSApplication.apiClient(0).getScoreByIdList(matchId + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCScorebean> items = (List<JCScorebean>) result.getData();

                    if (items != null) {
                        processDataScore(items);
                    }
                } else {
                    mJcCheckGp.setVisibility(View.GONE);
                    TmtUtils.midToast(UIUtils.getContext(), "赔率请求失败" + result.getMessage(), 0);
                    Logger.d(TAG, result.getMessage() + "失败信息");
                }
            }
        });
    }

    private void processDataScore(List<JCScorebean> bean) {
        /**
         * list	数组
         type	int		1进球数区间
         2全场比分
         3半全场赛果
         4全场单双
         5半场单双
         字段名	类型	长度	备注
         id	int		比分赔率主键
         name	string		名称
         payRate	string		赔率
         只有全场比分的格式有点不一样（多了一层）
         字段名	类型	长度	备注
         name	string		全场比分类别名字
         item	数组		数据
         备注：整个data为空不显示
         */
        //mBettingScoreLy.setVisibility(View.VISIBLE);
        if (bean.size() == 0) {
            mJcCheckGp.setVisibility(View.GONE);
            mBettingScoreLy.setVisibility(View.GONE);
        } else {
            mJcNoData.setVisibility(View.GONE);
            for (JCScorebean entity : bean) {
                switch (entity.type) {
                    case 1:
                        //进球区间
                        if (entity.list.size() > 0) {
                            mItemsInterval = entity.list.get(0).items;
                            mBettingScoreIntervalLy.setVisibility(View.VISIBLE);
                            if (mItemsInterval.size() > 0)
                                mItemsInterval.get(0).type = 1;
                            mAdapterInterVal = new JCScoreIntervalAdapter(context, mItemsInterval, entity.type);
                            mGvInterval.setAdapter(mAdapterInterVal);
                        }
                        break;
                    case 2:
                        mBettingScoreAllResultlLy.setVisibility(View.VISIBLE);
                        for (JCScorebean.ListEntity list : entity.list) {
                            switch (list.name) {
                                case "主胜":
                                    if (entity.list.size() > 0) {
                                        mItemsWin = entity.list.get(0).items;
                                        if (mItemsWin.size() == 1)
                                            mGvWin.setNumColumns(2);
                                        if (mItemsWin.size() > 0) {
                                            mItemsWin.get(0).type = 2;
                                            mItemsWin.get(0).wdr = 1;
                                        }
                                        mAdapterWin = new JCScoreIntervalAdapter(context, mItemsWin, entity.type);
                                        mGvWin.setAdapter(mAdapterWin);
                                        mBettingScoreAllResultlWin.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "平":
                                    if (entity.list.size() > 1) {
                                        mItemsDraw = entity.list.get(1).items;
                                        if (mItemsDraw.size() == 1)
                                            mGvDraw.setNumColumns(2);
                                        if (mItemsDraw.size() > 0) {
                                            mItemsDraw.get(0).type = 2;
                                            mItemsDraw.get(0).wdr = 2;
                                        }
                                        mItemsWin.get(0).wdr = 1;
                                        mAdapterDraw = new JCScoreIntervalAdapter(context, mItemsDraw, entity.type);
                                        mGvDraw.setAdapter(mAdapterDraw);

                                        mBettingScoreAllResultlDraw.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "主负":
                                    if (entity.list.size() > 2) {
                                        mItemsLost = entity.list.get(2).items;
                                        if (mItemsLost.size() == 1)
                                            mGvLost.setNumColumns(2);
                                        if (mItemsLost.size() > 0) {
                                            mItemsLost.get(0).type = 2;
                                            mItemsLost.get(0).wdr = 3;
                                        }
                                        mAdapterLost = new JCScoreIntervalAdapter(context, mItemsLost, entity.type);
                                        mGvLost.setAdapter(mAdapterLost);
                                        mBettingScoreAllResultlLost.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case "其他":
                                    if (entity.list.size() > 3) {
                                        mItemsOther = entity.list.get(3).items;
                                        if (mItemsOther.size() == 1)
                                            mGvOther.setNumColumns(2);
                                        if (mItemsOther.size() > 0) {
                                            mItemsOther.get(0).type = 2;
                                            mItemsOther.get(0).wdr = 4;
                                        }
                                        mAdapterOther = new JCScoreIntervalAdapter(context, mItemsOther, entity.type);
                                        mGvOther.setAdapter(mAdapterOther);
                                        mBettingScoreAllOther.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                    case 3:
                        if (entity.list.size() > 0) {
                            mItemsHalfAll = entity.list.get(0).items;
                            if (mItemsHalfAll.size() == 1)
                                mGvHalfResult.setNumColumns(2);
                            if (mItemsHalfAll.size() > 0) {
                                mItemsHalfAll.get(0).type = 3;
                            }
                            mBettingScoreHalfResult.setVisibility(View.VISIBLE);
                            mAdapterhalfAllResult = new JCScoreIntervalAdapter(context, mItemsHalfAll, entity.type);
                            mGvHalfResult.setAdapter(mAdapterhalfAllResult);
                        }
                        break;
                    case 4:
                        if (entity.list.size() > 0) {
                            mItemsAllSingeDouble = entity.list.get(0).items;
                            if (mItemsAllSingeDouble.size() > 0) {
                                mItemsAllSingeDouble.get(0).type = 4;
                            }
                            mAdapterAllSingeDouble = new JCScoreIntervalAdapter(context, mItemsAllSingeDouble, entity.type);
                            mGvSingleDouble.setAdapter(mAdapterAllSingeDouble);
                            mBettingScoreAllSingleDouble.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 5:
                        if (entity.list.size() > 0) {
                            mItemsHalfSingleDouble = entity.list.get(0).items;
                            if (mItemsHalfSingleDouble.size() > 0) {
                                mItemsHalfSingleDouble.get(0).type = 5;
                            }
                            mAdapterHalfSingeDouble = new JCScoreIntervalAdapter(context, mItemsHalfSingleDouble, entity.type);
                            mGvHalfSingleDouble.setAdapter(mAdapterHalfSingeDouble);
                            mBettingScoreHalfSingleDouble.setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        break;
                }
            }
        }
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 将layout转成view,因为dialog表示容器所以不能写true要写成空
        View contentView = View.inflate(context, R.layout.dialog_init, null);
        TextView notice = (TextView) contentView.findViewById(R.id.dialog_tv_notice);
        notice.setText(context.getString(R.string.balance_dialog_glod));
        builder.setView(contentView);

        final AlertDialog dialog = builder.create();

        // 找到cancel的控件id实行监听
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "取消对话框");
                        dialog.dismiss();
                    }
                });

        // 找到confirm的控件id实行监听
        contentView.findViewById(R.id.btn_confirm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "确认对话框");
                        mBettingLy.setFocusable(false);
                        if (mAdapterHot != null)
                            if (mHotChecked) {
                                isNullClear(mCbBean);
                                mAdapterHot.notifyDataSetChanged();
                            } else {
                                isNullClear(mMapChecks);
                                isNullClear(mListScore);
                                mPopBettingBodyScoreAdapter.notifyDataSetChanged();
                            }
                        isDissmiss(mBettingHeadPop);
                        isDissmiss(mPopBettingBody);

                        dialog.dismiss();
                        Intent intent = new Intent(context, NewRechargeActivity.class);
                        if (UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false)) {
                            TmtUtils.midToast(context, "试玩账号不能进行充值，提现，和查看提现明细、账户明细!", 0);
                        } else {
                            intent.putExtra(Constent.DIAMONDS, "2");
                            context.startActivity(intent);
                            Animation animation = popUp();
                            mBettingLy.startAnimation(animation);
                        }
                    }
                });
        dialog.show();
    }

    private void processData(List<JCbean> bean) {
        Logger.d(TAG, "竞猜返回数据是------------------------------:" + bean.toString());
        mData = bean;
        if (mData.size() == 0) {
            mBettingHotLy.setVisibility(View.GONE);
            mJcCheckGp.setVisibility(View.GONE);
        } else {
            mJcNoData.setVisibility(View.GONE);
            mBettingHotLy.setVisibility(View.GONE);
            mHotRecycle.setVisibility(View.VISIBLE);

            // mHotRecycle.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            // HotMatchInfoAdapter adapter = new HotMatchInfoAdapter( R.id.hot_match_info_item,mData,context);
            boolean mAllResult = false;
            boolean mHalfResult = false;
            boolean mAllSBig = false;
            boolean mHalfSBig = false;
            boolean mNowLost = false;
            boolean mHalfLost = false;
            for (JCbean d : mData) {
                switch (d.payTypeName) {
                    case "全场赛果":
                        if (mAllResult) {
                            d.mAllResult = 1;
                        } else {
                            d.mAllResult = 0;
                            mAllResult = true;
                        }
                        break;
                    case "当前让球":
                        if (mNowLost) {
                            d.mNowLost = 1;
                        } else {
                            d.mNowLost = 0;
                            mNowLost = true;
                        }
                        break;
                    case "半场让球":
                        if (mHalfLost) {
                            d.mHalfLost = 1;
                        } else {
                            d.mHalfLost = 0;
                            mHalfLost = true;
                        }
                        break;
                    case "全场大小":
                        if (mAllSBig) {
                            d.mAllSBig = 1;
                        } else {
                            d.mAllSBig = 0;
                            mAllSBig = true;
                        }
                        break;
                    case "半场赛果":
                        if (mHalfResult) {
                            d.mHalfResult = 1;
                        } else {
                            d.mHalfResult = 0;
                            mHalfResult = true;
                        }
                        break;
                    case "半场大小":
                        if (mHalfSBig) {
                            d.mHalfSBig = 1;
                        } else {
                            d.mHalfSBig = 0;
                            mHalfSBig = true;
                        }
                        break;
                }
            }
            mHotAdapterL = new HotMatchInfoLAdapter(mData, context, mIsFootBall);
            mHotRecycle.setAdapter(mHotAdapterL);
            ListScrollUtil.setListViewHeightBasedOnChildren(mHotRecycle);
        }
    }

    private View initView(Context context) {
        mView = View.inflate(context, R.layout.lv_guess, null);
        mJcNoData = (LinearLayout) mView.findViewById(R.id.jc_no_data);
        mJcHaveData = (ScrollView) mView.findViewById(R.id.jc_hava_data);
        mPlayHelp = (TextView) mView.findViewById(R.id.jc_play_help);
        mHotRecycle = (ListView) mView.findViewById(R.id.jc_hot_recycle);
        mBettingHistory = (TextView) mView.findViewById(R.id.jc_betting_history);
        mBettingAllResult = (LinearLayout) mView.findViewById(R.id.jc_betting_all_result);
        mBettingNowLost = (LinearLayout) mView.findViewById(R.id.jc_betting_now_lost);
        mBettingAllSb = (LinearLayout) mView.findViewById(R.id.jc_betting_all_sb);
        mBettingHalfResult = (LinearLayout) mView.findViewById(R.id.jc_betting_half_result);
        mBettingHalfLost = (LinearLayout) mView.findViewById(R.id.jc_betting_half_lost);
        mBettingHalfSb = (LinearLayout) mView.findViewById(R.id.jc_betting_half_sb);

        /**
         * 比分
         */

        mJcCheckGp = (RadioGroup) mView.findViewById(R.id.jc_cb_gp);
        mJcCheckHot = (RadioButton) mView.findViewById(R.id.jc_cb_hot);
        mBettingHotLy = (LinearLayout) mView.findViewById(R.id.jc_hot_betting);
        mBettingScoreLy = (LinearLayout) mView.findViewById(R.id.jc_score_ly);

        mPopFloatView = View.inflate(context, R.layout.guessball_pop_float, null);
        mFLoatJC = (RelativeLayout) mPopFloatView.findViewById(R.id.float_guessball);
        mFLoatClose = (Button) mPopFloatView.findViewById(R.id.float_close);
        mFLoatPlay = (LinearLayout) mPopFloatView.findViewById(R.id.float_play_introduce);
        mFLoatRecord = (LinearLayout) mPopFloatView.findViewById(R.id.float_betting_record);

        mPopFloat = PopUtil.popuMakeFalseW(mPopFloatView);

        mMain = (Button) mView.findViewById(R.id.fab_main_jc);
        mMain.bringToFront();


        //介绍标签
        mBettingScoreIntervalLy = (LinearLayout) mView.findViewById(R.id.jc_score_ball_interval);
        mBettingScoreAllResultlLy = (LinearLayout) mView.findViewById(R.id.jc_score_all_result);
        mBettingScoreAllResultlWin = (TextView) mView.findViewById(R.id.jc_score_all_main_win);
        mBettingScoreAllResultlDraw = (TextView) mView.findViewById(R.id.jc_score_all_draw);
        mBettingScoreAllResultlLost = (TextView) mView.findViewById(R.id.jc_score_all_main_lost);
        mBettingScoreAllOther = (TextView) mView.findViewById(R.id.jc_score_all_other);
        mBettingScoreHalfResult = (LinearLayout) mView.findViewById(R.id.jc_score_half_result);
        mBettingScoreAllSingleDouble = (LinearLayout) mView.findViewById(R.id.jc_score_all_single_double);
        mBettingScoreHalfSingleDouble = (LinearLayout) mView.findViewById(R.id.jc_score_half_single_double);
        //GrideView
        mGvInterval = (MyGridView) mView.findViewById(R.id.jc_score_gv_interval);
        mGvWin = (MyGridView) mView.findViewById(R.id.jc_score_gv_main_win);
        mGvDraw = (MyGridView) mView.findViewById(R.id.jc_score_gv_draw);
        mGvLost = (MyGridView) mView.findViewById(R.id.jc_score_gv_main_lost);
        mGvOther = (MyGridView) mView.findViewById(R.id.jc_score_gv_other);
        mGvHalfResult = (MyGridView) mView.findViewById(R.id.jc_score_gv_half_result);
        mGvSingleDouble = (MyGridView) mView.findViewById(R.id.jc_score_gv_single_double);
        mGvHalfSingleDouble = (MyGridView) mView.findViewById(R.id.jc_score_gv_half_single_double);


        //投注单
        mBettingPopHeaderView = View.inflate(context, R.layout.betting_pop_head, null);
        mBettingLy = (LinearLayout) mBettingPopHeaderView.findViewById(R.id.betting_list_ly);
        mBettingNum = (TextView) mBettingPopHeaderView.findViewById(R.id.betting_num);
        mBettingHeadPop = PopUtil.popuMakeWwf(mBettingPopHeaderView);

        //投注界面
        mPopBodyView = View.inflate(context, R.layout.betting_pop_body, null);
        mBettingBodyClose = (ImageView) mPopBodyView.findViewById(R.id.betting_body_close);
        mBettingBodyLoading = (TextView) mPopBodyView.findViewById(R.id.betting_body_loadingornum);
        mBettingBodyDel = (TextView) mPopBodyView.findViewById(R.id.betting_body_delete_all);
        mBettingBodyNum = (TextView) mPopBodyView.findViewById(R.id.betting_body_num);
        mBettingBodyLv = (ListView) mPopBodyView.findViewById(R.id.betting_body_lv);
        mBettingBodyUpload = (TextView) mPopBodyView.findViewById(R.id.betting_body_upload);
        mBettingBodyGoldBalance = (TextView) mPopBodyView.findViewById(R.id.betting_body_gold_balance);
        mPopBettingBody = PopUtil.popuMakeMwf(mPopBodyView);

        int px = DensityUtil.dip2px(context, 150);
        mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, px);

       /* SpannableString str2 = new SpannableString("投注记录");
        ForegroundColorSpan span2 = new ForegroundColorSpan(context.getResources().getColor(R.color.red));
        str2.setSpan(span2, 5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  //5-8个字符颜色为span2中设置的值不能为0
        mBettingHistory.setText(str2);*/

        mPopView = View.inflate(context, R.layout.jc_play_help_popu, null);
        mLyOut = (LinearLayout) mPopView.findViewById(R.id.jc_play_help_pop_out);
        mIvClose = (ImageView) mPopView.findViewById(R.id.jc_play_help_pop_close);

        mPopupWindowPlay = PopUtil.popuMake(mPopView);

        mPopView2 = View.inflate(context, R.layout.jc_play_history_pop, null);
        mHistoryLyOut = (LinearLayout) mPopView2.findViewById(R.id.jc_play_history_pop_out);
        mHistoryIvClose = (ImageView) mPopView2.findViewById(R.id.jc_play_history_pop_close);

        mHotChecked = true;
        return mView;
    }

    private void initListener() {
        mPopFloat.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mMain.setVisibility(View.VISIBLE);
            }
        });

        mFLoatJC.setOnClickListener(this);
        mFLoatRecord.setOnClickListener(this);
        mFLoatPlay.setOnClickListener(this);
        mFLoatClose.setOnClickListener(this);
        mMain.setOnClickListener(this);

        //help
        mIvClose.setOnClickListener(this);
        mLyOut.setOnClickListener(this);
        //记录
        mHistoryIvClose.setOnClickListener(this);
        mHistoryLyOut.setOnClickListener(this);

        //投注单
        mBettingLy.setOnClickListener(this);
        //投注界面
        mBettingBodyLoading.setOnClickListener(this);
        mBettingBodyClose.setOnClickListener(this);
        mBettingBodyDel.setOnClickListener(this);
        mBettingBodyUpload.setOnClickListener(this);
        mPlayHelp.setOnClickListener(this);
        mBettingHistory.setOnClickListener(this);

        mJcCheckGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isDissmiss(mBettingHeadPop);
                switch (checkedId) {
                    case R.id.jc_cb_hot:
                        mBettingHotLy.setVisibility(View.GONE);
                        mBettingScoreLy.setVisibility(View.GONE);
                        mHotRecycle.setVisibility(View.VISIBLE);
                        mHotChecked = true;
                        isShowHotHeadPop();
                        break;
                    case R.id.jc_cb_score:
                        mHotRecycle.setVisibility(View.GONE);
                        mBettingHotLy.setVisibility(View.GONE);
                        mBettingScoreLy.setVisibility(View.VISIBLE);
                        mHotChecked = false;
                        setHeadScore();
                        break;
                    default:
                        break;
                }
            }
        });
        mGvInterval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsInterval.get(position);
                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksInterVal.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksInterVal.put(position, entity);
                }

                mMapChecks.put(1, mMapChecksInterVal);
                setHeadScore();

                mAdapterInterVal.notifyDataSetChanged();
            }
        });
        mGvWin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsWin.get(position);

                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksWin.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksWin.put(position, entity);
                }
                mMapChecks.put(2, mMapChecksWin);
                setHeadScore();
                mAdapterWin.notifyDataSetChanged();
            }
        });
        mGvDraw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsDraw.get(position);

                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksDraw.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksDraw.put(position, entity);
                }
                mMapChecks.put(3, mMapChecksDraw);
                setHeadScore();
                mAdapterDraw.notifyDataSetChanged();
            }
        });
        mGvLost.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsLost.get(position);

                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksLost.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksLost.put(position, entity);
                }
                mMapChecks.put(4, mMapChecksLost);
                setHeadScore();
                mAdapterLost.notifyDataSetChanged();
            }
        });
        mGvOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsOther.get(position);

                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksOther.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksOther.put(position, entity);
                }
                mMapChecks.put(5, mMapChecksOther);
                setHeadScore();
                mAdapterOther.notifyDataSetChanged();
            }
        });
        mGvHalfResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsHalfAll.get(position);
                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksHalfAllResult.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksHalfAllResult.put(position, entity);
                }
                mMapChecks.put(6, mMapChecksHalfAllResult);
                setHeadScore();
                mAdapterhalfAllResult.notifyDataSetChanged();
            }
        });
        mGvSingleDouble.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsAllSingeDouble.get(position);
                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksAllSingleDouble.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksAllSingleDouble.put(position, entity);
                }
                mMapChecks.put(7, mMapChecksAllSingleDouble);
                setHeadScore();

                mAdapterAllSingeDouble.notifyDataSetChanged();
            }
        });
        mGvHalfSingleDouble.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JCScorebean.ListEntity.ItemsEntity entity = mItemsHalfSingleDouble.get(position);
                if (entity.checked) {
                    entity.checked = false;
                    mMapChecksHalfSingleDouble.remove(position);
                } else {
                    entity.checked = true;
                    mMapChecksHalfSingleDouble.put(position, entity);
                }
                mMapChecks.put(8, mMapChecksHalfSingleDouble);
                setHeadScore();
                mAdapterHalfSingeDouble.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.float_guessball:
                if (mPopFloat.isShowing())
                    isDissmiss(mPopFloat);
                break;
            case R.id.fab_main_jc:
                if (!mPopFloat.isShowing()) {
                    mPopFloat.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                    mMain.setVisibility(View.GONE);
                    if (mHotChecked)
                        setClearHOt();
                    else
                        setClearScore();
                }
                break;
            case R.id.float_play_introduce:
                isDissmiss(mPopFloat);
                mPopupWindowPlay.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.float_close:
                isDissmiss(mPopFloat);
                break;
            case R.id.float_betting_record:
                Intent intent;
                isDissmiss(mPopFloat);
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                    intent = new Intent(context, BettingRecordActivity.class);
                else
                    intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                break;
            case R.id.jc_play_help:
                mPopupWindowPlay.showAtLocation(mView, Gravity.CENTER, 0, 0);
                break;
            case R.id.jc_betting_history:
                mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                isNullClear(mCbBean);
                isDissmiss(mBettingHeadPop);
                if (mAdapterHot != null)
                    mAdapterHot.notifyDataSetChanged();
                if (mIsLoading)
                    intent = new Intent(context, BettingRecordActivity.class);
                else
                    intent = new Intent(context, LoginActivity.class);

                context.startActivity(intent);
                break;
            case R.id.jc_play_help_pop_close:
                isDissmiss(mPopupWindowPlay);
                break;
            case R.id.jc_play_help_pop_out:
                isDissmiss(mPopupWindowPlay);
                break;
            case R.id.jc_play_history_pop_close:
                break;
            case R.id.jc_play_history_pop_out:
                break;
            case R.id.betting_list_ly:
                if (mHotChecked) {
                    mI = 0;
                    mChecked = mHotAdapterL.getChecked();
                    Logger.d(TAG, "huoqu xuanz数据是------------------------------:" + mChecked.toString());
                    isNullClear(mCbBean);
                    for (int id : mChecked.keySet()) {
                        JCbean entity = mChecked.get(id);
                        if (entity.cbTag1) {
                            BetBean bean = getBetBean(entity);
                            bean.selected = 1;
                            bean.id = entity.id;
                            bean.realRate = entity.realRate1;
                            bean.payRateID = entity.id;///payRateId后台声明对应数据id值
                            mI = putData(mI, bean);
                        }
                        if (entity.cbTag2) {
                            BetBean bean = getBetBean(entity);
                            bean.selected = 3;
                            bean.id = entity.id;
                            bean.payRateID = entity.id;
                            bean.realRate = entity.realRate2;
                            mI = putData(mI, bean);
                        }
                        if (entity.cbTag3) {
                            BetBean bean = getBetBean(entity);
                            bean.selected = 2;
                            bean.id = entity.id;
                            bean.realRate = entity.realRate3;
                            bean.payRateID = entity.id;
                            mI = putData(mI, bean);
                        }
                    }
                    String text = mCbBean.size() + "";
                    mBettingBodyNum.setText(text);

                    Logger.d(TAG, "弹框主题------------------------------:" + mCbBean);
                    mAdapterHot = new PopBettingBodyAdapter(context, mCbBean);
                    mBettingBodyLv.setAdapter(mAdapterHot);

                    if (mCbBean.size() <= 1) {
                        mBettingBodyLv.setLayoutParams(mParams);
                    } else {
                        int height = ListScrollUtil.getLVheight(mBettingBodyLv);
                        int i = DensityUtil.dip2px(context, 350);
                        if (height > i)
                            mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, i);
                        else
                            mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);

                        mBettingBodyLv.setLayoutParams(mParamsW);
                    }
                    mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                    mPopBettingBody.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                    if (mIsLoading) {
                        String gold = UIUtils.getSputils().getString(Constent.GLODS, null);
                        Logger.d(TAG, "竞猜单子数据是------------------------------:金币" + UIUtils.getSputils().
                                getString(Constent.GLODS, null) + "钻石" + UIUtils.getSputils().getString(Constent.DIAMONDS, null));
                        mBettingBodyLoading.setText(gold);
                        mBettingBodyGoldBalance.setVisibility(View.VISIBLE);
                    } else {
                        mBettingBodyGoldBalance.setVisibility(View.GONE);
                        mBettingBodyLoading.setText("登录");
                    }
                    popDown();
                    mIsShow = false;
                } else {
                    String text = mCount + "";
                    mBettingBodyNum.setText(text);

                    isNullClear(mListScore);
                    for (Integer i : mMapChecks.keySet()) {
                        HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> map = mMapChecks.get(i);
                        Logger.d(TAG, "比分选中返回数据是------------------------------:" + i + "---" + map.size());
                        for (Integer j : map.keySet()) {
                            JCScorebean.ListEntity.ItemsEntity entity = map.get(j);
                            entity.type = i;
                            entity.amount = "请输入金币";
                            entity.returnNum = "0";
                            if (entity.checked)
                                mListScore.add(entity);
                        }
                    }
                    Logger.d(TAG, "比分的个数返回数据是------------------------------:" + mListScore.size());
                    mPopBettingBodyScoreAdapter = new PopBettingBodyScoreAdapter(context, mListScore, mHomeVsAway);
                    mBettingBodyLv.setAdapter(mPopBettingBodyScoreAdapter);
                    if (mListScore.size() <= 1) {
                        mBettingBodyLv.setLayoutParams(mParams);
                    } else {
                        int height = ListScrollUtil.getLVheight(mBettingBodyLv);
                        int i = DensityUtil.dip2px(context, 350);
                        if (height > i) {
                            mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, i);
                        } else {
                            mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                        }
                        mBettingBodyLv.setLayoutParams(mParamsW);
                    }
                    mPopBettingBody.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                    mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                    if (mIsLoading) {
                        String gold = UIUtils.getSputils().getString(Constent.GLODS, null);
                        Logger.d(TAG, "竞猜单子数据是------------------------------:金币" + UIUtils.getSputils().
                                getString(Constent.GLODS, null) + "钻石" + UIUtils.getSputils().getString(Constent.DIAMONDS, null));
                        mBettingBodyLoading.setText(gold);
                        mBettingBodyGoldBalance.setVisibility(View.VISIBLE);
                    } else {
                        mBettingBodyGoldBalance.setVisibility(View.GONE);
                        mBettingBodyLoading.setText("登录");
                    }
                    popDown();
                    mIsShowScore = false;
                }
                break;
            case R.id.betting_body_close:
                Logger.d(TAG, "您点击了pop关闭按钮------------------------------:");
                isDissmiss(mPopBettingBody);
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.in_from_down);
                animation1.setFillAfter(false);
                if (mHotChecked) {
                    isShowHotHeadPop();
                } else {
                    setHeadScore();
                    mBettingHeadPop.showAtLocation(mView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, mBettingPopHeaderView.getHeight() - 10);
                }
                break;
            case R.id.betting_body_delete_all:
                if (mHotChecked)
                    setClearHOt();
                else
                    setClearScore();
                break;
            case R.id.betting_body_upload:
                mDownGolod = 0;
                int minGlod = 0;
                if (UIUtils.getSputils().getBoolean(Constent.YP_IS_VISIABLE, false))
                    minGlod = 10;
                else
                    minGlod = 100;

                mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (mIsLoading) {
                    if (mHotChecked) {
                        ArrayList<BetBean> cbGlod = mAdapterHot.getGlod();
                        Logger.d(TAG, "回掉金币长度是------------------------------:" + cbGlod.size());
                        if (cbGlod.size() > 0) {
                            PayBallElement element = new PayBallElement();

                            for (BetBean bean : cbGlod) {
                                if (bean.amount.equals("请输入金币")) {
                                    TmtUtils.midToast(context, "请输入金币,下注金币不能少于" + minGlod + ",请检查金币", 1);
                                    return;
                                } else {
                                    if (TextUtils.isEmpty(bean.amount) || bean.amount.trim().length() > 7) {
                                        TmtUtils.midToast(context, "下注金额最少不得少于" + minGlod + ",最多不得大于500万!", 0);
                                        return;
                                    }
                                    int anInt = Integer.parseInt(bean.amount.trim());
                                    if (minGlod > anInt) {
                                        TmtUtils.midToast(context, "请输入金币,下注金币不能少于" + minGlod + ",请检查金币", 1);
                                        return;
                                    } else {
                                        PayBallElement.BetBean betBean = new PayBallElement.BetBean();
                                        Logger.d(TAG, "点击提交------------------------------:JC投注金币为" + bean.amount + "");
                                        betBean.type = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? 1 : 2;
                                        betBean.matchID = matchId;
                                        betBean.amount = bean.amount;
                                        betBean.selected = bean.selected;
                                        betBean.payRateID = bean.payRateID;
                                        element.getItems().add(betBean);
                                        mDownGolod += anInt;
                                    }
                                }
                            }
                            /**
                             * c)请求参数说明：
                             matchID:比赛ID
                             amount:下注的球币
                             payRateID: 赔率ID
                             selected:选择哪项 1-3--1:主胜2：客胜3：和局
                             auth_token：登陆后加入请求头
                             */
                            betMethod(element);
                        } else {
                            TmtUtils.midToast(context, "请输入您要下注的金币", 1);
                        }
                    } else {
                        ArrayList<JCScorebean.ListEntity.ItemsEntity> list = mPopBettingBodyScoreAdapter.getScoreList();

                        PayBallScoreElement element = new PayBallScoreElement();

                        for (JCScorebean.ListEntity.ItemsEntity en : list) {
                            if (en.amount.equals("请输入金币")) {
                                TmtUtils.midToast(context, "请输入金币,下注金币不能少于" + minGlod + "请检查金币", 1);
                                return;
                            } else {
                                if (TextUtils.isEmpty(en.amount) || en.amount.trim().length() > 7) {
                                    TmtUtils.midToast(context, "下注金额最少不得少于" + minGlod + ",最多不得大于500万!", 0);
                                    return;
                                }
                            }
                            Integer integer = Integer.valueOf(en.amount);
                            if (minGlod > integer) {
                                TmtUtils.midToast(context, "请输入金币,下注金币不能少于" + minGlod + ",请检查金币", 1);
                                return;
                            }
                            PayBallScoreElement.PayBallBean bean = new PayBallScoreElement.PayBallBean();
                            bean.setItemID(String.valueOf(en.id));
                            bean.setAmount(String.valueOf(integer));
                            element.getItems().add(bean);

                            mDownGolod += integer;
                        }

                        String s = UIUtils.getSputils().getString(Constent.GLODS, null);
                        double glod = Double.parseDouble(s);
                        if (mDownGolod > glod) {
                            initDialog();
                            return;
                        }

                        /**
                         * 11.	竞猜比分投注
                         1)	请求地址：/v1.0/payBall/score
                         2)	请求方式:post
                         auth_token	varchar		token
                         itemID	int		比分赔率主键ID
                         amount	int		投注金额
                         */

                        SSQSApplication.apiClient(0).payBallScore(element, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    TmtUtils.midToast(context, "下注成功!", 0);
                                    isDissmiss(mPopBettingBody);
                                    isDissmiss(mBettingHeadPop);
                                    setClearScore();
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        TmtUtils.midToast(context, result.getMessage(), 0);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    isNullClear(mCbBean);
                    if (mAdapterHot != null)
                        mAdapterHot.notifyDataSetChanged();

                    isNullClear(mMapChecks);
                    isNullClear(mListScore);

                    if (mPopBettingBodyScoreAdapter != null)
                        mPopBettingBodyScoreAdapter.notifyDataSetChanged();
                    isDissmiss(mBettingHeadPop);
                    intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    isDissmiss(mPopBettingBody);
                }

                break;
            case R.id.betting_body_loadingornum:
                mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                Logger.d(TAG, "登录倍点击了");
                if (!mIsLoading) {
                    Logger.d(TAG, "登录跳转");
                    setClearHOt();
                    if (mAdapterHot != null)
                        mAdapterHot.notifyDataSetChanged();

                    isNullClear(mMapChecks);
                    isNullClear(mListScore);
                    if (mPopBettingBodyScoreAdapter != null)
                        mPopBettingBodyScoreAdapter.notifyDataSetChanged();

                    isDissmiss(mBettingHeadPop);
                    isDissmiss(mPopBettingBody);

                    intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
                break;
        }
    }

    @NonNull
    private BetBean getBetBean(JCbean entity) {
        BetBean bean = new BetBean();
        bean.home = entity.home;
        bean.away = entity.away;
        bean.cbTag = false;
        bean.amount = "请输入金币";
        bean.returnNum = "0";
        bean.payTypeName = entity.payTypeName;
        return bean;
    }

    private int putData(int i, BetBean bean) {
        mCbBean.put(i, bean);
        i = i + 1;
        return i;
    }

    private void setClearScore() {
        isNullClear(mMapChecks);
        for (JCScorebean.ListEntity.ItemsEntity i : mListScore)
            getRefresh(i);

        isNullClear(mListScore);
        isDissmiss(mPopBettingBody);
        if (mPopBettingBodyScoreAdapter != null)
            mPopBettingBodyScoreAdapter.notifyDataSetChanged();

        setHeadScore();
        Logger.d(TAG, "比分清除完毕-------------");
    }


    private void setClearHOt() {
        backData();
        if (mHotAdapterL != null)
            mHotAdapterL.notifyDataSetChanged();
        if (mAdapterHot != null)
            mAdapterHot.notifyDataSetChanged();
        isDissmiss(mPopBettingBody);
        setClear();
        isShowHotHeadPop();
    }

    private void betMethod(PayBallElement element) {
        String s = UIUtils.getSputils().getString(Constent.GLODS, null);
        double v = Double.parseDouble(s);
        if (mDownGolod > v) {
            initDialog();
            return;
        }
        //// TODO: 2016/9/8  金币下注成功
        mBettingBodyUpload.setClickable(false);
        /**
         * okHttp post同步请求表单提交
         * @param actionUrl 接口地址
         * @param paramsMap 请求参数
         */

        SSQSApplication.apiClient(0).payBall(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    sucData();
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        mBettingBodyUpload.setClickable(true);
                    } else {
                        TmtUtils.midToast(context, result.getMessage(), 0);
                        mBettingBodyUpload.setClickable(true);
                    }
                }
            }
        });
    }

    private void sucData() {
        String beforeGold = UIUtils.getSputils().getString(Constent.GLODS, null);
        double glodValue = Double.parseDouble(beforeGold) - mDownGolod;
        String s = new DecimalFormat("#.00").format(glodValue);
        mBettingBodyLoading.setText(s);
        UIUtils.getSputils().putString(Constent.GLODS, s);

        //发送广播
        UIUtils.SendReRecevice(Constent.SERIES);

        isDissmiss(mPopBettingBody);

        setClear();
        backData();
        mHotAdapterL.notifyDataSetChanged();
        TmtUtils.midToast(context, "下注成功!", 0);

        mBettingBodyUpload.setClickable(true);
    }

    private void backData() {
        if (mData != null)
            for (JCbean d : mData) {
                d.cbTag1 = false;
                d.cbTag2 = false;
                d.cbTag3 = false;
            }
    }


    private void setHeadScore() {
        mCount = 0;
        isNullClear(mListScore);
        for (Integer i : mMapChecks.keySet()) {
            HashMap<Integer, JCScorebean.ListEntity.ItemsEntity> map = mMapChecks.get(i);
            for (int j : map.keySet()) {
                JCScorebean.ListEntity.ItemsEntity entity = map.get(j);
                if (entity.checked)
                    mListScore.add(entity);
            }
            mCount = mListScore.size();
        }
        Logger.d(TAG, "比分count返回数据是------------------------------:" + mCount);
        if (mCount > 0) {
            mBettingNum.setText(String.valueOf(mCount));
            mBettingBodyNum.setText(String.valueOf(mCount));
            Animation animation = popUp();
            if (!mIsShowScore)
                mBettingLy.startAnimation(animation);
            if (mCbBean.size() <= 1) {
                mBettingBodyLv.setLayoutParams(mParams);
            } else {
                int height = ListScrollUtil.getLVheight(mBettingBodyLv);
                int i = DensityUtil.dip2px(context, 450);
                if (height > i) {
                    mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, i);
                } else {
                    mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                }
                mBettingBodyLv.setLayoutParams(mParamsW);
            }
            mIsShowScore = true;
        } else {
            popDown();
            mIsShowScore = false;
        }
    }

    private void isShowHotHeadPop() {
        if (mChecked != null) {
            String size = mChecked.size() + "";
            Logger.d(TAG, "重新mmap返回数据是------------------------------:" + size);
            if (mChecked.size() > 0) {
                int i = 0;
                for (int id : mChecked.keySet()) {
                    JCbean entity = mChecked.get(id);
                    i = getNum(i, entity.cbTag1);
                    i = getNum(i, entity.cbTag2);
                    i = getNum(i, entity.cbTag3);
                    Logger.d(TAG, "选中个数是------------------------------:" + i);
                }
                mBettingNum.setText(String.valueOf(i));
                mBettingBodyNum.setText(String.valueOf(i));
                if (i > 0)
                    popUp();
                else
                    popDown();

                if (i <= 1) {
                    mBettingBodyLv.setLayoutParams(mParams);
                } else {
                    int height = ListScrollUtil.getLVheight(mBettingBodyLv);
                    int heighti = DensityUtil.dip2px(context, 450);
                    if (height > heighti)
                        mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, i);
                    else
                        mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);

                }
                if (!mIsShow) {
                    Animation animation = popUp();
                    mBettingLy.startAnimation(animation);
                    mIsShow = true;
                }
                Logger.d(TAG, "选中-----:" + mCbBean.size() + "显示pop" + mCbBean.toString());
            } else {
                Logger.d(TAG, "没有选中项-----:" + mCbBean.size() + "显示pop" + mCbBean.toString());
                popDown();
                mIsShow = false;
            }
        }
    }

    @NonNull
    private Animation popUp() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.in_from_down);
        animation.setFillAfter(false);

        if (!mBettingHeadPop.isShowing())
            mBettingHeadPop.showAtLocation(mView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, mBettingPopHeaderView.getHeight() - 10);

        return animation;
    }

    private void popDown() {
       /* Animation animation = AnimationUtils.loadAnimation(context, R.anim.out_from_down);
        animation.setFillAfter(false);
        mBettingLy.startAnimation(animation);*/
        if (mBettingHeadPop.isShowing())
            isDissmiss(mBettingHeadPop);
    }


    private class JCRecevice extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (mBettingHeadPop != null)
                switch (action) {
                    case Constent.HOT_DATA:
                        if (mHotAdapterL != null) {
                            mChecked = mHotAdapterL.getChecked();
                            Logger.d(TAG, "选中数据是--:" + mChecked.size());

                            if (mChecked.size() > 0) {
                                int i = 0;
                                for (int id : mChecked.keySet()) {
                                    JCbean entity = mChecked.get(id);
                                    i = getNum(i, entity.cbTag1);
                                    i = getNum(i, entity.cbTag2);
                                    i = getNum(i, entity.cbTag3);
                                    Logger.d(TAG, "选中个数是----:" + i);
                                }
                                mBettingNum.setText(String.valueOf(i));
                                if (i > 0)
                                    popUp();
                                else
                                    popDown();
                            } else {
                                popDown();
                            }
                        }
                        break;

                    case Constent.IS_SHOW2:
                        setClear();
                        ThreadPoolUtils.getInstance().addTask(new Runnable() {
                            @Override
                            public void run() {
                                mMyHandle.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setClearHOt();
                                        getNetDataWork();
                                        for (JCScorebean.ListEntity.ItemsEntity i : mListScore) {
                                            getRefresh(i);
                                        }
                                        isNullClear(mMapChecks);
                                        isNullClear(mListScore);
                                        isDissmiss(mPopBettingBody);
                                        Logger.d(TAG, "收到廣播----" + mHotChecked + "-------取消弹框");
                                    }
                                });
                            }
                        });
                        break;
                }
        }

    }

    private void setClear() {
        isNullClear(mChecked);
        isNullClear(mCbBean);
    }


    private int getNum(int i, boolean cbTag1) {
        if (cbTag1)
            i = i + 1;
        return i;
    }

    /**
     * 创建者     ZCL
     * 创建时间   2016/9/6 17:15
     * 描述	      ${TODO}
     * <p/>
     * 更新者     $Author$
     * 更新时间   $Date$
     * 更新描述   ${TODO}
     */
    public class PopBettingBodyAdapter extends BaseAdapter implements ListAdapter {
        private static final String TAG = "MatchInfoJc";
        private ArrayList<BetBean> mListBean;
        private Context context;
        private HashMap<Integer, BetBean> map;
        private final DecimalFormat mDf;
        private final HashMap<Integer, Integer> mListKeyBoard;

        public PopBettingBodyAdapter(Context context, HashMap<Integer, BetBean> map) {
            this.context = context;
            this.map = map;
            mDf = new DecimalFormat(".00");
            mListBean = new ArrayList<>();
            for (Map.Entry<Integer, BetBean> cb : map.entrySet()) {
                mListBean.add(cb.getValue());//获取bean的集合
            }
            mListKeyBoard = new HashMap<>();
        }

        @Override
        public int getCount() {
            if (mListBean != null) {
                return mListBean.size();
            }
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder hoder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.pop_betting_lv_item, null);
                hoder = new ViewHolder(convertView);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHolder) convertView.getTag();
            }
            /**
             * 还原初始化状态
             */
            switch (mListBean.get(position).selected) {
                case 1:
                    hoder.mBettingBodyItemResult.setText(mListBean.get(position).home);
                    break;
                case 3:
                    hoder.mBettingBodyItemResult.setText("平局");
                    break;
                case 2:
                    hoder.mBettingBodyItemResult.setText(mListBean.get(position).away);
                    break;
                default:
                    break;
            }

            final BetBean bean = mListBean.get(position);

            /**
             * 还原状态
             */
            hoder.mBettingBodyItemType.setText(bean.payTypeName);//下注
            String text = bean.home + "vs" + bean.away;
            hoder.mBettingBodyItemTeam.setText(text);//队伍
            hoder.mItemBettingBodyPL.setText(bean.realRate);//赔率

            hoder.mKeybordLy.setVisibility(bean.cbTag ? View.VISIBLE : View.GONE);//键盘
            if (bean.cbTag) {
                hoder.mBettingBodyItemInputGold.setChecked(true);//输入金额
            } else {
                hoder.mBettingBodyItemInputGold.setChecked(false);//输入金额
            }
            hoder.mBettingBodyItemInputGold.setText(bean.amount);//输入金额
            String returnNum = bean.returnNum + "";
            hoder.mBettingBodyItemExpectedReturn.setText(returnNum);

            hoder.mItemBettingBodyClose.setOnClickListener(new MyItemSonCloseClickListern(context, position) {
                @Override
                public void onClick(View v) {
                    Logger.d(TAG, "关闭的postion是------------------------------:" + postion);
                    mListBean.remove(postion);

                    mBettingBodyNum.setText(mListBean.size() + "");

                    ArrayList<Integer> list = new ArrayList<>();
                    for (Map.Entry<Integer, BetBean> bean : mCbBean.entrySet()) {
                        list.add(bean.getKey());
                    }
                    Logger.d(TAG, "关闭的postion是------------------------------:" + postion);
                    Integer key = list.get(postion);
                    final BetBean bet = mCbBean.get(key);
                    mCbBean.remove(key);
                    Logger.d(TAG, "mCbBean返回数据是----------" + bet.toString() + "------------:" + mCbBean.size() + "---" + bet.id + "--" + bet.selected);
                    PopBettingBodyAdapter.this.notifyDataSetChanged();
                    if (mListBean.size() == 0) {
                        isDissmiss(mPopBettingBody);
                        isDissmiss(mBettingHeadPop);
                    }
                    PopBettingBodyAdapter.this.notifyDataSetChanged();

                    hoder.mItemBettingBodyClose.setClickable(false);
                    for (JCbean data : mData) {
                        if (data.id == bet.id) {
                            switch (bet.selected) {
                                case 1:
                                    data.cbTag1 = false;
                                    break;
                                case 3:
                                    data.cbTag2 = false;
                                    break;
                                case 2:
                                    data.cbTag3 = false;
                                    break;
                            }
                            hoder.mItemBettingBodyClose.setClickable(true);
                            mHotAdapterL.notifyDataSetChanged();
                            return;
                        }
                    }
                    Logger.d(TAG, "我被点击了....................close" + mListBean.size());

                }
            });

            hoder.mBettingBodyItemInputGold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * 用于拼接键盘数字
                     */
                    boolean now = bean.cbTag;
                    for (Integer i : mListKeyBoard.keySet()) {
                        Integer id = mListKeyBoard.get(i);
                        mListBean.get(position).cbTag = false;
                        for (BetBean en : mListBean) {
                            if (id == en.id)
                                en.cbTag = false;
                        }
                    }
                    synchronized (this) {
                        notifyDataSetChanged();
                    }
                    Log.d(TAG, "返回数据是------------------------------:" + now);
                    hoder.mKeybordLy.setVisibility(now ? View.GONE : View.VISIBLE);
                    bean.cbTag = !now;
                    if (bean.cbTag) {
                        mListKeyBoard.put(bean.id, bean.id);
                    } else {
                        mListKeyBoard.remove(bean.id);
                    }
                }
            });

            hoder.mKeybord0.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (bean.amount.length() >= 0 && !"请输入金币".equals(bean.amount)) {
                                                           bean.amount = bean.amount + "0";
                                                           hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                           if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                               Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                               String s = String.valueOf(mDf.format(resid));
                                                               bean.returnNum = s;
                                                               hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                           }
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord1.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "1";
                                                       } else {
                                                           bean.amount = bean.amount + "1";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }

            );
            hoder.mKeybord2.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "2";
                                                       } else {
                                                           bean.amount = bean.amount + "2";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }

            );
            hoder.mKeybord3.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "3";
                                                       } else {
                                                           bean.amount = bean.amount + "3";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }

            );
            hoder.mKeybord4.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "4";
                                                       } else {
                                                           bean.amount = bean.amount + "4";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }

            );
            hoder.mKeybord5.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "5";
                                                       } else {
                                                           bean.amount = bean.amount + "5";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }

            );
            hoder.mKeybord6.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "6";
                                                       } else {
                                                           bean.amount = bean.amount + "6";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord7.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "7";
                                                       } else {
                                                           bean.amount = bean.amount + "7";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord8.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "8";
                                                       } else {
                                                           bean.amount = bean.amount + "8";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }

            );
            hoder.mKeybord9.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "9";
                                                       } else {
                                                           bean.amount = bean.amount + "9";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybordThousand.setOnClickListener(new MyItemClickListern(bean) {
                                                          @Override
                                                          public void onClick(View v) {
                                                              if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                  bean.amount = "";
                                                                  hoder.mBettingBodyItemExpectedReturn.setText("");
                                                              } else {
                                                                  bean.amount = bean.amount + "000";
                                                                  hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                  if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                                      Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                      String s = String.valueOf(mDf.format(resid));
                                                                      bean.returnNum = s;
                                                                      hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                  }
                                                              }
                                                          }
                                                      }
            );
            hoder.mKeybordTenThousand.setOnClickListener(new MyItemClickListern(bean) {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                     bean.amount = "";
                                                                     hoder.mBettingBodyItemExpectedReturn.setText("");
                                                                 } else {
                                                                     bean.amount = bean.amount + "0000";
                                                                     hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                     if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                                         Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                         String s = String.valueOf(mDf.format(resid));
                                                                         bean.returnNum = s;
                                                                         hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                     }
                                                                 }
                                                             }
                                                         }
            );
            hoder.mKeybordHundredThousand.setOnClickListener(new MyItemClickListern(bean) {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                         bean.amount = "";
                                                                         hoder.mBettingBodyItemExpectedReturn.setText("");
                                                                     } else {
                                                                         bean.amount = bean.amount + "00000";
                                                                         hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                         if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                                             Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                             String s = String.valueOf(mDf.format(resid));
                                                                             bean.returnNum = s;
                                                                             hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                         }
                                                                     }
                                                                 }
                                                             }
            );
            hoder.mKeybordMillion.setOnClickListener(new MyItemClickListern(bean) {
                                                         @Override
                                                         public void onClick(View v) {
                                                             if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                 bean.amount = "";
                                                                 hoder.mBettingBodyItemExpectedReturn.setText("");
                                                             } else {
                                                                 bean.amount = bean.amount + "000000";
                                                                 hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                 if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                                                                     Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                     String s = String.valueOf(mDf.format(resid));
                                                                     bean.returnNum = s;
                                                                     hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                 }
                                                             }
                                                         }
                                                     }
            );

            hoder.mAll.setOnClickListener(new MyItemClickListern(bean) {
                @Override
                public void onClick(View v) {
                    bean.amount = UIUtils.getSputils().getString(Constent.GLODS, "");
                    if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                        bean.amount = "";
                        hoder.mBettingBodyItemExpectedReturn.setText("");
                    } else {
                        hoder.mBettingBodyItemInputGold.setText(bean.amount);
                        if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.realRate.trim())) {
                            Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                            String s = String.valueOf(mDf.format(resid));
                            bean.returnNum = s;
                            hoder.mBettingBodyItemExpectedReturn.setText(s);
                        }
                    }

                }
            });
            hoder.mKeybordClear.setOnClickListener(new MyItemClickListern(bean) {
                                                       @Override
                                                       public void onClick(View v) {
                                                           bean.amount = "";
                                                           hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                           bean.returnNum = "0";
                                                           hoder.mBettingBodyItemExpectedReturn.setText("0");
                                                       }
                                                   }
            );
            hoder.mKeybordConfirm.setOnClickListener(new MyItemClickListern(bean) {
                                                         @Override
                                                         public void onClick(View v) {
                                                             if (!"请输入金币".equals(bean.amount) && bean.amount.length() > 0) {
                                                                 hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                 hoder.mBettingBodyItemInputGold.setChecked(bean.cbTag);
                                                                 Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                 String s = String.valueOf(mDf.format(resid));
                                                                 hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                 hoder.mBettingBodyItemInputGold.setTextColor(Color.GRAY);
                                                                 hoder.mBettingBodyItemInputGold.setChecked(false);
                                                                 bean.cbTag = false;
                                                                 mListKeyBoard.remove(bean.id);
                                                                 hoder.mKeybordLy.setVisibility(View.GONE);
                                                             } else {
                                                                 TmtUtils.midToast(context, "请输入下注金额", 0);
                                                             }
                                                         }
                                                     }
            );

            return convertView;
        }

        class ViewHolder {
            @Bind(R.id.item_betting_body_pl)
            TextView mItemBettingBodyPL;
            @Bind(R.id.item_betting_body_ly)
            LinearLayout mItemBettingBodyLy;
            @Bind(R.id.item_betting_body_close)
            ImageView mItemBettingBodyClose;
            @Bind(R.id.betting_body_item_type)
            TextView mBettingBodyItemType;
            @Bind(R.id.betting_body_item_result)
            TextView mBettingBodyItemResult;
            @Bind(R.id.betting_body_item_team)
            TextView mBettingBodyItemTeam;
            @Bind(R.id.betting_body_item_input_gold)
            CheckBox mBettingBodyItemInputGold;
            @Bind(R.id.betting_body_item_expected_return)
            TextView mBettingBodyItemExpectedReturn;
            @Bind(R.id.keybord_1)
            Button mKeybord1;
            @Bind(R.id.keybord_2)
            Button mKeybord2;
            @Bind(R.id.keybord_3)
            Button mKeybord3;
            @Bind(R.id.keybord_4)
            Button mKeybord4;
            @Bind(R.id.keybord_5)
            Button mKeybord5;
            @Bind(R.id.keybord_6)
            Button mKeybord6;
            @Bind(R.id.keybord_7)
            Button mKeybord7;
            @Bind(R.id.keybord_8)
            Button mKeybord8;
            @Bind(R.id.keybord_9)
            Button mKeybord9;
            @Bind(R.id.keybord_0)
            Button mKeybord0;
            @Bind(R.id.keybord_thousand)
            Button mKeybordThousand;
            @Bind(R.id.keybord_ten_thousand)
            Button mKeybordTenThousand;
            @Bind(R.id.keybord_hundred_thousand)
            Button mKeybordHundredThousand;
            @Bind(R.id.keybord_million)
            Button mKeybordMillion;
            @Bind(R.id.keybord_all)
            Button mAll;
            @Bind(R.id.keybord_clear)
            Button mKeybordClear;
            @Bind(R.id.keybord_confirm)
            Button mKeybordConfirm;
            @Bind(R.id.keybord_ly)
            LinearLayout mKeybordLy;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public ArrayList<BetBean> getGlod() {
            return mListBean;
        }
    }

    /**
     * 比赛详情比分下注bodyAdapter
     */
    public class PopBettingBodyScoreAdapter extends BaseAdapter {
        private static final String TAG = "PopBettingBodyScoreAdapter";
        private final String homeVsAway;
        private final DecimalFormat mDf;
        private final HashMap<Integer, Integer> mListKeyBoardScore;
        private Context context;

        private ArrayList<JCScorebean.ListEntity.ItemsEntity> data;

        public PopBettingBodyScoreAdapter(Context context, ArrayList<JCScorebean.ListEntity.ItemsEntity> list, String homeVsAway) {
            super();
            this.context = context;
            this.data = list;
            this.homeVsAway = homeVsAway;
            mListKeyBoardScore = new HashMap<>();
            mDf = new DecimalFormat(".00");
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder hoder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.pop_betting_lv_item, null);
                hoder = new ViewHolder(convertView);
                convertView.setTag(hoder);
            } else {
                hoder = (ViewHolder) convertView.getTag();
            }
            /**
             * 还原初始化状态
             */
            Logger.d(TAG, "比分Adapter的返回数据是---------:" + position + "===" + data.size());
            final JCScorebean.ListEntity.ItemsEntity item = data.get(position);
            /**
             *1进球数区间
             2全场比分
             3半全场赛果
             4全场单双
             5半场单双
             */
            Logger.d(TAG, "itemtype返回数据是---------:" + item.type);
            Logger.d(TAG, "itemname返回数据是---------:" + item.name);
            Logger.d(TAG, "item.payRate返回数据是---------:" + item.payRate);
            switch (item.type) {
                case 1:
                    hoder.mBettingBodyItemType.setText("进球数区间");//下注区间
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                    hoder.mBettingBodyItemType.setText("全场比分");//下注区间
                    break;
                case 6:
                    hoder.mBettingBodyItemType.setText("半全场赛果");//下注区间
                    break;
                case 7:
                    hoder.mBettingBodyItemType.setText("全场单双");//下注区间
                    break;
                case 8:
                    hoder.mBettingBodyItemType.setText("半场单双");//下注区间
                    break;
                default:
                    break;
            }
            hoder.mBettingBodyItemResult.setText(item.name);//下注项
            hoder.mBettingBodyItemTeam.setText(homeVsAway);//队伍
            String payRate = "@" + item.payRate;
            hoder.mItemBettingBodyPL.setText(payRate);//赔率

            hoder.mKeybordLy.setVisibility(item.cbTag ? View.VISIBLE : View.GONE);//键盘
            if (item.cbTag) {
                hoder.mBettingBodyItemInputGold.setChecked(true);//输入金额
            } else {
                hoder.mBettingBodyItemInputGold.setChecked(false);//输入金额
            }

            hoder.mBettingBodyItemInputGold.setText(item.amount);//输入金额
            String returnNum = item.returnNum + "";
            hoder.mBettingBodyItemExpectedReturn.setText(returnNum);

            hoder.mItemBettingBodyClose.setOnClickListener(new MyItemSonCloseClickListern(context, position) {
                @Override
                public void onClick(View v) {
                    Logger.d(TAG, "关闭的postion是------------------------------:" + postion);
                    JCScorebean.ListEntity.ItemsEntity en = data.get(position);
                    data.remove(position);
               /* if (data.size() <= 1) {
                    int i = DensityUtil.dip2px(context, 200);
                    hoder.mItemBettingBodyLy.setMinimumHeight(i);
                }*/
                    Logger.d(TAG, "我被点击了....................close" + data.size());
                    mBettingBodyNum.setText(String.valueOf(data.size()));
                    if (data.size() <= 0) {
                        isDissmiss(mBettingHeadPop);
                        isDissmiss(mPopBettingBody);
                    }
                    PopBettingBodyScoreAdapter.this.notifyDataSetChanged();
                    getRefresh(en);
                }
            });
            hoder.mBettingBodyItemInputGold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBettingBodyLv.setSelection(position);
                    // 用于拼接键盘数字
                    boolean now = item.cbTag;
                    if (mListKeyBoardScore.size() > 0)
                        for (Integer i : mListKeyBoardScore.keySet()) {
                            Integer id = mListKeyBoardScore.get(i);
                            for (JCScorebean.ListEntity.ItemsEntity en : data) {
                                if (id == en.id)
                                    en.cbTag = false;
                            }
                        }
                    synchronized (this) {
                        notifyDataSetChanged();
                    }
                    hoder.mKeybordLy.setVisibility(now ? View.GONE : View.VISIBLE);
                    item.cbTag = !now;
                    if (item.cbTag) {
                        mListKeyBoardScore.put(item.id, item.id);
                    } else {
                        mListKeyBoardScore.remove(item.id);
                    }
                }
            });

            hoder.mKeybord0.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (bean.amount.length() >= 0 && !"请输入金币".equals(bean.amount)) {
                                                           bean.amount = bean.amount + "0";
                                                           hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                           if (TextUtils.isEmpty(bean.amount.trim()) || TextUtils.isEmpty(bean.payRate.trim()))
                                                               return;
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;

                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord1.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "1";
                                                       } else {
                                                           bean.amount = bean.amount + "1";
                                                       }
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   }
                                               }
            );
            hoder.mKeybord2.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "2";
                                                       } else {
                                                           bean.amount = bean.amount + "2";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord3.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "3";
                                                       } else {
                                                           bean.amount = bean.amount + "3";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord4.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "4";
                                                       } else {
                                                           bean.amount = bean.amount + "4";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord5.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "5";
                                                       } else {
                                                           bean.amount = bean.amount + "5";
                                                       }

                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord6.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "6";
                                                       } else {
                                                           bean.amount = bean.amount + "6";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord7.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "7";
                                                       } else {
                                                           bean.amount = bean.amount + "7";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord8.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "8";
                                                       } else {
                                                           bean.amount = bean.amount + "8";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybord9.setOnClickListener(new MyItemScoreClickListern(item) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if ("请输入金币".equals(bean.amount)) {
                                                           bean.amount = "9";
                                                       } else {
                                                           bean.amount = bean.amount + "9";
                                                       }
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                       if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                           Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                           String s = String.valueOf(mDf.format(resid));
                                                           bean.returnNum = s;
                                                           hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                       }
                                                   }
                                               }
            );
            hoder.mKeybordThousand.setOnClickListener(new MyItemScoreClickListern(item) {
                                                          @Override
                                                          public void onClick(View v) {
                                                              if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                  bean.amount = "";
                                                                  hoder.mBettingBodyItemExpectedReturn.setText("");
                                                              } else {
                                                                  bean.amount = bean.amount + "000";
                                                                  hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                                  if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                                      Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                                      String s = String.valueOf(mDf.format(resid));
                                                                      bean.returnNum = s;
                                                                      hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                  }
                                                              }
                                                          }
                                                      }
            );
            hoder.mKeybordTenThousand.setOnClickListener(new MyItemScoreClickListern(item) {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                     bean.amount = "";
                                                                     hoder.mBettingBodyItemExpectedReturn.setText("");
                                                                 } else {
                                                                     bean.amount = bean.amount + "0000";
                                                                     hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                                     if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                                         Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                                         String s = String.valueOf(mDf.format(resid));
                                                                         bean.returnNum = s;
                                                                         hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                     }
                                                                 }
                                                             }
                                                         }
            );
            hoder.mKeybordHundredThousand.setOnClickListener(new MyItemScoreClickListern(item) {
                                                                 @Override
                                                                 public void onClick(View v) {
                                                                     if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                         bean.amount = "";
                                                                         hoder.mBettingBodyItemExpectedReturn.setText("");
                                                                     } else {
                                                                         bean.amount = bean.amount + "00000";
                                                                         hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                                         if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                                             Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                                             String s = String.valueOf(mDf.format(resid));
                                                                             bean.returnNum = s;
                                                                             hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                         }
                                                                     }
                                                                 }
                                                             }
            );
            hoder.mKeybordMillion.setOnClickListener(new MyItemScoreClickListern(item) {
                                                         @Override
                                                         public void onClick(View v) {
                                                             if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                 bean.amount = "";
                                                                 hoder.mBettingBodyItemExpectedReturn.setText("");
                                                             } else {
                                                                 bean.amount = bean.amount + "000000";
                                                                 hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                                 if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                                     Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                                     String s = String.valueOf(mDf.format(resid));
                                                                     bean.returnNum = s;
                                                                     hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                 }
                                                             }
                                                         }
                                                     }
            );
            hoder.mAll.setOnClickListener(new MyItemScoreClickListern(item) {
                                              @Override
                                              public void onClick(View v) {
                                                  bean.amount = UIUtils.getSputils().getString(Constent.GLODS, "");
                                                  if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                      bean.amount = "";
                                                      hoder.mBettingBodyItemExpectedReturn.setText("");
                                                  } else {
                                                      hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                      hoder.mBettingBodyItemInputGold.setText(bean.amount);

                                                      if (!TextUtils.isEmpty(bean.amount.trim()) && !TextUtils.isEmpty(bean.payRate.trim())) {
                                                          Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                          String s = String.valueOf(mDf.format(resid));
                                                          bean.returnNum = s;
                                                          hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                      }
                                                  }

                                              }
                                          }
            );
            hoder.mKeybordClear.setOnClickListener(new MyItemScoreClickListern(item) {
                                                       @Override
                                                       public void onClick(View v) {
                                                           bean.amount = "";
                                                           hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                           bean.returnNum = "0";
                                                           hoder.mBettingBodyItemExpectedReturn.setText("0");
                                                       }
                                                   }
            );
            hoder.mKeybordConfirm.setOnClickListener(new MyItemScoreClickListern(item) {
                                                         @Override
                                                         public void onClick(View v) {
                                                             if (!"请输入金币".equals(bean.amount) && bean.amount.length() > 0) {
                                                                 hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                 hoder.mBettingBodyItemInputGold.setChecked(bean.cbTag);
                                                                 if (TextUtils.isEmpty(bean.amount.trim()) || TextUtils.isEmpty(bean.payRate.trim()))
                                                                     return;
                                                                 Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.payRate.trim());
                                                                 String s = String.valueOf(mDf.format(resid));
                                                                 hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                 hoder.mBettingBodyItemInputGold.setTextColor(Color.GRAY);
                                                                 hoder.mBettingBodyItemInputGold.setChecked(false);
                                                                 bean.cbTag = false;
                                                                 mListKeyBoardScore.remove(bean.id);
                                                                 hoder.mKeybordLy.setVisibility(View.GONE);
                                                             } else {
                                                                 TmtUtils.midToast(context, "请输入下注金额", 0);
                                                             }
                                                         }
                                                     }
            );
            return convertView;
        }

        public ArrayList<JCScorebean.ListEntity.ItemsEntity> getScoreList() {
            return data;
        }

        class ViewHolder {
            @Bind(R.id.item_betting_body_pl)
            TextView mItemBettingBodyPL;
            @Bind(R.id.item_betting_body_ly)
            LinearLayout mItemBettingBodyLy;
            @Bind(R.id.item_betting_body_close)
            ImageView mItemBettingBodyClose;
            @Bind(R.id.betting_body_item_type)
            TextView mBettingBodyItemType;
            @Bind(R.id.betting_body_item_result)
            TextView mBettingBodyItemResult;
            @Bind(R.id.betting_body_item_team)
            TextView mBettingBodyItemTeam;
            @Bind(R.id.betting_body_item_input_gold)
            CheckBox mBettingBodyItemInputGold;
            @Bind(R.id.betting_body_item_expected_return)
            TextView mBettingBodyItemExpectedReturn;
            @Bind(R.id.keybord_1)
            Button mKeybord1;
            @Bind(R.id.keybord_2)
            Button mKeybord2;
            @Bind(R.id.keybord_3)
            Button mKeybord3;
            @Bind(R.id.keybord_4)
            Button mKeybord4;
            @Bind(R.id.keybord_5)
            Button mKeybord5;
            @Bind(R.id.keybord_6)
            Button mKeybord6;
            @Bind(R.id.keybord_7)
            Button mKeybord7;
            @Bind(R.id.keybord_8)
            Button mKeybord8;
            @Bind(R.id.keybord_9)
            Button mKeybord9;
            @Bind(R.id.keybord_0)
            Button mKeybord0;
            @Bind(R.id.keybord_thousand)
            Button mKeybordThousand;
            @Bind(R.id.keybord_ten_thousand)
            Button mKeybordTenThousand;
            @Bind(R.id.keybord_hundred_thousand)
            Button mKeybordHundredThousand;
            @Bind(R.id.keybord_million)
            Button mKeybordMillion;
            @Bind(R.id.keybord_all)
            Button mAll;
            @Bind(R.id.keybord_clear)
            Button mKeybordClear;
            @Bind(R.id.keybord_confirm)
            Button mKeybordConfirm;
            @Bind(R.id.keybord_ly)
            LinearLayout mKeybordLy;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private void getRefresh(JCScorebean.ListEntity.ItemsEntity en) {
        Logger.d(TAG, "比赛类型改变的是------------------------------:" + en.type);
        switch (en.type) {
            case 1:
                for (Integer i : mMapChecksInterVal.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksInterVal.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterInterVal != null)
                    mAdapterInterVal.notifyDataSetChanged();
                break;
            case 2:
                for (Integer i : mMapChecksWin.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksWin.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterWin != null)
                    mAdapterWin.notifyDataSetChanged();
                break;
            case 3:
                for (Integer i : mMapChecksDraw.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksDraw.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterDraw != null)
                    mAdapterDraw.notifyDataSetChanged();
                break;
            case 4:
                for (Integer i : mMapChecksLost.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksLost.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterLost != null)
                    mAdapterLost.notifyDataSetChanged();
                break;
            case 5:
                for (Integer i : mMapChecksOther.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksOther.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterOther != null)
                    mAdapterOther.notifyDataSetChanged();
                break;
            case 6:
                for (Integer i : mMapChecksHalfAllResult.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksHalfAllResult.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterhalfAllResult != null)
                    mAdapterhalfAllResult.notifyDataSetChanged();
                break;
            case 7:
                for (Integer i : mMapChecksAllSingleDouble.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksAllSingleDouble.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterAllSingeDouble != null)
                    mAdapterAllSingeDouble.notifyDataSetChanged();
                break;
            case 8:
                for (Integer i : mMapChecksHalfSingleDouble.keySet()) {
                    JCScorebean.ListEntity.ItemsEntity entity = mMapChecksHalfSingleDouble.get(i);
                    if (en.id == entity.id)
                        en.checked = false;
                }
                if (mAdapterHalfSingeDouble != null)
                    mAdapterHalfSingeDouble.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    /**
     * 判斷
     */
    public void isNullClear(Object l) {
        if (l != null)
            if (l instanceof List) {
                ((List) l).clear();
            } else if (l instanceof Map) {
                ((Map) l).clear();
            }
    }

    public void isDissmiss(PopupWindow p) {
        if (p != null)
            p.dismiss();
    }
}
