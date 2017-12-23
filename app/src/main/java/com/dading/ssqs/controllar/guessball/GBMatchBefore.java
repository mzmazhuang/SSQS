package com.dading.ssqs.controllar.guessball;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.GuessBallChoiceGvAdapter;
import com.dading.ssqs.adapter.MatchBeforeLDAdapter;
import com.dading.ssqs.adapter.MatchBeforeRDAdapter;
import com.dading.ssqs.adapter.MatchBeforeRNAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseGuessball;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/7 11:40
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBMatchBefore extends BaseGuessball {

    private static final String TAG = "GBMatchBefore";


    RadioGroup mTimeLeague;
    LinearLayout mByLeague;
    LinearLayout mPlayHelp;
    ListView mListLeft;
    ListView mListRight;
    SwipeRefreshLayout mRefresh;

    private View mView;
    private View mChioceView;
    private int mType;
    private MatchBeforBeanAll mLeftBean;

    private GridView mChioceSetting;
    public PopupWindow mPopChioce;
    private LinearLayout mChioceLy;
    private RadioGroup mChioceGp;
    private RadioButton mChioceSettingRb1;
    private TextView mChioceConfirm;
    private GuessBallChoiceGvAdapter mAdapter;
    private ArrayList<GusessChoiceBean.FilterEntity> mList;
    private RadioButton mChioceSettingRb2;
    public MatchBeforRecevice mReceiver;
    private List<MatchBeforBeanAll.LeagueNameEntity> mLeagueName;
    private MatchBeforeLAdapter mAdapterLN;
    private MatchBeforeRNAdapter mAdapterRn;
    private List<MatchBeforBeanAll.LeagueNameEntity.MatchsEntity> mMatchsN;
    private List<MatchBeforBeanAll.LeagueDateEntity> mLeagueDate;
    private List<MatchBeforBeanAll.LeagueDateEntity.MatchsEntity> mMatchsRd;
    private MatchBeforeRDAdapter mAdapterRd;
    private MatchBeforeLDAdapter mAdapterLd;

    private int isCurrUrl = 0;


    @Override
    protected View initMidContentView(Context content) {
        mList = new ArrayList<>();
        mView = View.inflate(mContent, R.layout.gb_matchbefore, null);

        mRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.guessball_matchbefore_refresh);
        mListLeft = (ListView) mView.findViewById(R.id.guessball_matchbefore_left);
        mListRight = (ListView) mView.findViewById(R.id.guessball_matchbefore_right);

        mTimeLeague = (RadioGroup) mView.findViewById(R.id.by_time_laegue);
        mByLeague = (LinearLayout) mView.findViewById(R.id.by_league);
        mPlayHelp = (LinearLayout) mView.findViewById(R.id.matchbefore_playhelp);


        mChioceView = View.inflate(mContent, R.layout.pop_chioce_view, null);
        mChioceSetting = (GridView) mChioceView.findViewById(R.id.guessball_chioce_setting);
        mChioceGp = (RadioGroup) mChioceView.findViewById(R.id.guessball_chioce_gp);
        mChioceSettingRb1 = (RadioButton) mChioceView.findViewById(R.id.guessball_chioce_rb1);
        mChioceSettingRb2 = (RadioButton) mChioceView.findViewById(R.id.guessball_chioce_rb2);
        mChioceLy = (LinearLayout) mChioceView.findViewById(R.id.guessball_chioce_ly);
        mChioceConfirm = (TextView) mChioceView.findViewById(R.id.guessball_chioce_confirm);

        mPopChioce = PopUtil.popuMake(mChioceView);

        mReceiver = new MatchBeforRecevice();
        UIUtils.ReRecevice(mReceiver, Constent.LOADING_FOOTBALL);
        UIUtils.ReRecevice(mReceiver, Constent.SQ_RECEVICE);

        mAdapter = new GuessBallChoiceGvAdapter(mContent);
        mChioceSetting.setAdapter(mAdapter);

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
        getData();
        mChioceSettingRb2.setChecked(true);
    }

    private void getMatchGuessData() {
        SSQSApplication.apiClient(0).getMatchGuessList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mLeftBean = (MatchBeforBeanAll) result.getData();

                    if (mLeftBean != null) {
                        mLoadingAnimal.setVisibility(View.GONE);
                        mDrawable.stop();
                        processData(mLeftBean);
                    }

                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                    mLoadingAnimal.setVisibility(View.GONE);
                    mDrawable.stop();
                    TmtUtils.midToast(mContent, result.getMessage(), 0);
                    LogUtil.util(TAG, "赛前请求失败------------------------------:" + result.getMessage());
                }
            }
        });
    }

    private void getMatchBallGuessData() {
        SSQSApplication.apiClient(0).getMatchBallGuessList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mLeftBean = (MatchBeforBeanAll) result.getData();

                    if (mLeftBean != null) {
                        mLoadingAnimal.setVisibility(View.GONE);
                        mDrawable.stop();
                        processData(mLeftBean);
                    }

                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                    mLoadingAnimal.setVisibility(View.GONE);
                    mDrawable.stop();
                    TmtUtils.midToast(mContent, result.getMessage(), 0);
                    LogUtil.util(TAG, "赛前请求失败------------------------------:" + result.getMessage());
                }
            }
        });
    }

    private void getData() {
        ListScrollUtil.setNoFit(mListLeft, mRefresh);
        ListScrollUtil.setNoFit(mListRight, mRefresh);

        mLoadingAnimal.setVisibility(View.VISIBLE);
        mDrawable.start();

        mType = 2; //1,显示时间  2,顯示聯賽
        mTimeLeague.check(R.id.by_league_match);

        getNetDataWork();
    }

    private void getNetDataWork() {
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        if (b) {
            isCurrUrl = 1;
            getMatchGuessData();
        } else {
            isCurrUrl = 2;
            getMatchBallGuessData();
        }
    }

    private void processData(MatchBeforBeanAll bean) {
        if (mLeftBean != null) {
            if (mType == 2) {
                mLeagueName = bean.leagueName;
                if (mLeagueName != null) {
                    for (int i = 0; i < mLeagueName.size(); i++) {
                        MatchBeforBeanAll.LeagueNameEntity entity = mLeagueName.get(i);
                        if (i == 0) {
                            entity.isColor = true;
                        } else {
                            entity.isColor = false;
                        }
                    }
                    mAdapterLN = new MatchBeforeLAdapter(mContent, mLeagueName);
                    mListLeft.setAdapter(mAdapterLN);
                }

                if (mLeagueName != null && mLeagueName.size() > 0) {
                    mMatchsN = mLeagueName.get(0).matchs;
                    mAdapterRn = new MatchBeforeRNAdapter(mContent, mMatchsN);
                    LogUtil.util(TAG, "返回联赛数据是------" + mMatchsN.size() + "-------:" + mMatchsN.toString());
                    mListRight.setAdapter(mAdapterRn);
                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                }
            } else if (mType == 1) {
                mLeagueDate = bean.leagueDate;
                for (int i = 0; i < mLeagueDate.size(); i++) {
                    LogUtil.util(TAG, "返回数据是---------------" + mLeagueDate.size() + "---------------:" + i);
                    MatchBeforBeanAll.LeagueDateEntity entity = mLeagueDate.get(i);
                    if (i == 0) {
                        entity.isColor = true;
                    } else {
                        entity.isColor = false;
                    }
                }
                mAdapterLd = new MatchBeforeLDAdapter(mContent, mLeagueDate);
                mListLeft.setAdapter(mAdapterLd);
                if (mLeagueDate.size() > 0) {
                    mMatchsRd = mLeagueDate.get(0).matchs;
                    LogUtil.util(TAG, "返回时间数据是------" + mMatchsRd.size() + "-------:" + mMatchsRd.toString());
                    mAdapterRd = new MatchBeforeRDAdapter(mContent, mMatchsRd);
                    mListRight.setAdapter(mAdapterRd);
                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                }
            }
        } else {
            TmtUtils.midToast(mContent, "mLeftBean is not null", 0);
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initListener() {
        mListLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mType == 2) {
                    for (MatchBeforBeanAll.LeagueNameEntity l : mLeagueName) {
                        l.isColor = false;
                    }
                    mLeagueName.get(position).isColor = true;
                    mAdapterRn = new MatchBeforeRNAdapter(mContent, mLeagueName.get(position).matchs);
                    mListRight.setAdapter(mAdapterRn);

                    mAdapterLN.notifyDataSetChanged();

                } else if (mType == 1) {
                    for (MatchBeforBeanAll.LeagueDateEntity l : mLeagueDate) {
                        l.isColor = false;
                    }
                    mLeagueDate.get(position).isColor = true;
                    mAdapterRd = new MatchBeforeRDAdapter(mContent, mLeagueDate.get(position).matchs);
                    mListRight.setAdapter(mAdapterRd);
                    mAdapterLd.notifyDataSetChanged();
                }
            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.setRefreshing(true);
                mEmpty.setVisibility(View.GONE);

                if (isCurrUrl == 1) {
                    getMatchGuessData();
                } else if (isCurrUrl == 2) {
                    getMatchBallGuessData();
                }

                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mRefresh.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        mPopChioce.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPlayHelp.setClickable(true);
                mByLeague.setClickable(true);
            }
        });
        mChioceSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LogUtil.util(TAG, "全部不選------------------------------:");
               /* mChioceSettingRb1.setChecked(false);
                mChioceSettingRb2.setChecked(false);*/
                boolean checked = mList.get(position).checked;
                if (checked) {
                    mList.get(position).checked = false;
                } else {
                    mList.get(position).checked = true;
                }
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();
            }
        });
        mChioceGp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.guessball_chioce_rb1:
                        LogUtil.util(TAG, "全選------------------------------:");
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).checked = true;
                        }
                        if (mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                        break;
                    case R.id.guessball_chioce_rb2:
                        LogUtil.util(TAG, "全bu選------------------------------:");
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).checked = false;
                        }
                        if (mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });
        mChioceConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingAnimal.setVisibility(View.VISIBLE);
                mDrawable.start();
                mPopChioce.dismiss();
                StringBuilder sb = new StringBuilder();
                ArrayList<GusessChoiceBean.FilterEntity> listAdd = new ArrayList<>();
                if (mAdapter != null) {
                    List<GusessChoiceBean.FilterEntity> list = mAdapter.getList();
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
                 * 15.	猜球-根据筛选项获取赛前列表
                 a)	请求地址：
                 /v1.0/match/guess/leagueIDs/{leagueIDs}
                 b)	请求方式:
                 get
                 c)	请求参数说明：
                 auth_token：登陆后加入请求头
                 leagueIDs:联赛ids，以逗号隔开

                 29.	根据筛选获取篮球赛前列表
                 1)	请求地址：
                 /v1.0/match/guess/ball/leagueIDs/{ leagueIDs}
                 2)	请求方式:
                 get
                 */

                boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                if (b) {
                    SSQSApplication.apiClient(0).getMatchGuessLeagueIds(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                mLeftBean = (MatchBeforBeanAll) result.getData();

                                if (mLeftBean != null) {
                                    mLoadingAnimal.setVisibility(View.GONE);
                                    mDrawable.stop();

                                    processData(mLeftBean);
                                }
                            } else {
                                mLoadingAnimal.setVisibility(View.GONE);
                                mDrawable.stop();
                            }
                        }
                    });
                } else {
                    SSQSApplication.apiClient(0).getMatchBallGuessLeagueIds(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                mLeftBean = (MatchBeforBeanAll) result.getData();

                                if (mLeftBean != null) {
                                    mLoadingAnimal.setVisibility(View.GONE);
                                    mDrawable.stop();

                                    processData(mLeftBean);
                                }
                            } else {
                                mLoadingAnimal.setVisibility(View.GONE);
                                mDrawable.stop();
                            }
                        }
                    });
                }
            }
        });
        mChioceLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopChioce.dismiss();
            }
        });
        mByLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * a)	请求地址：
                 /v1.0/match/filter
                 b)	请求方式:
                 get
                 c)	请求参数说明：
                 auth_token：登陆后加入请求头

                 28.	篮球赛前筛选列表
                 1)	请求地址：
                 /v1.0/match/ball/filter
                 2)	请求方式:
                 get
                 */
                boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                if (b) {
                    SSQSApplication.apiClient(0).getMatchFilter(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                List<GusessChoiceBean> items = (List<GusessChoiceBean>) result.getData();

                                if (items != null) {
                                    processDataChoice(items);
                                }
                            } else {
                                TmtUtils.midToast(mContent, result.getMessage(), 0);
                                LogUtil.util(TAG, result.getMessage() + "竞猜筛选失败信息");
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
                                    processDataChoice(items);
                                }
                            } else {
                                TmtUtils.midToast(mContent, result.getMessage(), 0);
                                LogUtil.util(TAG, result.getMessage() + "竞猜筛选失败信息");
                            }
                        }
                    });
                }
            }
        });

        mTimeLeague.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.by_time_match:
                        mType = 1;
                        break;
                    case R.id.by_league_match:
                        mType = 2;
                        break;
                }
                processData(mLeftBean);
            }
        });
    }

    private void processDataChoice(List<GusessChoiceBean> data) {
        mList.clear();
        for (int i = 0; i < data.size(); i++) {
            GusessChoiceBean entity = data.get(i);
            for (int j = 0; j < entity.filter.size(); j++) {
                mList.add(entity.filter.get(j));
            }
        }
        mAdapter.setList(mList);

        mPlayHelp.setClickable(false);
        mByLeague.setClickable(false);
        mChioceSettingRb2.setChecked(true);
        mChioceSettingRb1.setChecked(false);
        mPopChioce.showAsDropDown(mByLeague, 0, 0);
    }

    private class MatchBeforRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.util("GBSS", "赛前收到广播------:");
            String action = intent.getAction();
            if (action.equals(Constent.LOADING_FOOTBALL)) {
                getData();
            } else if (isCurrUrl == 1) {
                getMatchGuessData();
            } else if (isCurrUrl == 2) {
                getMatchBallGuessData();
            }
        }
    }
}
