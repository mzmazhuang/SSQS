package com.dading.ssqs.controllar.guessball;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseGuessball;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/7 11:40
 * 描述	      猜球滚球
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBGrounder extends BaseGuessball {

    private static final String TAG = "GBGrounder";
    PullToRefreshListView mPullToRefreshListView;
    private int mPage;
    private int mCount;
    private List<ScoreBean> mItems;
    private int mTotalCount;
    private ScoreMatchAdapterGQ mAdapter;
    private String mDate;
    public GQRecevice mRecevice;
    private Runnable mTask;

    private String leagueIDs = "0";

    @Override
    protected View initMidContentView(Context content) {
        View view = View.inflate(mContent, R.layout.gb_grounder, null);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.guessball_grounder_exp_info);
        mPullToRefreshListView.getRefreshableView().setEmptyView(mEmpty);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mRecevice = new GQRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.GQ_RECEVICE);
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_FOOTBALL);
        getData();

    }

    @Override
    public void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
        UIUtils.removeTask(mTask);
    }

    private void getData() {
        /**
         * 23.	滚球列表
         1)	请求地址：/v1.0/match/type/{type}/date/{date}/subType/{subType}/leagueIDs/{leagueIds}/page/{page}/count/10
         2)	请求方式:get
         3)	请求参数说明：
         字段名	类型	长度	是否必填	备注
         auth_token	varchar		否	token
         type	int	1	是	2,即时    3，赛果    4,赛程    5,关注    6：滚球
         date	varchar	8	是	时间（格式:yyyyMMdd）默认当前时间
         subType	int	1	是	0-全部 1-热门 ，默认值0
         leagueIDs	varchar		是	联赛ID，以逗号隔开，默认值”0”
         page	int		是	第几页，当前页码
         count	int		是	条数，默认值10
         32.	篮球滚球列表
         1)	请求地址：/v1.0/match/roll/ball/subType/{subType}/leagueIDs/{leagueIds}/page/{page}/count/10
         */
        mLoadingAnimal.setVisibility(View.VISIBLE);
        mDrawable.start();

        mDate = DateUtils.getCurTime("yyyyMMdd");
        mPage = 1;

        getNetDataWork();
    }

    private void getNetDataWork() {
        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        SSQSApplication.apiClient(0).getScrollBallList(b, 6, mDate, 0,leagueIDs, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mLoadingAnimal.setVisibility(View.GONE);
                mDrawable.stop();

                if (result.isOk()) {
                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                    if (page != null) {
                        mTotalCount = page.getTotalCount();
                        mPage = page.getTotalPage();

                        if (page.getItems() != null) {
                            mItems = page.getItems();

                            mAdapter = new ScoreMatchAdapterGQ(mContent, page.getItems(), 4);
                            mPullToRefreshListView.setAdapter(mAdapter);
                        }
                    }
                } else {
                    mEmpty.setVisibility(View.VISIBLE);
                    mLoadingAnimal.setVisibility(View.GONE);
                    mDrawable.stop();

                    TmtUtils.midToast(mContent, result.getMessage(), 0);
                }
            }
        });
    }

    @Override
    protected void initListener() {
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                SSQSApplication.apiClient(0).getScrollBallList(b, 6, mDate, 0,leagueIDs, mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mPullToRefreshListView.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                            if (page != null) {
                                mPage = page.getTotalPage();
                                mTotalCount = page.getTotalCount();
                                if (page.getItems() != null) {
                                    mItems = page.getItems();

                                    mPullToRefreshListView.setAdapter(new ScoreMatchAdapterGQ(mContent, mItems, 4));
                                }
                            }
                        } else {
                            TmtUtils.midToast(mContent, result.getMessage(), 0);
                            LogUtil.util(TAG, result.getMessage() + "下拉滚球失败信息");
                        }
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                LogUtil.util(TAG, "这是第几页--------------:" + mPage);
                ++mPage;
                if (mPage > mTotalCount) {
                    --mPage;
                    TmtUtils.midToast(UIUtils.getContext(), "已全部加载,无新数据!", 0);
                } else {
                    boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                    SSQSApplication.apiClient(0).getScrollBallList(b, 6, mDate, 0,leagueIDs, mPage, 10, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            mPullToRefreshListView.onRefreshComplete();

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
                                mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);

                                TmtUtils.midToast(mContent, result.getMessage(), 0);
                                LogUtil.util(TAG, result.getMessage() + "下拉滚球失败信息");
                            }
                        }
                    });
                }
                mTask = new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshListView.onRefreshComplete();
                    }
                };
                UIUtils.postTaskDelay(mTask, 1000);
            }
        });
    }

    private void addData(List<ScoreBean> items) {
        mCount = mItems.size();
        mItems.addAll(items);
        if (mItems != null) {
            LogUtil.util(TAG, "总共有" + mItems.size() + "条item");
            mAdapter.notifyDataSetChanged();
            mPullToRefreshListView.getRefreshableView().setSelection(mCount);
            mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }


    private class GQRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constent.LOADING_FOOTBALL))
                getData();
            else
                getNetDataWork();
        }
    }
}
