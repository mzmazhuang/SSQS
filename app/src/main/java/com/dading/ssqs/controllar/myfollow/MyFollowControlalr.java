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
import com.dading.ssqs.adapter.MyFollowListAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MyFollowBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/5 17:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyFollowControlalr extends Fragment {
    private static final String TAG = "MyFollowControlalr";
    private Context context;
    public View mRootView;
    private PullToRefreshListView mList;
    private RelativeLayout mEmpty;
    private int mPage;
    private int mTotalPage;
    private MyFollowListAdapter mAdapterUser;
    public MyFollowCbRecevice mRecevice;

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
        View view = View.inflate(context, R.layout.my_follow, null);
        mList = ButterKnife.findById(view, R.id.my_follow_list1);
        mEmpty = ButterKnife.findById(view, R.id.data_empty);
        return view;
    }

    private void initData() {
        mRecevice = new MyFollowCbRecevice();

        UIUtils.ReRecevice(mRecevice, Constent.MY_FOLLOW_TAG);

        mList.setEmptyView(mEmpty);
        /**
         * 1.	我的关注
         /v1.0/fouce/list/type/{type}/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         type: 1:用户2：粉丝
         page:当前页数
         count:获取条数
         */
        mPage = 1;
        getData();
    }

    private void getData() {
        SSQSApplication.apiClient(0).getMyFansList(1, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMyFansPage page = (CcApiResult.ResultMyFansPage) result.getData();

                    if (page != null) {
                        mPage = page.getTotalPage();
                        mTotalPage = page.getTotalCount();

                        if (page.getItems() != null) {
                            mAdapterUser = new MyFollowListAdapter(context, page.getItems(), 1);
                            mList.setAdapter(mAdapterUser);
                        }
                    }
                } else {
                    ToastUtils.midToast(context, "服务器开小差了,请重新刷新!", 0);
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
                if (mPage >= mTotalPage) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((MyCircleFollowActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.midToast(context, "没有更多数据!", 0);
                                    mList.onRefreshComplete();
                                }
                            });
                        }
                    }, 1000);
                    return;
                }
                loadData();
            }
        });
    }

    private void loadData() {
        mList.setMode(PullToRefreshBase.Mode.DISABLED);
        mPage++;
        loadData2(mPage);
    }

    private void loadData2(int page) {
        SSQSApplication.apiClient(0).getMyFansList(1, page, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mList.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                mList.onRefreshComplete();

                if (result.isOk()) {
                    CcApiResult.ResultMyFansPage resultMyFansPage = (CcApiResult.ResultMyFansPage) result.getData();

                    if (resultMyFansPage != null) {
                        mTotalPage = resultMyFansPage.getTotalCount();
                        mPage = resultMyFansPage.getTotalPage();

                        if (resultMyFansPage.getItems() != null) {
                            List<MyFollowBean> data = mAdapterUser.getData();
                            int size = data.size();
                            data.addAll(resultMyFansPage.getItems());
                            mList.getRefreshableView().smoothScrollToPosition(size);
                            mAdapterUser.notifyDataSetChanged();
                        }
                    }
                } else {
                    Logger.INSTANCE.d(TAG, result.getMessage() + "我的关注失败信息");
                }
            }
        });
    }

    private class MyFollowCbRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            for (int i = 1; i <= mPage; i++) {
                if (i == 1) {
                    getData();
                } else if (i > 1) {
                    loadData2(i);
                }
            }
        }
    }
}
