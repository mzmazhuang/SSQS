package com.dading.ssqs.controllar.myfollow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.MyCircleFollowActivity;
import com.dading.ssqs.adapter.MyFollowListFansAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MyFollowBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/5 17:23
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyFansControlalr extends Fragment {
    private static final String TAG = "MyFansControlalr";
    private Context context;
    public View mRootView;
    private PullToRefreshListView mList;
    private int mPage;
    private RelativeLayout mEmpty;
    private int totalCount;
    private MyFollowListFansAdapter mAdapterFans;
    public MyFansCbRecevice mRecevice;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getActivity();
        mRootView = initView();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        UIUtils.UnReRecevice(mRecevice);
    }

    private View initView() {
        View view = View.inflate(context, R.layout.my_fans, null);
        mList = ButterKnife.findById(view, R.id.my_fans_list1);
        mEmpty = ButterKnife.findById(view, R.id.data_empty);
        return view;
    }

    private void initData() {
        mRecevice = new MyFansCbRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.MY_FANS_TAG);
        mList.setEmptyView(mEmpty);

        mPage = 1;
        getData();
    }

    private void getData() {

        SSQSApplication.apiClient(0).getMyFansList(2, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMyFansPage page = (CcApiResult.ResultMyFansPage) result.getData();

                    if (page != null) {
                        totalCount = page.getTotalCount();
                        mPage = page.getTotalPage();

                        if (page.getItems() != null) {
                            mAdapterFans = new MyFollowListFansAdapter(context, page.getItems(), 2);
                            mList.setAdapter(mAdapterFans);
                        }
                    }
                } else {
                    TmtUtils.midToast(context, "服务器开小差了,请重新刷新!", 0);
                }
            }
        });
    }

    private void initListener() {
        mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mList.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= totalCount) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((MyCircleFollowActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TmtUtils.midToast(context, "没有更多数据!", 0);
                                    mList.onRefreshComplete();
                                }
                            });
                        }
                    }, 1000);
                } else {
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        mList.setMode(PullToRefreshBase.Mode.DISABLED);
        mPage++;
        loadMore2(mPage);

    }

    private void loadMore2(int page) {

        SSQSApplication.apiClient(0).getMyFansList(2, page, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                mList.onRefreshComplete();

                if (result.isOk()) {
                    CcApiResult.ResultMyFansPage resultMyFansPage = (CcApiResult.ResultMyFansPage) result.getData();

                    if (resultMyFansPage != null) {
                        totalCount = resultMyFansPage.getTotalCount();
                        mPage = resultMyFansPage.getTotalPage();

                        if (resultMyFansPage.getItems() != null) {
                            List<MyFollowBean> data = mAdapterFans.getData();
                            int size = data.size();
                            data.addAll(resultMyFansPage.getItems());
                            mList.getRefreshableView().smoothScrollToPosition(size);
                            mAdapterFans.notifyDataSetChanged();
                            mList.onRefreshComplete();
                        }
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "我的关注失败信息");
                }
            }
        });
    }

    private class MyFansCbRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            for (int i = 1; i <= mPage; i++) {
                if (i == 1) {
                    getData();
                } else if (i > 1) {

                    loadMore2(i);
                }
            }
        }
    }
}
