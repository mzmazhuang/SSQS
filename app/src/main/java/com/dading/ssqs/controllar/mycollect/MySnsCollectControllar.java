package com.dading.ssqs.controllar.mycollect;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.SNSCollectAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.SNSCollectBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/22 11:40
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MySnsCollectControllar extends BaseFragnment {
    private static final String TAG = "MySnsCollectControllar";
    @Bind(R.id.p_lv)
    PullToRefreshListView mPLv;
    @Bind(R.id.data_empty)
    RelativeLayout mWenzhangNoData;
    public View mRootView;
    private int mPage;
    private int mCount;
    private List<SNSCollectBean> mData;
    private SNSCollectAdapter mAdapter;
    private Timer mTimer;

    private int totalCount;
    private int totalPage;


    @Override
    protected int setLayout() {
        return R.layout.sns_collect;
    }

    @Override
    public void initData() {
        mPLv.setEmptyView(mWenzhangNoData);
        mPLv.setMode(PullToRefreshBase.Mode.BOTH);
        /**
         * 13.	社区我的收藏
         1)	请求地址：
         /v1.0/wCollect/page/{page}/count/{count}
         2)	请求方式:
         get
         3)	请求参数说明：
         字段名	类型	长度	是否必填	备注
         page	int		是	当前页码
         count	int		是	记录条数
         auth_token	String		是	token（放在头部）
         */
        mPage = 1;
        mCount = 10;
        getData();
    }

    private void getData() {

        SSQSApplication.apiClient(0).getWcollectList(1, mCount, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultSNSCollectPage page = (CcApiResult.ResultSNSCollectPage) result.getData();

                    if (page != null) {
                        mPage = page.getTotalPage();

                        totalPage = page.getTotalPage();
                        totalCount = page.getTotalCount();

                        if (page.getItems() != null) {
                            mData = page.getItems();
                            mAdapter = new SNSCollectAdapter(mContent, mData);
                            mPLv.setAdapter(mAdapter);
                        }
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "社區收藏失败信息");
                }
            }
        });
    }

    @Override
    public void initListener() {
        mPLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPLv.onRefreshComplete();
                            }
                        });
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getMoreData();
            }
        });
    }

    private void getMoreData() {
        if (totalPage < totalCount) {
            mPage++;

            SSQSApplication.apiClient(0).getWcollectList(mPage, 10, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {
                        CcApiResult.ResultSNSCollectPage page = (CcApiResult.ResultSNSCollectPage) result.getData();

                        if (page != null) {

                            if (page.getItems() != null) {
                                mCount = mCount + 10;
                                mData.addAll(page.getItems());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mAdapter != null) {
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        LogUtil.util(TAG, result.getMessage() + "社區收藏失败信息");
                    }
                }
            });
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPLv.onRefreshComplete();
                        if (totalPage >= totalCount)
                            TmtUtils.midToast(mContent, "没有更多数据!", 0);
                    }
                });
            }
        }, 1000);
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        if (mTimer != null)
            mTimer.cancel();
    }
}
