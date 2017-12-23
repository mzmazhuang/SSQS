package com.dading.ssqs.controllar.withdrawdetail;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.WithDrawDetailBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/9/13.
 */
public abstract class BaseWithDrawDetailFrament extends BaseFragnment {

    String TAG = "BaseWithDrawFragnment";
    @Bind(R.id.recycleview)
    EmptyRecyclerView mRecycleview;
    @Bind(R.id.swipe_refresh_ly)
    SwipeRefreshLayout mSwipeRefreshLy;

    @Bind(R.id.data_empty)
    RelativeLayout mDataEmpty;

    @Bind(R.id.show_network_err)
    LinearLayout mShowNetworkErr;

    @Bind(R.id.loading_animal)
    LinearLayout mLoadingAnimal;

    public int mPage;
    private List<WithDrawDetailBean> mItems = new ArrayList<>();
    private WithDrawFragmentDetailAdapter mAdapter;
    private boolean mB = true;

    @Override
    protected int setLayout() {
        return R.layout.fragment_recycle_view;
    }

    @Override
    public void initData() {
        super.initData();
        mPage = 1;
        mRecycleview.setEmptyView(mDataEmpty);
        volleyGet(getType(), getPgae(), getLimit());
    }

    public abstract int getType();

    public abstract int getPgae();

    public abstract int getLimit();

    private void volleyGet(int type, int offset, int limit) {
        if (mPage == 1)
            mLoadingAnimal.setVisibility(View.VISIBLE);

        SSQSApplication.apiClient(0).withDrawalsHistory(type, offset, limit, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mShowNetworkErr.setVisibility(View.GONE);
                mLoadingAnimal.setVisibility(View.GONE);

                if (result.isOk()) {
                    CcApiResult.ResultWithDrawDetailBeanPage page = (CcApiResult.ResultWithDrawDetailBeanPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        mB = page.getTotalPage() < page.getTotalCount();
                        if (mB)
                            mPage++;

                        processData(page.getItems(), page.getTotalPage());
                    }
                } else {
                    Logger.d(TAG, "返回错误数据是------------------------------:" + result.getMessage());
                    mLoadingAnimal.setVisibility(View.GONE);
                    mShowNetworkErr.setVisibility(View.VISIBLE);
                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);

                }
            }
        });
    }

    private void processData(List<WithDrawDetailBean> bean, int totalPage) {
        Logger.d(TAG, "是否更多------------------------------:" + mB);

        if (totalPage == 1) {
            mRecycleview.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
            mItems = bean;
            mAdapter = new WithDrawFragmentDetailAdapter(R.layout.account_detail_item, mItems);
            //mAdapter = new AccountDetailAdapter2(mContent, bean.getItems( ));
            mRecycleview.setAdapter(mAdapter);
            mAdapter.openLoadMore(mPage, mB);
            mAdapter.setLoadingView(mViewLoad);

            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    volleyGet(getType(), getPgae(), getLimit());
                }
            });
        } else {
            if (mAdapter != null)
                mItems.addAll(bean);
        }
        if (mAdapter != null)
            mAdapter.notifyDataChangedAfterLoadMore(mB);
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
