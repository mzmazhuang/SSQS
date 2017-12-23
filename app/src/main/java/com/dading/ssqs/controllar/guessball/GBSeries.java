package com.dading.ssqs.controllar.guessball;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.BettingRecordActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.adapter.GuessBallChoiceGvAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.PayBallDoubleElement;
import com.dading.ssqs.base.BaseGuessball;
import com.dading.ssqs.bean.BetBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GBSeriesBean;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.interfaces.MyItemClickListern;
import com.dading.ssqs.interfaces.MyItemSonCloseClickListern;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshExpandableListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/7 11:40
 * 描述	      猜球 串关
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBSeries extends BaseGuessball {

    private static final String TAG = "GBSeries";

    LinearLayout mSeriesSx;
    TextView mSeriesPlayHelp;
    TextView mSeriesBettingRecord;
    PullToRefreshExpandableListView mExpandList;

    private DecimalFormat mDf;
    private ExpandableListView mExpandableListView;
    private LinearLayout mBettingLy;
    private TextView mBettingNum;
    private View mPopBodyView;
    private ImageView mBettingBodyClose;
    private TextView mBettingBodyLoading;
    private TextView mBettingBodyDel;
    private TextView mBettingBodyNum;
    private ListView mBettingBodyLv;
    private TextView mBettingBodyUpload;
    private TextView mBettingBodyGoldBalance;
    private PopupWindow mPopBettingBody;
    private View mView;

    private PopBettingCgBodyAdapter mAdapter;
    private boolean mIsLoading;
    private TextView mMode;
    private CheckBox mInputNum;
    private TextView mSerisePL;
    private TextView mSeriseReturn;
    private LinearLayout mKeyBordLy;
    private TextView mKey0;
    private TextView mKey1;
    private TextView mKey2;
    private TextView mKey3;
    private TextView mKey4;
    private TextView mKey5;
    private TextView mKey6;
    private TextView mKey7;
    private TextView mKey8;
    private TextView mKey9;
    private double mPL = 1;
    private TextView mKeyThousand;
    private TextView mTenThousand;
    private TextView mHundredThousand;
    private TextView mMillion;
    private TextView mClear;
    private TextView mConfirm;
    private BetBean mCbBean;
    private List<GBSeriesBean> mData;
    private GBSeriesAdapter mExpandAdapter;
    private ArrayList<BetBean> mFrilstList;
    private ArrayList<BetBean> mSecondList = new ArrayList<>();
    private View mChioceView;
    private GridView mChioceSetting;
    public PopupWindow mPopChioce;
    public PopupWindow mPopupWindow1;
    private LinearLayout mChioceLy;
    private RadioGroup mChioceGp;
    private RadioButton mChioceSettingRb1;
    private TextView mChioceConfirm;
    private RadioButton mChioceSettingRb2;
    private ArrayList<GusessChoiceBean.FilterEntity> mList;
    private GuessBallChoiceGvAdapter mAdapterSx;
    private View mPopView;
    private LinearLayout mLyOut;
    private ImageView mIvClose;
    public MyBroadcastReciver mReceiver;
    private View mViewFoot;
    private LinearLayout.LayoutParams mParamsW;
    private TextView mAll;
    private HashMap<Integer, GBSeriesBean.PayRateEntity> mMap;

    @Override
    protected View initMidContentView(Context content) {
        mDf = new DecimalFormat(".00");

        mView = View.inflate(mContent, R.layout.gb_series, null);
        mExpandList = (PullToRefreshExpandableListView) mView.findViewById(R.id.guessball_series_exp_info);
        mSeriesSx = (LinearLayout) mView.findViewById(R.id.by_league_gbserise);
        mSeriesPlayHelp = (TextView) mView.findViewById(R.id.headview_series_playhelp);
        mSeriesBettingRecord = (TextView) mView.findViewById(R.id.headview_series_bettingrecord);

        //投注单
        mBettingLy = (LinearLayout) mView.findViewById(R.id.betting_list_ly_cg);
        mBettingNum = (TextView) mView.findViewById(R.id.betting_num_cg);

        mExpandList.getRefreshableView().setGroupIndicator(null);//去掉一级菜单头
        mExpandableListView = mExpandList.getRefreshableView();
        mExpandableListView.setEmptyView(mEmpty);
        mExpandableListView.setDivider(null);

        mExpandAdapter = new GBSeriesAdapter(mContent);
        mExpandableListView.setAdapter(mExpandAdapter);

        //帮助pop
        mPopView = View.inflate(mContent, R.layout.jc_play_help_popu, null);
        mLyOut = (LinearLayout) mPopView.findViewById(R.id.jc_play_help_pop_out);
        mIvClose = (ImageView) mPopView.findViewById(R.id.jc_play_help_pop_close);


        //投注界面
        mPopBodyView = View.inflate(mContent, R.layout.betting_pop_body, null);
        mBettingBodyClose = (ImageView) mPopBodyView.findViewById(R.id.betting_body_close);
        mBettingBodyLoading = (TextView) mPopBodyView.findViewById(R.id.betting_body_loadingornum);
        mBettingBodyDel = (TextView) mPopBodyView.findViewById(R.id.betting_body_delete_all);
        mBettingBodyNum = (TextView) mPopBodyView.findViewById(R.id.betting_body_num);//个数
        mBettingBodyLv = (ListView) mPopBodyView.findViewById(R.id.betting_body_lv);
        mBettingBodyUpload = (TextView) mPopBodyView.findViewById(R.id.betting_body_upload);
        mBettingBodyGoldBalance = (TextView) mPopBodyView.findViewById(R.id.betting_body_gold_balance);

        mPopBettingBody = PopUtil.popuMakeMwf(mPopBodyView);

        mViewFoot = View.inflate(mContent, R.layout.gb_series_foot, null);
        mMode = (TextView) mViewFoot.findViewById(R.id.betting_body_item_serise_mode);//8串1
        mInputNum = (CheckBox) mViewFoot.findViewById(R.id.betting_body_item_serise_input_gold);
        mSerisePL = (TextView) mViewFoot.findViewById(R.id.betting_body_item_serise_pl);//赔率
        mSeriseReturn = (TextView) mViewFoot.findViewById(R.id.betting_body_item_serise_return);//返回金币


        mKeyBordLy = (LinearLayout) mViewFoot.findViewById(R.id.keybord_ly);
        mKey0 = (TextView) mViewFoot.findViewById(R.id.keybord_0);
        mKey1 = (TextView) mViewFoot.findViewById(R.id.keybord_1);
        mKey2 = (TextView) mViewFoot.findViewById(R.id.keybord_2);
        mKey3 = (TextView) mViewFoot.findViewById(R.id.keybord_3);
        mKey4 = (TextView) mViewFoot.findViewById(R.id.keybord_4);
        mKey5 = (TextView) mViewFoot.findViewById(R.id.keybord_5);
        mKey6 = (TextView) mViewFoot.findViewById(R.id.keybord_6);
        mKey7 = (TextView) mViewFoot.findViewById(R.id.keybord_7);
        mKey8 = (TextView) mViewFoot.findViewById(R.id.keybord_8);
        mKey9 = (TextView) mViewFoot.findViewById(R.id.keybord_9);
        mKeyThousand = (TextView) mViewFoot.findViewById(R.id.keybord_thousand);
        mTenThousand = (TextView) mViewFoot.findViewById(R.id.keybord_ten_thousand);
        mHundredThousand = (TextView) mViewFoot.findViewById(R.id.keybord_hundred_thousand);
        mMillion = (TextView) mViewFoot.findViewById(R.id.keybord_million);
        mAll = (TextView) mViewFoot.findViewById(R.id.keybord_all);
        mClear = (TextView) mViewFoot.findViewById(R.id.keybord_clear);
        mConfirm = (TextView) mViewFoot.findViewById(R.id.keybord_confirm);

        mBettingBodyLv.addFooterView(mViewFoot);

        mChioceView = View.inflate(mContent, R.layout.pop_chioce_view, null);
        mChioceSetting = ButterKnife.findById(mChioceView, R.id.guessball_chioce_setting);
        mChioceGp = ButterKnife.findById(mChioceView, R.id.guessball_chioce_gp);
        mChioceSettingRb1 = ButterKnife.findById(mChioceView, R.id.guessball_chioce_rb1);
        mChioceSettingRb2 = ButterKnife.findById(mChioceView, R.id.guessball_chioce_rb2);
        mChioceLy = ButterKnife.findById(mChioceView, R.id.guessball_chioce_ly);
        mChioceConfirm = ButterKnife.findById(mChioceView, R.id.guessball_chioce_confirm);

        mPopChioce = PopUtil.popuMake(mChioceView);
        mPopupWindow1 = PopUtil.popuMake(mPopView);

        mAdapterSx = new GuessBallChoiceGvAdapter(mContent);
        mChioceSetting.setAdapter(mAdapterSx);

        return mView;
    }

    @Override
    public void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mReceiver);
    }

    @Override
    public void initData() {
        super.initData();

        mMap = new HashMap<>();

        mReceiver = new MyBroadcastReciver();
        UIUtils.ReRecevice(mReceiver, Constent.POP_CLOSE);
        UIUtils.ReRecevice(mReceiver, Constent.LOADING_FOOTBALL);
        UIUtils.ReRecevice(mReceiver, Constent.CG_RECEVICE);

        mExpandList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        mList = new ArrayList<>();
        String s = UIUtils.getSputils().getString(Constent.GLODS, null);
        LogUtil.util(TAG, "拥有金币------------------------------:" + s);
        mChioceSettingRb1.setChecked(true);

        getData();
    }

    private void getNetDataWork() {
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        SSQSApplication.apiClient(0).getMatchGuessCg(b, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mLoadingAnimal.setVisibility(View.GONE);
                mDrawable.stop();

                if (result.isOk()) {

                    List<GBSeriesBean> items = (List<GBSeriesBean>) result.getData();

                    if (items != null) {
                        processData(items);
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "失败信息");
                }
            }
        });
    }

    private void getData() {
        mLoadingAnimal.setVisibility(View.VISIBLE);
        mDrawable.start();

        getNetDataWork();
    }

    private void processDataChoice(List<GusessChoiceBean> data) {
        mList.clear();
        for (int i = 0; i < data.size(); i++) {
            GusessChoiceBean entity = data.get(i);
            for (int j = 0; j < entity.filter.size(); j++)
                mList.add(entity.filter.get(j));
        }
        mChioceSettingRb2.setChecked(true);

        mAdapterSx.setList(mList);

        mPopChioce.showAsDropDown(mSeriesSx, 0, 0);
    }

    @Override
    protected void initListener() {
        mPopBettingBody.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mBettingLy.setClickable(true);
            }
        });
        mPopupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSeriesPlayHelp.setClickable(true);
            }
        });
        mPopChioce.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mSeriesSx.setClickable(true);
            }
        });
        mBettingBodyLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("登录".equals(mBettingBodyLoading.getText())) {
                    mPopBettingBody.dismiss();
                    Intent intent = new Intent(mContent, LoginActivity.class);
                    mContent.startActivity(intent);
                }
            }
        });
        //help
        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow1.dismiss();
            }
        });
        mLyOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow1.dismiss();
            }
        });
        mSeriesBettingRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (mIsLoading) {
                    Intent intent = new Intent(mContent, BettingRecordActivity.class);
                    mContent.startActivity(intent);
                } else {
                    mPopBettingBody.dismiss();
                    Intent intent = new Intent(mContent, LoginActivity.class);
                    mContent.startActivity(intent);
                }
            }
        });
        mSeriesPlayHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow1.showAtLocation(mView, Gravity.CENTER, 0, 0);
                mSeriesPlayHelp.setClickable(false);
            }
        });
        mSeriesSx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * a)	请求地址：
                 /v1.0/match/filter
                 b)	请求方式:get
                 c)	请求参数说明：
                 auth_token：登陆后加入请求头
                 篮球  "/v1.0/match/ball/filter"
                 */
                boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                if (b) {
                    SSQSApplication.apiClient(0).getMatchFilter(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                List<GusessChoiceBean> bean = (List<GusessChoiceBean>) result.getData();

                                if (bean != null) {
                                    processDataChoice(bean);
                                }
                            } else {
                                LogUtil.util(TAG, result.getMessage() + "竞猜筛选失败信息");
                            }
                        }
                    });
                } else {
                    SSQSApplication.apiClient(0).getMatchBallFilterList(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                List<GusessChoiceBean> bean = (List<GusessChoiceBean>) result.getData();

                                if (bean != null) {
                                    processDataChoice(bean);
                                }
                            } else {
                                LogUtil.util(TAG, result.getMessage() + "竞猜筛选失败信息");
                            }
                        }
                    });
                }
                mSeriesSx.setClickable(false);
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
                if (mAdapterSx != null)
                    mAdapterSx.notifyDataSetChanged();
            }
        });
        mChioceGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.guessball_chioce_rb1:
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).checked = true;
                        }
                        if (mAdapterSx != null)
                            mAdapterSx.notifyDataSetChanged();
                        break;
                    case R.id.guessball_chioce_rb2:
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).checked = false;
                        }
                        if (mAdapterSx != null)
                            mAdapterSx.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
        mChioceConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.util(TAG, "点击提交------------------------------:");
                mLoadingAnimal.setVisibility(View.VISIBLE);
                mDrawable.start();
                StringBuilder sb = new StringBuilder();
                ArrayList<GusessChoiceBean.FilterEntity> listAdd = new ArrayList<>();
                if (mAdapterSx != null) {
                    List<GusessChoiceBean.FilterEntity> list = mAdapterSx.getList();
                    for (int i = 0; i < list.size(); i++) {
                        GusessChoiceBean.FilterEntity entity = list.get(i);
                        if (entity.checked) {
                            listAdd.add(entity);
                        }
                    }
                    for (int i = 0; i < listAdd.size(); i++) {
                        GusessChoiceBean.FilterEntity ab = listAdd.get(i);
                        if (i == listAdd.size() - 1) {
                            sb.append(ab.id);
                        } else {
                            sb.append(ab.id).append(",");
                        }
                    }
                }
                LogUtil.util(TAG, "上传的数据是------------------------------:" + sb.toString());
                /**
                 a)	请求地址：
                 /v1.0/match/guess/cg/leagueIDs/{leagueIDs}
                 b)	请求方式:get
                 c)	请求参数说明：auth_token：登陆后加入请求头
                 leagueIDs:联赛ids，以逗号隔开
                 篮球  /v1.0/match/guess/ball/leagueIDs/{ leagueIDs}
                 */
                boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                SSQSApplication.apiClient(0).getMatchGuessByIds(b, sb.toString(), new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mLoadingAnimal.setVisibility(View.GONE);
                        mDrawable.stop();

                        if (result.isOk()) {
                            List<GBSeriesBean> items = (List<GBSeriesBean>) result.getData();

                            if (items != null) {
                                mData = items;

                                mExpandAdapter.setList(items);
                            }
                        } else {
                            LogUtil.util(TAG, result.getMessage() + "筛选上传失败信息");
                        }
                    }
                });
                mPopChioce.dismiss();
            }
        });
        mChioceLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopChioce.dismiss();
                mChioceSettingRb2.setChecked(true);
            }
        });
        mInputNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示键盘
                    LogUtil.util(TAG, "点击键盘------------------------------:");
                    mKeyBordLy.setVisibility(View.VISIBLE);
                } else {
                    //隐藏键盘
                    mKeyBordLy.setVisibility(View.GONE);
                }
            }
        });
        mKey0.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:0");
                                         if (mCbBean.amount.length() != 0 && !"000".equals(mCbBean.amount)) {
                                             mCbBean.amount = mCbBean.amount + "0";
                                             setCB();
                                         }
                                     }
                                 }
        );
        mKey1.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:1");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "1";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "1";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey2.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:2");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "2";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "2";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey3.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------3");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "3";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "3";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey4.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:4");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "4";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "4";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey5.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:5");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "5";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "5";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey6.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:6");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "6";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "6";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey7.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:7");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "7";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "7";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey8.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:8");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "8";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "8";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKey9.setOnClickListener(new MyItemClickListern(mCbBean) {
                                     @Override
                                     public void onClick(View v) {
                                         LogUtil.util(TAG, "点击键盘------------------------------:9");
                                         if ("000".equals(mCbBean.amount)) {
                                             mCbBean.amount = "9";
                                         } else {
                                             mCbBean.amount = mCbBean.amount + "9";
                                         }
                                         setCB();
                                     }
                                 }
        );
        mKeyThousand.setOnClickListener(new MyItemClickListern(mCbBean) {
                                            @Override
                                            public void onClick(View v) {
                                                LogUtil.util(TAG, "点击键盘------------------------------:000");
                                                if ("000".equals(mCbBean.amount) || mCbBean.amount.length() == 0) {
                                                    mCbBean.amount = "";
                                                    mSeriseReturn.setText("");
                                                } else {
                                                    mCbBean.amount = mCbBean.amount + "000";
                                                    setCB();
                                                }
                                            }
                                        }
        );
        mTenThousand.setOnClickListener(new MyItemClickListern(mCbBean) {
                                            @Override
                                            public void onClick(View v) {
                                                LogUtil.util(TAG, "点击键盘------------------------------:0000");
                                                if ("000".equals(mCbBean.amount) || mCbBean.amount.length() == 0) {
                                                    mCbBean.amount = "";
                                                    mSeriseReturn.setText("");
                                                } else {
                                                    mCbBean.amount = mCbBean.amount + "0000";
                                                    setCB();
                                                }
                                            }
                                        }
        );
        mHundredThousand.setOnClickListener(new MyItemClickListern(mCbBean) {
                                                @Override
                                                public void onClick(View v) {
                                                    LogUtil.util(TAG, "点击键盘------------------------------:00000");
                                                    if ("000".equals(mCbBean.amount) || mCbBean.amount.length() == 0) {
                                                        mCbBean.amount = "";
                                                        mSeriseReturn.setText("");
                                                    } else {
                                                        mCbBean.amount = mCbBean.amount + "00000";
                                                        setCB();
                                                    }
                                                }
                                            }
        );
        mMillion.setOnClickListener(new MyItemClickListern(mCbBean) {
                                        @Override
                                        public void onClick(View v) {
                                            LogUtil.util(TAG, "点击键盘------------------------------000000:");
                                            if ("000".equals(mCbBean.amount) || mCbBean.amount.length() == 0) {
                                                mCbBean.amount = "";
                                                mSeriseReturn.setText("");
                                            } else {
                                                mCbBean.amount = mCbBean.amount + "000000";
                                                setCB();
                                            }
                                        }
                                    }
        );
        mAll.setOnClickListener(new MyItemClickListern(mCbBean) {
                                    @Override
                                    public void onClick(View v) {
                                        LogUtil.util(TAG, "点击键盘------------------------------all:");
                                        mCbBean.amount = UIUtils.getSputils().getString(Constent.GLODS, "");
                                        if ("000".equals(mCbBean.amount) || mCbBean.amount.length() == 0) {
                                            mCbBean.amount = "";
                                            mSeriseReturn.setText("");
                                        } else {
                                            setCB();
                                        }
                                    }
                                }
        );
        mClear.setOnClickListener(new MyItemClickListern(mCbBean) {
                                      @Override
                                      public void onClick(View v) {
                                          LogUtil.util(TAG, "点击键盘------------------------------:clear");
                                          mCbBean.amount = "";
                                          mInputNum.setText(mCbBean.amount);
                                          mCbBean.returnNum = "0";
                                          mSeriseReturn.setText("0");
                                      }
                                  }
        );
        mConfirm.setOnClickListener(new MyItemClickListern(mCbBean) {
            @Override
            public void onClick(View v) {
                LogUtil.util(TAG, "点击键盘------------------------------:确认");
                if (!"000".equals(mCbBean.amount) && mCbBean.amount.length() > 0) {
                    mInputNum.setText(mCbBean.amount);
                    mInputNum.setChecked(mCbBean.cbTag);
                    Double resid = Double.parseDouble(mCbBean.amount.trim()) * mPL;
                    String s = String.valueOf(mDf.format(resid));
                    mSeriseReturn.setText(s);
                    mCbBean.returnNum = s;
                    mInputNum.setTextColor(Color.GRAY);
                    mInputNum.setChecked(false);
                    mCbBean.cbTag = false;
                    mKeyBordLy.setVisibility(View.GONE);
                } else {
                    TmtUtils.midToast(mContent, "请输入金币数", 0);
                }
            }
        });

        mExpandList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                //下拉时执行的操作

                boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                SSQSApplication.apiClient(0).getMatchGuessCg(b, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mExpandList.onRefreshComplete();

                        if (result.isOk()) {

                            List<GBSeriesBean> items = (List<GBSeriesBean>) result.getData();

                            if (items != null) {
                                processData(items);
                            }
                        } else {
                            TmtUtils.midToast(mContent, result.getMessage(), 0);
                        }
                    }
                });
            }
        });


        //投注单
        mBettingLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.util(TAG, "串关ly被点击了------------------------------:");
                mFrilstList = new ArrayList<>();
                Iterator<Map.Entry<Integer, GBSeriesBean.PayRateEntity>> iterator = mMap.entrySet().iterator();
                LogUtil.util(TAG, "再次添加mmap返回数据是------------------------------:" + mMap.size());
                while (iterator.hasNext()) {
                    Map.Entry<Integer, GBSeriesBean.PayRateEntity> next = iterator.next();
                    GBSeriesBean.PayRateEntity payRate = next.getValue();
                    BetBean bean = getBetBeanBase(payRate);
                    if (payRate.cbTag1) {
                        bean.realRate = payRate.realRate1;
                        bean.selected = 1;
                    } else if (payRate.cbTag2) {
                        bean.realRate = payRate.realRate2;
                        bean.selected = 3;
                    } else if (payRate.cbTag3) {
                        bean.selected = 2;
                        bean.realRate = payRate.realRate3;
                    }
                    mFrilstList.add(bean);
                }

                String text = mMap.size() + "";
                mBettingBodyNum.setText(text);
                setSeriseNum();
                mPL = 1;
                for (BetBean bean : mFrilstList) {
                    if (bean.realRate == null) {
                        LogUtil.util(TAG, "比利时空赛事数据是------------------------------:" + bean);
                        return;
                    }
                    mPL = Double.parseDouble(bean.realRate) * mPL;
                }

                mAdapter = new PopBettingCgBodyAdapter(mContent, mFrilstList);

                mCbBean = new BetBean();
                mCbBean.cbTag = false;
                mCbBean.amount = "";

                mSerisePL.setText(mDf.format(mPL));
                mSeriseReturn.setText("0");
                mInputNum.setText("请输入金币数");

                LogUtil.util(TAG, "传递过去下注数据还有--------:" + mMap.size());
                mBettingBodyLv.setAdapter(mAdapter);

                mBettingBodyLv.setBackgroundResource(R.color.white);
                int height = ListScrollUtil.getLVheight(mBettingBodyLv) + 100;
                int i = DensityUtil.dip2px(mContent, 350);
                if (height > i) {
                    mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, i);
                } else {
                    mParamsW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
                }
                mBettingBodyLv.setLayoutParams(mParamsW);

                mPopBettingBody.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                mBettingLy.setClickable(false);
                mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (mIsLoading) {
                    String gold = UIUtils.getSputils().getString(Constent.GLODS, null);
                    UIUtils.getSputils().putString(Constent.GLODS, gold);
                    LogUtil.util(TAG, "下朱丹被点击了,数据是------------------------------:金币" + UIUtils.getSputils().getString(Constent.GLODS, null) + "钻石" + UIUtils.getSputils().getString(Constent.DIAMONDS, null));
                    mBettingBodyLoading.setText(UIUtils.getSputils().getString(Constent.GLODS, null));
                    mBettingBodyGoldBalance.setVisibility(View.VISIBLE);
                } else {
                    mBettingBodyGoldBalance.setVisibility(View.GONE);
                    mBettingBodyLoading.setText("登录");
                }
                Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.out_from_down);
                animation.setFillAfter(false);
                mBettingLy.startAnimation(animation);
            }
        });
        //投注界面
        mBettingBodyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.util(TAG, "您点击了pop关闭按钮------------------------------:");
                mPL = 1;
                mPopBettingBody.dismiss();
                mBettingLy.setFocusable(false);
            }
        });
        mBettingBodyDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBettingBodyUpload.getText().equals("确认")) {
                    TmtUtils.midToast(mContent, "已下注,无法从服务器撤回,请耐心等待结果.", 0);
                    return;
                }
                getNetDataWork();
                mPL = 1;
                mFrilstList.clear();
                mPopBettingBody.dismiss();
                mMap.clear();
                String text = mMap.size() + "";
                mBettingBodyNum.setText(text);
                Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.out_from_down);
                animation.setFillAfter(false);
                mBettingLy.startAnimation(animation);
                mBettingLy.setVisibility(View.GONE);
            }
        });
        mBettingBodyUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    //传递数据,得到金币值
                    LogUtil.util(TAG, "已登录------------------------------执行上传");
                    String gold = UIUtils.getSputils().getString(Constent.GLODS, null);
                    if (gold == null && mCbBean.amount == null) {
                        TmtUtils.midToast(mContent, "参数有误,请重新下注", 0);
                        mSecondList.clear();
                        mMap.clear();
                        mPopBettingBody.dismiss();
                        Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.out_from_down);
                        animation.setFillAfter(false);
                        mBettingBodyLv.setClickable(true);
                        mBettingLy.startAnimation(animation);
                        mBettingLy.setVisibility(View.GONE);
                        return;
                    }
                    if (TextUtils.isEmpty(mCbBean.amount)) {
                        TmtUtils.midToast(mContent, "請輸入下注金額 !", 0);
                        return;
                    }
                    if (Double.parseDouble(gold) < Double.parseDouble(mCbBean.amount)) {
                        initDialog();
                        return;
                    }
                    int minGlod = 0;
                    if (UIUtils.getSputils().getBoolean(Constent.YP_IS_VISIABLE, false)) {
                        minGlod = 10;
                    } else {
                        minGlod = 100;
                    }

                    if (Double.parseDouble(mCbBean.amount) < minGlod) {
                        TmtUtils.midToast(mContent, "最少下注金币不少于" + minGlod + "..", 0);
                        return;
                    } else if (5000000 < Double.parseDouble(mCbBean.amount)) {
                        TmtUtils.midToast(mContent, "最少下注金币不得大于五百万..", 0);
                        return;
                    }
                    if (100000000 <= Double.parseDouble(mCbBean.returnNum)) {
                        TmtUtils.midToast(mContent, "下注金币的奖金上限不得大于一亿,请修改下注金额..", 0);
                        return;
                    }
                    PayBallDoubleElement element = new PayBallDoubleElement();

                    if (mSecondList.size() == 0) {
                        //mFrilstList.add(mCbBean);
                        for (BetBean bean : mFrilstList) {
                            bean.amount = mCbBean.amount;
                            bean.type = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? 1 : 2;
                        }
                        element.setmFrilstList(mFrilstList);
                        mFrilstList.clear();
                    } else {
                        for (BetBean bean : mSecondList) {
                            bean.amount = mCbBean.amount;
                            bean.type = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? 1 : 2;
                        }
                        element.setmSecondList(mSecondList);
                        mSecondList.clear();
                    }

                    /**
                     * okHttp post同步请求表单提交
                     * @param actionUrl 接口地址
                     * @param paramsMap 请求参数
                     */


                    SSQSApplication.apiClient(0).payBallDouble(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            mBettingBodyUpload.setClickable(true);
                            mBettingBodyLv.setClickable(true);

                            getNetDataWork();

                            if (result.isOk()) {

                                //剩余金币
                                double v1 = Double.parseDouble(UIUtils.getSputils().getString(Constent.GLODS, null)) - Double.parseDouble(mCbBean.amount);
                                String s1 = mDf.format(v1);
                                //开奖还不知道
                                LogUtil.util(TAG, "下注后剩余金额------------------------------:" + s1);
                                UIUtils.getSputils().putString(Constent.GLODS, s1);
                                mBettingBodyLoading.setText(s1);
                                mBettingBodyGoldBalance.setVisibility(View.VISIBLE);
                                TmtUtils.midToast(mContent, "下注成功", 0);

                                //发送广播
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);

                                mFrilstList.clear();
                                mSecondList.clear();
                                mMap.clear();
                                mPopBettingBody.dismiss();
                                Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.out_from_down);
                                animation.setFillAfter(false);
                                mBettingBodyLv.setClickable(true);
                                mBettingLy.startAnimation(animation);
                                mBettingLy.setVisibility(View.GONE);
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(mContent, LoginActivity.class);
                                    mContent.startActivity(intent);
                                    mPopBettingBody.dismiss();

                                } else {
                                    TmtUtils.midToast(mContent, result.getMessage(), 0);
                                }
                            }
                        }
                    });
                    mPL = 1;
                    //mBettingBodyUpload.setText("确认");
                    mBettingBodyLv.setClickable(false);
                } else {
                    mBettingBodyUpload.setClickable(true);
                    mBettingBodyLv.setClickable(true);
                    mPopBettingBody.dismiss();
                    Intent intent = new Intent(mContent, LoginActivity.class);
                    mContent.startActivity(intent);
                }
            }
        });
    }

    private void setSeriseNum() {
        switch (mFrilstList.size()) {
            case 1:
                mMode.setText("1串1");
                break;
            case 2:
                mMode.setText("2串1");
                break;
            case 3:
                mMode.setText("3串1");
                break;
            case 4:
                mMode.setText("4串1");
                break;
            case 5:
                mMode.setText("5串1");
                break;
            case 6:
                mMode.setText("6串1");
                break;
            case 7:
                mMode.setText("7串1");
                break;
            case 8:
                mMode.setText("8串1");
                break;
        }
    }

    @NonNull
    private BetBean getBetBeanBase(GBSeriesBean.PayRateEntity payRate) {
        BetBean bean = new BetBean();
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);
        if (b)
            bean.type = 1;
        else
            bean.type = 2;
        bean.payRateID = payRate.id;
        bean.matchID = payRate.matchID;
        bean.amount = "000";
        bean.home = payRate.home;
        bean.away = payRate.away;
        return bean;
    }


    private void processData(List<GBSeriesBean> bean) {
        mBettingLy.setVisibility(View.GONE);
        mData = bean;
        if (mData != null)
            for (GBSeriesBean d : mData) {
                boolean allResultShow = true;
                boolean nowLostShow = true;
                boolean bigSmallShow = true;
                List<GBSeriesBean.PayRateEntity> payRate = d.payRate;
                if (payRate != null)
                    for (GBSeriesBean.PayRateEntity p : payRate) {
                        p.home = d.home;
                        p.away = d.away;
                        switch (p.payTypeID) {
                            case 1:
                                if (allResultShow) {
                                    p.allResult = allResultShow;
                                    allResultShow = !allResultShow;
                                } else {
                                    p.allResult = allResultShow;
                                }
                                break;
                            case 2:
                                if (nowLostShow) {
                                    p.nowLost = nowLostShow;
                                    nowLostShow = !nowLostShow;
                                } else {
                                    p.nowLost = nowLostShow;
                                }
                                break;
                            case 3:
                                if (bigSmallShow) {
                                    p.allBigSmall = bigSmallShow;
                                    bigSmallShow = !bigSmallShow;
                                } else {
                                    p.allBigSmall = bigSmallShow;
                                }
                                break;
                        }
                    }
            }
        mExpandAdapter.setList(mData);
        mExpandableListView.expandGroup(0);
    }

    private void setCB() {
        mInputNum.setText(mCbBean.amount);
        Double resid = Double.parseDouble(mCbBean.amount.trim()) * mPL;
        String s = String.valueOf(mDf.format(resid));
        mCbBean.returnNum = s;
        mSeriseReturn.setText(s);
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContent);
        // 将layout转成view,因为dialog表示容器所以不能写true要写成空
        View contentView = View.inflate(mContent, R.layout.dialog_init, null);
        TextView notice = (TextView) contentView.findViewById(R.id.dialog_tv_notice);
        notice.setText(mContent.getString(R.string.balance_dialog_glod));
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
                        Intent intent = new Intent(mContent, StoreActivity.class);
                        intent.putExtra(Constent.DIAMONDS, "2");
                        mContent.startActivity(intent);
                        Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.out_from_down);
                        animation.setFillAfter(false);
                        mBettingLy.startAnimation(animation);
                        mBettingLy.setVisibility(View.GONE);
                        mPopBettingBody.dismiss();
                        mPL = 1;
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    /**
     * 创建者     ZCL
     * 创建时间   2016/9/13 17:22
     * 描述	      ${TODO}
     * <p/>
     * 更新者     $Author$
     * 更新时间   $Date$
     * 更新描述   ${TODO}
     */
    class GBSeriesAdapter extends BaseExpandableListAdapter {
        private Context content;
        private List<GBSeriesBean> data;


        public GBSeriesAdapter(Context content) {
            this.content = content;
            this.data = new ArrayList<>();
        }

        public void setList(List<GBSeriesBean> list) {
            if (list != null) {
                data.clear();
                data.addAll(list);
                notifyDataSetChanged();
            }
        }

        public void addList(List<GBSeriesBean> list) {
            if (list != null) {
                data.addAll(list);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getGroupCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(content, R.layout.gb_series_group_ly, null);
                holder.mLeagueName = (TextView) convertView.findViewById(R.id.gbseries_gp_league_name);
                holder.mOpenTime = (TextView) convertView.findViewById(R.id.gbseries_gp_open_time);
                holder.mMainTeam = (TextView) convertView.findViewById(R.id.gbseries_gp_main_team);
                holder.mSecondTeam = (TextView) convertView.findViewById(R.id.gbseries_gp_second_team);
                holder.mIv = (ImageView) convertView.findViewById(R.id.gbseries_gp_item_arrow);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mLeagueName.setText(data.get(groupPosition).leagueName);
            switch (groupPosition % 3) {
                case 0:
                    holder.mLeagueName.setTextColor(ContextCompat.getColor(content, R.color.orange_tv));
                    break;
                case 1:
                    holder.mLeagueName.setTextColor(ContextCompat.getColor(content, R.color.green_tv));
                    break;
                case 2:
                    holder.mLeagueName.setTextColor(ContextCompat.getColor(content, R.color.blue_tv));
                    break;
                default:
                    break;
            }
            String openTime = data.get(groupPosition).openTime;
            String time = openTime.substring(6, 16);
            holder.mOpenTime.setText(time);
            holder.mMainTeam.setText(data.get(groupPosition).home);
            holder.mSecondTeam.setText(data.get(groupPosition).away);

            if (isExpanded) {
                holder.mIv.setImageResource(R.mipmap.shang_);
            } else {
                holder.mIv.setImageResource(R.mipmap.xia);
            }
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(content, R.layout.gb_series_children_ly_gd, null);
                holder.childrenLv = (ListView) convertView.findViewById(R.id.series_children_lv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            GBSeriesBean entity = mData.get(groupPosition);
            if (entity != null) {
                List<GBSeriesBean.PayRateEntity> payRate = entity.payRate;
                SeriesChildrensAdapter adapter = new SeriesChildrensAdapter(payRate, mContent);
                holder.childrenLv.setAdapter(adapter);
                ListScrollUtil.setListViewHeightBasedOnChildren(holder.childrenLv);
            }

            return convertView;
        }

        private class ViewHolder {
            public ListView childrenLv;
            public TextView mLeagueName;
            public TextView mOpenTime;
            public TextView mMainTeam;
            public TextView mSecondTeam;
            public ImageView mIv;
        }

        /*-----------------------------------------------------------------------------------------------------*/
        @Override
        public Object getGroup(int groupPosition) {
            return data.get(groupPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return data.get(groupPosition).payRate.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

    private void visiablePop(HashMap<Integer, GBSeriesBean.PayRateEntity> map) {
        String size = map.size() + "";
        LogUtil.util(TAG, "显示选中个数------------------------------:" + size);
        mBettingNum.setText(size);
        mBettingBodyNum.setText(size);
        if (map.size() > 0) {
            if (mBettingLy.getVisibility() != View.VISIBLE) {
                Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.in_from_down);
                animation.setFillAfter(false);
                mBettingLy.startAnimation(animation);
                mBettingLy.setVisibility(View.VISIBLE);
            }
        } else {
            Animation animation = AnimationUtils.loadAnimation(mContent, R.anim.out_from_down);
            animation.setFillAfter(false);
            mPopBettingBody.dismiss();
            mBettingLy.startAnimation(animation);
            mBettingLy.setVisibility(View.GONE);
        }
    }

    private class MyBroadcastReciver extends BroadcastReceiver {
        private ArrayList<Integer> mListRemove;

        @Override
        public void onReceive(final Context mContent, Intent intent) {
            switch (intent.getAction()) {
                case Constent.LOADING_FOOTBALL:
                    boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);
                    LogUtil.util(TAG, "接到广播------:" + (b ? "足球" : "篮球"));
                    getData();
                    break;
            }
        }
    }

    public class SeriesChildrensAdapter extends BaseAdapter implements ListAdapter {
        private final Context context;
        private final List<GBSeriesBean.PayRateEntity> data;

        public SeriesChildrensAdapter(List<GBSeriesBean.PayRateEntity> payRate, Context context) {
            this.context = context;
            this.data = payRate;
        }

        @Override
        public int getCount() {
            if (data != null)
                return data.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.hot_match_info_item, null);
                holder = new ViewHolder();

                holder.mMatchInfoItemIcon = (ImageView) convertView.findViewById(R.id.match_info_item_icon);
                holder.mMatchInfoItemTitle = (TextView) convertView.findViewById(R.id.match_info_item_title);
                holder.mMatchInfoItemSubscribe = (TextView) convertView.findViewById(R.id.match_info_item_subscribe);
                holder.mItemSonLeftName = (TextView) convertView.findViewById(R.id.item_son_left_name);
                holder.mItemSonLeftMid = (TextView) convertView.findViewById(R.id.item_son_left_mid);
                holder.mItemSonLeftNum = (TextView) convertView.findViewById(R.id.item_son_left_num);
                holder.mItemSonLeft = (LinearLayout) convertView.findViewById(R.id.item_son_left);

                holder.mItemSonMidName = (TextView) convertView.findViewById(R.id.item_son_mid_name);
                holder.mItemSonMidNum = (TextView) convertView.findViewById(R.id.item_son_mid_num);
                holder.mItemSonMid = (LinearLayout) convertView.findViewById(R.id.item_son_mid);

                holder.mItemSonRightName = (TextView) convertView.findViewById(R.id.item_son_right_name);
                holder.mItemSonRightMid = (TextView) convertView.findViewById(R.id.item_son_right_mid);
                holder.mItemSonRightNum = (TextView) convertView.findViewById(R.id.item_son_right_num);
                holder.mItemSonRight = (LinearLayout) convertView.findViewById(R.id.item_son_right);
                holder.mMatchInfoItemTitleLy = (LinearLayout) convertView.findViewById(R.id.match_info_item_title_ly);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GBSeriesBean.PayRateEntity d = data.get(position);
            holder.mMatchInfoItemTitle.setText(d.payTypeName);
            switch (d.payTypeID) {
                case 1:
                    if (d.allResult)
                        holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                    else
                        holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                    holder.mMatchInfoItemIcon.setImageResource(R.mipmap.vs);
                    holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.guess_ninty_result));
                    break;
                case 2:
                    if (d.nowLost)
                        holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                    else
                        holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);

                    holder.mItemSonLeftMid.setVisibility(View.VISIBLE);
                    holder.mItemSonRightMid.setVisibility(View.VISIBLE);
                    holder.mItemSonMid.setVisibility(View.GONE);

                    holder.mMatchInfoItemIcon.setImageResource(R.mipmap.rangqiu);
                    holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.ninty_lostball));
                    setLost(holder, d);
                    break;
                case 3:
                    if (d.allBigSmall)
                        holder.mMatchInfoItemTitleLy.setVisibility(View.VISIBLE);
                    else
                        holder.mMatchInfoItemTitleLy.setVisibility(View.GONE);
                    holder.mItemSonLeftMid.setVisibility(View.VISIBLE);
                    holder.mItemSonRightMid.setVisibility(View.VISIBLE);
                    holder.mItemSonMid.setVisibility(View.GONE);

                    holder.mMatchInfoItemIcon.setImageResource(R.mipmap.daxiao);
                    holder.mMatchInfoItemSubscribe.setText(context.getString(R.string.ninty_all_balls));
                    setLost(holder, d);
                    break;
            }

            //主队
            holder.mItemSonLeftName.setText(d.home);
            holder.mItemSonLeftNum.setText(d.realRate1);
            //平局
            holder.mItemSonMidName.setText(context.getString(R.string.draw_result));
            holder.mItemSonMidNum.setText(d.realRate2);
            //客队
            holder.mItemSonRightName.setText(d.away);
            holder.mItemSonRightNum.setText(d.realRate3);
            if (!d.cbTag1) {
                holder.mItemSonLeft.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mItemSonLeftName.setTextColor(context.getResources().getColor(R.color.black));
                holder.mItemSonLeftNum.setTextColor(context.getResources().getColor(R.color.orange));
            } else {
                holder.mItemSonLeft.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.mItemSonLeftName.setTextColor(context.getResources().getColor(R.color.white));
                holder.mItemSonLeftNum.setTextColor(context.getResources().getColor(R.color.yellow_light));
            }
            if (!d.cbTag2) {
                holder.mItemSonMid.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mItemSonMidName.setTextColor(context.getResources().getColor(R.color.black));
                holder.mItemSonMidNum.setTextColor(context.getResources().getColor(R.color.orange));
            } else {
                holder.mItemSonMid.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.mItemSonMidName.setTextColor(context.getResources().getColor(R.color.white));
                holder.mItemSonMidNum.setTextColor(context.getResources().getColor(R.color.yellow_light));
            }
            if (!d.cbTag3) {
                holder.mItemSonRight.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.mItemSonRightName.setTextColor(context.getResources().getColor(R.color.black));
                holder.mItemSonRightNum.setTextColor(context.getResources().getColor(R.color.orange));
            } else {
                holder.mItemSonRight.setBackgroundColor(context.getResources().getColor(R.color.orange));
                holder.mItemSonRightName.setTextColor(context.getResources().getColor(R.color.white));
                holder.mItemSonRightNum.setTextColor(context.getResources().getColor(R.color.yellow_light));
            }
            holder.mItemSonLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMap.size() < 8 || mMap.containsKey(d.matchID)) {
                        setBackData();
                        d.cbTag1 = !d.cbTag1;
                        if (d.cbTag1) {
                            mMap.put(d.matchID, d);
                        } else {
                            setRemove(d);
                        }
                    }
                    visiablePop(mMap);
                    LogUtil.util(TAG, "返回数据是------------------------------:" + mMap.size());
                }
            });
            holder.mItemSonMid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMap.size() < 8 || mMap.containsKey(d.matchID)) {
                        setBackData();
                        d.cbTag2 = !d.cbTag2;
                        if (d.cbTag2) {
                            mMap.put(d.matchID, d);
                        } else {
                            setRemove(d);
                        }
                    }
                    visiablePop(mMap);
                    LogUtil.util(TAG, "返回数据是------------------------------:" + mMap.size());
                }
            });
            holder.mItemSonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMap.size() < 8 || mMap.containsKey(d.matchID)) {
                        setBackData();
                        d.cbTag3 = !d.cbTag3;
                        if (d.cbTag3) {
                            mMap.put(d.matchID, d);
                        } else {
                            setRemove(d);
                        }
                    }
                    visiablePop(mMap);
                    LogUtil.util(TAG, "返回数据是------------------------------:" + mMap.size());
                }
            });
            return convertView;
        }


        private void setBackData() {
            for (GBSeriesBean.PayRateEntity p : data) {
                p.cbTag1 = false;
                p.cbTag2 = false;
                p.cbTag3 = false;
            }
            notifyDataSetChanged();
        }

        private void setRemove(GBSeriesBean.PayRateEntity d) {
            if (!d.cbTag1 && !d.cbTag2 && !d.cbTag3)
                mMap.remove(d);
        }

        private void setLost(ViewHolder baseViewHolder, GBSeriesBean.PayRateEntity d) {
            if (d.realRate2.contains("/")) {
                String[] split = d.realRate2.split("/");
                if (!split[0].equals(split[1]) && Math.abs(Double.valueOf(split[0])) == Math.abs(Double.valueOf(split[1]))) {
                    if (Double.valueOf(split[0]) > 0) {
                        String s = "+" + split[0];
                        baseViewHolder.mItemSonLeftMid.setText(s);
                    } else {
                        baseViewHolder.mItemSonLeftMid.setText(split[0]);
                    }
                    if (Double.valueOf(split[1]) > 0) {
                        String s = "+" + split[1];
                        baseViewHolder.mItemSonRightMid.setText(s);
                    } else {
                        baseViewHolder.mItemSonRightMid.setText(split[1]);
                    }
                } else {
                    baseViewHolder.mItemSonLeftMid.setText(d.realRate2);
                    baseViewHolder.mItemSonRightMid.setText(d.realRate2);
                }
            } else {
                if (Double.valueOf(d.realRate2) > 0) {
                    String s = "+" + d.realRate2;
                    baseViewHolder.mItemSonLeftMid.setText(s);
                    String s1 = "-" + d.realRate2;
                    baseViewHolder.mItemSonRightMid.setText(s1);
                } else {
                    if (Double.valueOf(d.realRate2) == 0) {
                        String s = "" + Math.abs(Double.valueOf(d.realRate2));
                        baseViewHolder.mItemSonLeftMid.setText(s);
                        String s1 = "" + Math.abs(Double.valueOf(d.realRate2));
                        baseViewHolder.mItemSonRightMid.setText(s1);
                    } else {
                        baseViewHolder.mItemSonLeftMid.setText(d.realRate2);
                        String text = "+" + Math.abs(Double.valueOf(d.realRate2));
                        baseViewHolder.mItemSonRightMid.setText(text);
                    }
                }
            }
        }

        class ViewHolder {
            ImageView mMatchInfoItemIcon;
            TextView mMatchInfoItemTitle;
            TextView mMatchInfoItemSubscribe;
            TextView mItemSonLeftName;
            TextView mItemSonLeftMid;
            TextView mItemSonLeftNum;
            LinearLayout mItemSonLeft;

            TextView mItemSonMidName;
            TextView mItemSonMidNum;
            LinearLayout mItemSonMid;
            TextView mItemSonRightName;
            TextView mItemSonRightMid;
            TextView mItemSonRightNum;
            LinearLayout mItemSonRight;
            LinearLayout mMatchInfoItemTitleLy;
        }
    }

    public class PopBettingCgBodyAdapter extends BaseAdapter implements ListAdapter {
        private final ArrayList<BetBean> list;
        private Context context;
        private final ArrayList<Integer> mList;


        public PopBettingCgBodyAdapter(Context context, ArrayList<BetBean> list) {
            this.context = context;
            this.list = list;
            mDf = new DecimalFormat(".00");
            mList = new ArrayList<>();
        }


        @Override
        public int getCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder hoder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.pop_betting_lv_item_serise, null);
                hoder = new ViewHolder();

                hoder.mItemBettingBodyPL = (TextView) convertView.findViewById(R.id.item_betting_body_pl_serise);
                hoder.mItemBettingBodyClose = (ImageView) convertView.findViewById(R.id.item_betting_body_close_serise);
                hoder.mBettingBodyItemResult = (TextView) convertView.findViewById(R.id.betting_body_item_result_serise);
                hoder.mBettingBodyItemTeam = (TextView) convertView.findViewById(R.id.betting_body_item_team_serise);

                convertView.setTag(hoder);
            } else {
                hoder = (ViewHolder) convertView.getTag();
            }
            /**
             * 还原初始化状态
             */
            BetBean betBean = list.get(position);
            switch (betBean.selected) {
                case 0:
                    break;
                case 1:
                    hoder.mBettingBodyItemResult.setText(list.get(position).home);
                    break;
                case 2:
                    hoder.mBettingBodyItemResult.setText(list.get(position).away);
                    break;
                case 3:
                    hoder.mBettingBodyItemResult.setText("平局");
                    break;
            }

            final BetBean bean = list.get(position);

            /**
             * 还原状态
             */
            String text = bean.home + "vs" + bean.away;
            hoder.mBettingBodyItemTeam.setText(text);//队伍
            hoder.mItemBettingBodyPL.setText(bean.realRate);//赔率

            hoder.mItemBettingBodyClose.setOnClickListener(new MyItemSonCloseClickListern(context, position) {
                @Override
                public void onClick(View v) {
                    //移除之前先改变其值
                    LogUtil.util(TAG, "串关返回数据是---:" + bean.toString() + "------" + JSON.toJSON(bean));
                    list.remove(bean);
                    mFrilstList.remove(bean);
                    mMap.remove(bean.matchID);
                    visiablePop(mMap);
                    mPL = mPL / Double.parseDouble(bean.realRate);
                    mSerisePL.setText(mDf.format(mPL));
                    setSeriseNum();
                    String s = mInputNum.getText().toString();
                    if (!"请输入金币数".equals(s)) {
                        String returnNum = mDf.format(mPL * Integer.parseInt(s));
                        mSeriseReturn.setText(returnNum);
                    }

                    for (GBSeriesBean data : mData) {
                        if (bean.matchID == data.id) {
                            List<GBSeriesBean.PayRateEntity> payRate = data.payRate;
                            for (GBSeriesBean.PayRateEntity p : payRate) {
                                p.cbTag1 = false;
                                p.cbTag2 = false;
                                p.cbTag3 = false;
                            }
                        }
                    }
                    mExpandAdapter.setList(mData);
                    PopBettingCgBodyAdapter.this.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView mItemBettingBodyPL;
            ImageView mItemBettingBodyClose;
            TextView mBettingBodyItemResult;
            TextView mBettingBodyItemTeam;
        }

        public ArrayList<BetBean> getList() {
            return list;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public ArrayList<Integer> getListRemove() {
            return mList;
        }

        //根据目前的位置来判断到底当前条目是什么类型
        @Override
        public int getItemViewType(int position) {

       /* if (position == list.size()) {
            return ITEM_Bottom;
        }*/
            return super.getItemViewType(position);
        }

        //用于控制listview中有多少条目类型
        @Override
        public int getViewTypeCount() {
            return 1;
        }

    }
}

