package com.dading.ssqs.controllar.mycollect;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.adapter.MyFTAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MyTzBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/22 10:28
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyCollectControllar extends BaseFragnment {
    private static final String TAG = "MyCollectControllar";
    @Bind(R.id.p_lv)
    PullToRefreshListView mMatchColloctLv;

    @Bind(R.id.data_empty)
    RelativeLayout mMatchColloctNoData;
    public View mRootView;
    private int mPage;
    private String mUrl;
    private int mTotalPage;
    private MyFTAdapter mAdapter;
    private List<MyTzBean> mData;
    private Runnable mTask;


    @Override
    protected int setLayout() {
        return R.layout.my_collect;
    }

    @Override
    public void initData() {
        mMatchColloctLv.setEmptyView(mMatchColloctNoData);
        /**
         * 8.	我的收藏（文章）
         a)	请求地址：/v1.0/fouceArticle/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明：page:当前页数 count:条数 auth_token：登陆后加入请求头
         */
        mPage = 1;

        SSQSApplication.apiClient(0).getFouceArticleList(mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMyTzPage page = (CcApiResult.ResultMyTzPage) result.getData();

                    if (page != null) {
                        mTotalPage = page.getTotalCount();

                        if (page.getItems() != null) {
                            mData = page.getItems();

                            mAdapter = new MyFTAdapter(getActivity(), mData);
                            mMatchColloctLv.setAdapter(mAdapter);
                        }
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    public void initListener() {
        mMatchColloctLv.setMode(PullToRefreshBase.Mode.BOTH);
        mMatchColloctLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //关闭下拉刷新的效果
                mTask = new Runnable() {
                    @Override
                    public void run() {
                        SSQSApplication.apiClient(0).getFouceArticleList(1, 10, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                //关闭下拉刷新的效果
                                mMatchColloctLv.onRefreshComplete();

                                if (result.isOk()) {
                                    CcApiResult.ResultMyTzPage page = (CcApiResult.ResultMyTzPage) result.getData();

                                    if (page != null) {

                                        if (page.getItems() != null) {
                                            mPage = 1;

                                            mAdapter = new MyFTAdapter(getActivity(), page.getItems());
                                            mMatchColloctLv.setAdapter(mAdapter);
                                        }
                                    }
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
                                    } else {
                                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                    }
                                }
                            }
                        });
                    }
                };
                UIUtils.postTaskDelay(mTask, 3000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    mPage++;

                    SSQSApplication.apiClient(0).getFouceArticleList(mPage, 10, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                CcApiResult.ResultMyTzPage page = (CcApiResult.ResultMyTzPage) result.getData();

                                if (page != null) {
                                    mTotalPage = page.getTotalPage();

                                    if (page.getItems() != null) {
                                        mData.addAll(page.getItems());
                                        mAdapter.notifyDataSetChanged();
                                        mMatchColloctLv.onRefreshComplete();
                                    }
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                }
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMatchColloctLv.onRefreshComplete();
                                if (mPage >= mTotalPage)
                                    ToastUtils.midToast(getActivity(), "没有更多数据!", 0);
                            }
                        });
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.removeTask(mTask);
    }
}
