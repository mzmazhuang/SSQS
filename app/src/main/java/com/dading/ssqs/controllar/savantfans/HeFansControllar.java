package com.dading.ssqs.controllar.savantfans;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.FansInfoActivity;
import com.dading.ssqs.adapter.HeFollowAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.SavantFansBean;
import com.dading.ssqs.utils.ToastUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/9 15:44
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HeFansControllar {
    private static final String TAG = "HeFollowControllar";

    private Context context;
    private String savantId;
    public View mRootView;

    private int mPage;
    private PullToRefreshListView mHeFansLv;
    private int mTotalPage;
    private List<SavantFansBean> mData;
    private HeFollowAdapter mAdapter;

    public HeFansControllar(Context context, String savantId) {
        this.context = context;
        this.savantId = savantId;
        mRootView = initView();
        initData();
        initListener();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.he_fans, null);
        mHeFansLv = ButterKnife.findById(view, R.id.he_fans_lv);
        return view;
    }

    private void initData() {
        /**
         * 7.	专家/用户粉丝
         a)	请求地址：
         /v1.0/fouce/other/id/{userID}/type/{type}/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         userID:用户ID,
         type: 1:用户2：粉丝
         page:当前页数
         count:获取条数
         auth_token：登陆后加入请求头
         */

        mPage = 1;

        SSQSApplication.apiClient(0).getFansList(savantId, 2, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultFansPage page = (CcApiResult.ResultFansPage) result.getData();

                    if (page != null) {
                        mTotalPage = page.getTotalCount();

                        if (page.getItems() != null) {
                            mAdapter = new HeFollowAdapter(context, page.getItems());
                            mHeFansLv.setAdapter(mAdapter);
                        }
                    }
                } else {

                }
            }
        });
    }

    private void initListener() {
        mHeFansLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mHeFansLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mHeFansLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ((FansInfoActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mHeFansLv.onRefreshComplete();
                                    ToastUtils.midToast(context, "没有更多数据!", 0);
                                }
                            });
                        }
                    }, 1000);
                    return;
                }
                mPage++;

                SSQSApplication.apiClient(0).getExpertHotList(mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mHeFansLv.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultFansPage page = (CcApiResult.ResultFansPage) result.getData();

                            if (page != null) {
                                mPage = page.getTotalPage();
                                mTotalPage = page.getTotalCount();

                                if (page.getItems() != null) {
                                    mData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            mPage--;
                        }
                    }
                });
            }
        });
    }
}
