package com.dading.ssqs.controllar.sns;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/9 10:17
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class FiveLeaguesControllar {

    private static final String TAG = "HeadLineControllar";
    private Context context;
    public final View mRootView;

    private PullToRefreshListView mLv;
    private int mPage;
    private HeadLineAdapter mAdapter;

    private int totalCount;

    public FiveLeaguesControllar(Context context) {
        this.context = context;
        mRootView = initView();
        initData();
        initListener();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.hot_spot, null);
        mLv = (PullToRefreshListView) view.findViewById(R.id.lv_mm);

        mAdapter = new HeadLineAdapter(context);
        mLv.getRefreshableView().setAdapter(mAdapter);
        return view;
    }

    private void initData() {
        getData();
    }

    private void getData() {
        mPage = 1;

        SSQSApplication.apiClient(0).getFiveDataList(7, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultSnsPage page = (CcApiResult.ResultSnsPage) result.getData();

                    if (page != null) {
                        totalCount = page.getTotalCount();

                        if (page.getWrites() != null) {
                            mAdapter.setList(page.getWrites());
                        }
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "五大联赛失败信息");
                }
            }
        });
    }

    private void initListener() {
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mLv.onRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage < totalCount) {
                    loadMore();
                }
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mLv.onRefreshComplete();
                        TmtUtils.midToast(context, "没有更多数据!", 0);
                    }
                }, 1000);
            }
        });
    }

    private void loadMore() {
        mPage++;

        SSQSApplication.apiClient(0).getFiveDataList(7, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultSnsPage page = (CcApiResult.ResultSnsPage) result.getData();

                    if (page != null) {
                        totalCount = page.getTotalCount();

                        if (page.getWrites() != null) {
                            mAdapter.addList(page.getWrites());
                        }
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "五大联赛失败信息");
                }
            }
        });
    }
}
