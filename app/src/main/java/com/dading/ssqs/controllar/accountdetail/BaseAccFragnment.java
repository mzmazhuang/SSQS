package com.dading.ssqs.controllar.accountdetail;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.AccountDetailAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.AccountDetailBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/9/13.
 */
public abstract class BaseAccFragnment extends BaseFragnment implements BaseQuickAdapter.RequestLoadMoreListener {
    private static final String TAG = "BaseAccFragnment";
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

    private boolean mRefreshing;
    public int mPage;
    private List<AccountDetailBean> mItems = new ArrayList<>();
    private AccountDetailAdapter mAdapter;
    private boolean mB = true;

    private int totalPage;

    @Override
    protected int setLayout() {
        return R.layout.templete_account_detail;
    }

    @Override
    public void initData() {
        mRefreshing = mSwipeRefreshLy.isRefreshing();
        if (mRefreshing)
            mSwipeRefreshLy.setRefreshing(false);
        mRecycleview.setEmptyView(mDataEmpty);
        /**
         21.	账号明细
         a)	请求地址：/v1.0/accountDetail/type/{type}/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明
         字段名	类型	长度	是否必填	备注
         auth_token	string		是	token
         type	int	1	是	类型1：全部  2：充值    3：提款    4：购彩    5：中奖    6：佣金提成  7：其他
         page	int		是	当前页数
         count	int		是	一页的条数
         */
        mPage = 1;
        getData(mPage, getType(), getLimit());
    }

    private void getData(int page, int type, int limit) {
        if (page == 1) {
            mLoadingAnimal.setVisibility(View.VISIBLE);
        }

        SSQSApplication.apiClient(0).getAccount(type, page, limit, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (mLoadingAnimal != null) {
                    mLoadingAnimal.setVisibility(View.GONE);
                }

                setVisiable(false);
                if (result.isOk()) {
                    CcApiResult.ResultAccountDetailsPage page = (CcApiResult.ResultAccountDetailsPage) result.getData();

                    if (page != null) {
                        totalPage = page.getTotalPage();

                        mB = page.getTotalPage() < page.getTotalCount();
                        if (page.getItems() != null) {
                            processedData(page.getItems());
                        }
                    }
                } else {
                    mLoadingAnimal.setVisibility(View.GONE);
                    ToastUtils.midToast(mContent, result.getMessage(), 0);
                    setVisiable(true);
                }
            }
        });
    }

    public abstract int getType();

    public abstract int getPage();

    public abstract int getLimit();

    @Override
    public void initListener() {
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

    private void setVisiable(boolean b) {
        if (mShowNetworkErr != null) {
            mShowNetworkErr.setVisibility(b ? View.VISIBLE : View.GONE);
        }
    }

    private void processedData(List<AccountDetailBean> bean) {
        if (mB)
            mPage++;
        if (totalPage == 1 && mRecycleview != null) {
            mRecycleview.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
            mItems = bean;
            mAdapter = new AccountDetailAdapter(R.layout.account_detail_item, mItems);
            mRecycleview.setAdapter(mAdapter);

            mAdapter.openLoadMore(mPage, mB);
            mAdapter.setLoadingView(mViewLoad);
            mAdapter.setOnLoadMoreListener(this);
        } else {
            if (mAdapter != null)
                mItems.addAll(bean);
        }
        if (mAdapter != null)
            mAdapter.notifyDataChangedAfterLoadMore(mB);
    }

    @OnClick({R.id.show_network_err_refresh})
    public void OnClik(View v) {
        initData();
    }

    @Override
    public void onLoadMoreRequested() {
        getData(mPage, getType(), getLimit());
    }
}
