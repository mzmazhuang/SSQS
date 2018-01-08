package com.dading.ssqs.controllar.savantrefer;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.ReferInfosActivity;
import com.dading.ssqs.adapter.SavantReferInfoAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.ReferInfoARBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
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
public class ReferInfoHLost {
    private static final String TAG = "RedPeopleHLost";
    private final Context context;
    private final String savantID;
    public View mRootView;
    private PullToRefreshListView mLv;
    private int mPage;
    private List<ReferInfoARBean> mData;
    private int mTotalPage;
    private SavantReferInfoAdapter mAdapter;
    private RelativeLayout mNodata;

    public ReferInfoHLost(Context context, String savantID) {
        this.context = context;
        this.savantID = savantID;
        mRootView = initView();
        initData();
        initListener();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.red_people_h_lost, null);
        mLv = ButterKnife.findById(view, R.id.red_people_h_lost_lv);
        mNodata = ButterKnife.findById(view, R.id.data_empty);
        return view;
    }

    private void initData() {
        /**
         6.	专家推荐列表
         a)	请求地址：
         /v1.0/recomm/type/{type}/userID/{userID}/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：赔率类型
         1-全场赛果2- 当前让球3-全场大小4-半场赛果5-半场让球6-半场大小
         userID:用户ID
         auth_token：登陆后加入请求头
         */
        mPage = 1;

        SSQSApplication.apiClient(0).getRecommExpert(5, savantID, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultReferInfoARPage page = (CcApiResult.ResultReferInfoARPage) result.getData();

                    if (page != null) {
                        mTotalPage = page.getTotalCount();

                        if (page.getItems() != null) {
                            mData = page.getItems();

                            if (mData.size() > 0) {
                                mAdapter = new SavantReferInfoAdapter(context, mData);
                                mLv.setAdapter(mAdapter);

                                mNodata.setVisibility(View.GONE);
                            } else {
                                mNodata.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                } else {
                    Logger.INSTANCE.d(TAG, result.getMessage() + "专家推荐列表全场赛果失败信息");
                }
            }
        });
    }

    private void initListener() {
        mLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((ReferInfosActivity) context).runOnUiThread(new Runnable() {
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

                SSQSApplication.apiClient(0).getRecommExpert(5, savantID, mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mLv.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultReferInfoARPage page = (CcApiResult.ResultReferInfoARPage) result.getData();

                            if (page != null) {
                                mPage = page.getTotalPage();
                                mTotalPage = page.getTotalCount();

                                if (page.getItems() != null) {
                                    mData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Logger.INSTANCE.d(TAG, result.getMessage() + "专家推荐列表半场让球失败信息");
                        }
                    }
                });
            }
        });
    }
}
