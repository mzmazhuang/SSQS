package com.dading.ssqs.controllar.scores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.adapter.ScoreMatchAdapterJS;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseScoreControllar;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/5 17:07
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GZScoreControllar extends BaseScoreControllar {

    private static final String TAG = "GZScoreControllar";
    private PullToRefreshListView mGZList;
    private ScoreMatchAdapterJS mAdapter;
    private List<ScoreBean> mItems;
    public GZRecevice mRecevice;
    private RelativeLayout mEmpty;
    private LinearLayout mLoadingAnimal;
    private Runnable mTask;


    @Override
    public void initScorecalendar() {
        mScoreWeekLayout.setVisibility(View.GONE);
    }

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(mContent, R.layout.scorepager_gz, null);
        mGZList = (PullToRefreshListView) view.findViewById(R.id.score_vp_gz);
        mEmpty = (RelativeLayout) view.findViewById(R.id.data_empty);
        mLoadingAnimal = (LinearLayout) view.findViewById(R.id.loading_anim);
        //mLoadingAnimal = (LinearLayout) view.findViewById(R.id.loading_animal);
        mGZList.getRefreshableView().setEmptyView(mEmpty);

        mAdapter = new ScoreMatchAdapterJS(mContent, 3);
        mGZList.setAdapter(mAdapter);

        mGZList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mGZList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //关闭下拉刷新的效果
                mTask = new Runnable() {
                    @Override
                    public void run() {
                        boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

                        SSQSApplication.apiClient(0).getMatchBallOrTypeList(b, 5, "2016082200:00:00", "0", 0, "0", 1, 1000, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                mGZList.onRefreshComplete();

                                if (result.isOk()) {
                                    CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                                    if (page != null) {

                                        if (page.getItems() != null) {
                                            mItems = page.getItems();

                                            mAdapter.setData(mItems);
                                        }
                                    }
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(mContent, LoginActivity.class);
                                        mContent.startActivity(intent);
                                    } else {
                                        ToastUtils.midToast(mContent, result.getMessage(), 0);
                                    }
                                }
                            }
                        });
                        //关闭下拉刷新的效果
                    }
                };
                UIUtils.postTaskDelay(mTask, 3000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mGZList.onRefreshComplete();
            }
        });

        mRecevice = new GZRecevice();

        UIUtils.ReRecevice(mRecevice, Constent.GZ_RECEVICE);
        UIUtils.ReRecevice(mRecevice, Constent.LOADING_FOOTBALL_SCORE);
        return view;
    }

    private boolean hasInit = false;

    public void init() {
        if (!hasInit) {
            hasInit = true;

            boolean b = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

            mLoadingAnimal.setVisibility(View.VISIBLE);
            SSQSApplication.apiClient(0).getMatchBallOrTypeList(b, 5, "2016082200:00:00", "0", 0, "0", 1, 1000, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    mLoadingAnimal.setVisibility(View.GONE);

                    if (result.isOk()) {
                        CcApiResult.ResultScorePage page = (CcApiResult.ResultScorePage) result.getData();

                        if (page != null) {

                            if (page.getItems() != null) {
                                mItems = page.getItems();

                                mAdapter.setData(mItems);
                            }
                        }
                    } else {
                        ToastUtils.midToast(mContent, result.getMessage(), 0);
                    }
                }
            });
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
        UIUtils.removeTask(mTask);
    }

    private class GZRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
}
