package com.dading.ssqs.controllar.redpeople;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.RedPeopleSingleHLAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.RedPopleARBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 16:18
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RedPeopleHLost extends BaseFragnment {
    private static final String TAG = "RedPeopleHLost";

    @Bind(R.id.red_people_h_lost_lv)
    PullToRefreshListView mLv;
    @Bind(R.id.data_empty)
    RelativeLayout mNodata;

    private int mPage;
    private List<RedPopleARBean> mData;
    private int mTotalPage;
    private RedPeopleSingleHLAdapter mAdapter;
    private Timer mTimer;

    @Override
    protected int setLayout() {
        return R.layout.red_people_h_lost;
    }

    @Override
    public void initData() {
        /**
         /v1.0/expert/day/hot/{type}/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明：Type:赔率类型ID
         1-	全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
         auth_token：登陆后加入请求头
         */
        mPage = 1;

        SSQSApplication.apiClient(0).getHotDataPage(5, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultRedPoplePage page = (CcApiResult.ResultRedPoplePage) result.getData();

                    if (page != null) {
                        mTotalPage = page.getTotalCount();

                        if (page.getItems() != null) {
                            mData = page.getItems();

                            if (mData.size() > 0) {
                                mAdapter = new RedPeopleSingleHLAdapter(getActivity(), mData);
                                mLv.setAdapter(mAdapter);

                                mNodata.setVisibility(View.GONE);
                            } else {
                                mNodata.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "红人全场赛果失败信息");
                }
            }
        });
    }

    @Override
    public void initListener() {
        mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    mTimer = new Timer();
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

                SSQSApplication.apiClient(0).getHotDataPage(5, mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mLv.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultRedPoplePage page = (CcApiResult.ResultRedPoplePage) result.getData();

                            if (page != null) {
                                mPage = page.getTotalPage();
                                mTotalPage = page.getTotalCount();

                                if (page.getItems() != null) {
                                    mData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Logger.d(TAG, result.getMessage() + "红人半场让球失败信息");
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
