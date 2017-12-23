package com.dading.ssqs.controllar.recharge;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.RechargeFragmentDetailAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.RechargeDetailBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public abstract class BaseRechargeFragnment extends BaseFragnment {
    String TAG = "BaseRechargeFragnment";
    @Bind(R.id.recycleview)
    EmptyRecyclerView mRecycleview;
    @Bind(R.id.swipe_refresh_ly)
    SwipeRefreshLayout mSwipeRefreshLy;

    @Bind(R.id.data_empty)
    RelativeLayout mDataEmpty;

    @Bind(R.id.show_network_err)
    LinearLayout mShowNetworkErr;
    public int mPage;
    @Bind(R.id.loading_animal)
    LinearLayout mLoadingAnimal;
    private List<RechargeDetailBean> mItems = new ArrayList<>();
    private RechargeFragmentDetailAdapter mAdapter;
    private boolean mB = true;

    @Override
    protected int setLayout() {
        return R.layout.fragment_recycle_view;
    }

    @Override
    public void initData() {
        super.initData();
        mRecycleview.setEmptyView(mDataEmpty);
        mPage = 1;
        volleyGet(getType(), getPage(), getLimit());
    }

    public abstract int getType();

    public abstract int getPage();

    public abstract int getLimit();

    private void volleyGet(int type, int page, int limit) {
        if (mPage == 1) {
            mLoadingAnimal.setVisibility(View.VISIBLE);
        }

        SSQSApplication.apiClient(0).getChargeHistoryList(type, page, limit, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mLoadingAnimal.setVisibility(View.GONE);
                setVisiable(false);

                if (result.isOk()) {
                    CcApiResult.ResultChargeHistoryPage resultChargeHistoryPage = (CcApiResult.ResultChargeHistoryPage) result.getData();

                    if (resultChargeHistoryPage != null && resultChargeHistoryPage.getItems() != null) {
                        processData(resultChargeHistoryPage.getItems(), resultChargeHistoryPage.getTotalCount(), resultChargeHistoryPage.getTotalPage());
                    }
                } else {
                    if (mPage == 1) {
                        setVisiable(true);
                    }
                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);

                }
            }
        });
    }

    private void processData(List<RechargeDetailBean> bean, int count, int page) {
        LogUtil.util(TAG, "是否更多------------------------------:" + mB);
        mB = page < count;
        if (mB)
            mPage++;

        if (page == 1) {
            mRecycleview.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
            mItems = bean;
            mAdapter = new RechargeFragmentDetailAdapter(R.layout.account_detail_item, mItems);
            //mAdapter = new AccountDetailAdapter2(mContent, bean.getItems( ));
            mRecycleview.setAdapter(mAdapter);

            mAdapter.openLoadMore(mPage, mB);
            mAdapter.setLoadingView(mViewLoad);

            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    volleyGet(getType(), getPage(), getLimit());
                }
            });
        } else {
            if (mAdapter != null)
                mItems.addAll(bean);
        }
        if (mAdapter != null)
            mAdapter.notifyDataChangedAfterLoadMore(mB);
    }

    private void setVisiable(boolean b) {
        mShowNetworkErr.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initListener() {
        super.initListener();
        mSwipeRefreshLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                UIUtils.getMainThreadHandler().postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLy.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }

    @OnClick({R.id.show_network_err_refresh})
    public void OnClik(View v) {
        initData();
    }
}
