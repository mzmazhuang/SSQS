package com.dading.ssqs.controllar.scores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.ScoreMatchAdapterJS;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseScoreControllar;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/5 14:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class BaseJs extends BaseScoreControllar {
    private static final String TAG = "BaseJs";
    public PullToRefreshListView mJsList;
    private ScoreMatchAdapterJS mAdapter;
    private List<ScoreBean> mItems;
    private String mFormatData;
    private int mCount = 10;
    private int mTotalCount;
    public JSRecevice mRecevice;
    public Timer mTimer;
    private int mFristItem;
    private int mPage;
    public RelativeLayout mEmpty;
    public LinearLayout mLoadAnimal;
    private Runnable mTask;
    private Runnable mTaskMore;
    public LinearLayout mEmptyGB;
    private TextView mEmptyGBGO;
    private ImageView mLoadAnimalIv;
    private AnimationDrawable mDrawable;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;

    @Override
    public void initScorecalendar() {
        int height = mScoreWeekLayout.getHeight();
        mScoreWeekLayout.setPadding(0, -height, 0, 0);
    }

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(mContent, R.layout.scorepager_js, null);

        mJsList = (PullToRefreshListView) view.findViewById(R.id.score_vp_js);
        mEmpty = (RelativeLayout) view.findViewById(R.id.data_empty);
        mEmptyGB = (LinearLayout) view.findViewById(R.id.guess_ball_no_data);

        mEmptyGBGO = (TextView) view.findViewById(R.id.go_to_match_before);

        setEmptyView();

        mLoadAnimal = (LinearLayout) view.findViewById(R.id.loading_anim);
        mLoadAnimalIv = (ImageView) view.findViewById(R.id.loading_anim_iv);

        mAdapter = new ScoreMatchAdapterJS(mContent, 0);
        mJsList.setAdapter(mAdapter);

        mJsList.getRefreshableView().post(new Runnable() {
            @Override
            public void run() {
                mJsList.getRefreshableView().smoothScrollToPosition(mFristItem);
            }
        });

        return view;
    }

    private boolean hasInit = false;

    public void init() {
        if (!hasInit) {
            hasInit = true;

            mDrawable.start();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    getData(UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true), mCount, mFormatData, "0", "0", 1);
                }
            };
            mTimer.schedule(task, 5000, 40 * 1000);

            getData(UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true), mCount, mFormatData, "0", "0", 1);
        }
    }

    public void setEmptyView() {
    }

    @Override
    public void initData() {
        mLoadAnimal.setVisibility(View.VISIBLE);
        mLoadAnimalIv.setImageResource(R.drawable.loading_anim);
        mDrawable = (AnimationDrawable) mLoadAnimalIv.getDrawable();

        mTimer = new Timer();

        mRecevice = new JSRecevice();

        setSend();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss", Locale.CHINA);
        Date date = calendar.getTime();
        mFormatData = sdf.format(date);
        Logger.INSTANCE.d(TAG, "日期是-------------------------------------" + mFormatData);
        mCount = 10;
    }

    public void setSend() {
        UIUtils.ReRecevice(mRecevice, Constent.JS_RECEVICE);
        UIUtils.ReRecevice(mRecevice, Constent.JS_RECEVICE_CB);
    }


    @Override
    public void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
        if (mTimer != null)
            mTimer.cancel();
        UIUtils.removeTask(mTask);
        UIUtils.removeTask(mTaskMore);
    }

    public void getData(boolean b, int count, String date, String subType, String leagusID, int page) {
        if (!isRefresh && !isLoadMore) {
            isRefresh = true;

            SSQSApplication.apiClient(0).getMatchBallOrTypeList(b, 2, date, subType, 0, leagusID, page, count, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    mLoadAnimal.setVisibility(View.GONE);
                    mFristItem = 0;
                    mDrawable.stop();
                    mJsList.onRefreshComplete();
                    isRefresh = false;

                    if (result.isOk()) {
                        CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                        if (page != null) {
                            mTotalCount = page.getTotalCount();
                            mPage = page.getTotalPage();

                            if (page.getItems() != null) {
                                mItems = page.getItems();

                                mAdapter.setData(mItems);
                            }
                        }
                    } else {
                        ToastUtils.midToast(mContent, result.getMessage(), 0);
                        Logger.INSTANCE.d(TAG, result.getMessage() + "下拉JS失败信息");
                    }
                }
            });
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mEmptyGBGO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.SendReRecevice(Constent.GO_TO_BEFORE);
                UIUtils.SendReRecevice(Constent.LOADING_SCORE);
            }
        });
        mJsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mFristItem = firstVisibleItem;
            }
        });
        mJsList.setMode(PullToRefreshBase.Mode.BOTH);
        mJsList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mTask = new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.getSputils().putString(Constent.SUBTYPE, "0");
                        UIUtils.getSputils().putString(Constent.LEAGUEIDS, "0");

                        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                        getData(b, mCount, mFormatData, "0", "0", 1);
                    }
                };
                UIUtils.postTaskDelay(mTask, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //关闭上啦加载的效果
                mTaskMore = new Runnable() {
                    @Override
                    public void run() {
                        Logger.INSTANCE.d(TAG, "这是第几页--------------:" + mPage);
                        ++mPage;
                        if (mPage > mTotalCount) {
                            --mPage;
                            ToastUtils.midToast(UIUtils.getContext(), "已全部加载,无新数据!", 0);
                            mJsList.onRefreshComplete();
                        } else {
                            boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                            if (!isLoadMore && !isRefresh) {
                                isLoadMore = true;
                                SSQSApplication.apiClient(0).getMatchBallOrTypeList(b, 2, mFormatData,
                                        (b ? UIUtils.getSputils().getString(Constent.SUBTYPE, "0") : "0"), 0,
                                        (b ? UIUtils.getSputils().getString(Constent.LEAGUEIDS, "0") : "0"), mPage, 10, new CcApiClient.OnCcListener() {
                                            @Override
                                            public void onResponse(CcApiResult result) {
                                                mJsList.onRefreshComplete();
                                                isLoadMore = false;

                                                if (result.isOk()) {
                                                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                                                    if (page != null) {
                                                        mPage = page.getTotalPage();
                                                        mTotalCount = page.getTotalCount();

                                                        if (page.getItems() != null) {
                                                            addData(page.getItems());
                                                        }
                                                    }
                                                } else {
                                                    mJsList.setMode(PullToRefreshBase.Mode.BOTH);

                                                    Logger.INSTANCE.d(TAG, result.getMessage() + "失败信息");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                };
                UIUtils.postTaskDelay(mTaskMore, 500);
            }
        });
    }

    private void addData(List<ScoreBean> items) {
        int mCount = mItems.size();
        mItems.addAll(items);
        if (mItems != null) {
            mAdapter.addData(items);

            mJsList.getRefreshableView().setSelection(mCount);
            mJsList.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }

    private class JSRecevice extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);
            String s = UIUtils.getSputils().getString(Constent.LEAGUEIDS, "0");
            String action = intent.getAction();

            if (!isRefresh && !isLoadMore) {
                isRefresh = true;
                SSQSApplication.apiClient(0).getMatchBallOrTypeList(b, 2, mFormatData,
                        (b ? UIUtils.getSputils().getString(Constent.SUBTYPE, "0") : "0"), 0,
                        (action.equals(Constent.JS_SG_SC_FITTER) ? s : "0"), 1, 10, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                mLoadAnimal.setVisibility(View.GONE);
                                mDrawable.stop();
                                isRefresh = false;

                                if (result.isOk()) {
                                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                                    if (page != null) {
                                        mPage = page.getTotalPage();
                                        mTotalCount = page.getTotalCount();

                                        if (page.getItems() != null) {

                                            if (mAdapter != null) {
                                                int mPostion = mAdapter.getScorePostion();

                                                mItems = page.getItems();

                                                mAdapter.setData(mItems);

                                                mJsList.getRefreshableView().smoothScrollToPosition(mPostion);
                                            }
                                        }
                                    }
                                } else {
                                    ToastUtils.midToast(mContent, result.getMessage(), 0);
                                }
                            }
                        });
            }
        }
    }
}
