package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyReferrLvAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.ReferReferBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import butterknife.ButterKnife;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/14 17:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoTj {
    private static final String TAG = "MatchInfoTj";
    private final Context context;
    public final View mRootView;
    private final int matchId;
    private PullToRefreshListView mDataLv;
    private RelativeLayout mNoDataView;
    private ListView mLv;

    public MatchInfoTj(Context context, int matchId) {
        this.context = context;
        this.matchId = matchId;
        mRootView = initView(context);
        initData();
    }

    private void initData() {
        /**
         a)	请求地址：
         /v1.0/recomm/matchID/{matchID}/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         matchID：比赛ID
         page:当前页数
         count:显示条数
         auth_token：登陆后加入请求头
         */

        SSQSApplication.apiClient(0).getRecommMatchList(matchId, 1, 100, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultReferReferPage page = (CcApiResult.ResultReferReferPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        processData(page.getItems());
                    }
                } else {
                    ToastUtils.midToast(UIUtils.getContext(), "比赛详情推荐" + result.getMessage(), 0);
                    Logger.INSTANCE.d(TAG, result.getMessage() + "失败信息");
                }
            }
        });
    }

    private void processData(List<ReferReferBean> data) {
        if (data != null && data.size() != 0) {
            mNoDataView.setVisibility(View.GONE);
            mDataLv.setVisibility(View.VISIBLE);
            mDataLv.setAdapter(new MyReferrLvAdapter(context, data));
        } else {
            mNoDataView.setVisibility(View.VISIBLE);
            mDataLv.setVisibility(View.GONE);
        }
    }
    //mDataLv.setAdapter(new MyReferrLvAdapter());

    private View initView(Context context) {
        View view = View.inflate(context, R.layout.lv_referr, null);
        mNoDataView = ButterKnife.findById(view, R.id.data_empty);
        mDataLv = ButterKnife.findById(view, R.id.tj_data_lv);
        View footview = View.inflate(context, R.layout.pl_footview, null);
        mLv = mDataLv.getRefreshableView();
        mLv.addFooterView(footview);
        return view;
    }

}
