package com.dading.ssqs.controllar.myrefer;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyReferrSixAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.MyReferBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 16:18
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyReferHLost extends BaseFragnment {
    private static final String TAG = "RedPeopleAllResult";
    private Timer mTimer;
    public View mRootView;
    @Bind(R.id.refer_lv)
    PullToRefreshListView mLv;
    @Bind(R.id.data_empty)
    RelativeLayout mNodata;

    private List<MyReferBean> mData;
    private int mPage;
    private MyReferrSixAdapter mAdapter;
    private int mTotalPage;

    @Override
    protected int setLayout() {
        mTimer = new Timer();
        return R.layout.refer_vp_1;
    }

    public void initData() {
        mLv.setEmptyView(mNodata);
        /**
         * /v1.0/recomm/type/{type}/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明：type：赔率类型
         1-当前让球2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
         auth_token：登陆后加入请求头

         /v1.0/recomm/type/{type}/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明：type：赔率类型
         1-全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
         auth_token：登陆后加入请求头
         */
        getData();

    }

    private void getData() {
        mPage = 1;

        SSQSApplication.apiClient(0).getRecommPage(5, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMyReferPage page = (CcApiResult.ResultMyReferPage) result.getData();

                    if (page != null) {
                        mPage = page.getTotalPage();
                        mTotalPage = page.getTotalCount();

                        if (page.getItems() != null) {
                            mData = page.getItems();
                            mAdapter = new MyReferrSixAdapter(getActivity(), mData);
                            mLv.setAdapter(mAdapter);
                        }
                    }
                } else {
                    Logger.INSTANCE.d(TAG, result.getMessage() + "我的推荐失败信息");
                }
            }
        });
    }

    @Override
    public void initListener() {
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mLv.onRefreshComplete();
                            }
                        });
                    }
                }, 1000);
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLv.onRefreshComplete();
                                }
                            });
                        }
                    }, 1000);
                    return;
                }
                mPage++;

                SSQSApplication.apiClient(0).getRecommPage(5, mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mLv.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultMyReferPage page = (CcApiResult.ResultMyReferPage) result.getData();

                            if (page != null) {
                                mPage = page.getTotalPage();
                                mTotalPage = page.getTotalCount();

                                if (page.getItems() != null) {
                                    mData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Logger.INSTANCE.d(TAG, result.getMessage() + "我的推荐赛果失败信息");
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        if (mTimer != null)
            mTimer.cancel();
    }
}
