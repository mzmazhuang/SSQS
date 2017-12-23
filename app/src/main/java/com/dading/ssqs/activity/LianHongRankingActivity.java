package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.LianHongAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LHRankingBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 10:13
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LianHongRankingActivity extends BaseActivity {

    private static final String TAG = "LianHongRankingActivity";
    @Bind(R.id.lianhong_lv)
    PullToRefreshListView mLianhongLv;
    @Bind(R.id.lianhong_no_data)
    ImageView mLianhongNoData;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private int mPage;
    private List<LHRankingBean> mData;
    private int mTotalPage;
    private LianHongAdapter mAdapter;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_lianhong_ranking;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.lianhong_ranking));
        /**
         * 4.	连红榜
         a)	请求地址：
         /v1.0/expert/recomm/hot/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         */
        mPage = 1;

        SSQSApplication.apiClient(classGuid).recommHotList(mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultLHRankPage page = (CcApiResult.ResultLHRankPage) result.getData();

                    if (page != null) {
                        mTotalPage = page.getTotalPage();

                        if (page.getItems() != null) {
                            mData = page.getItems();

                            if (mData.size() > 0) {
                                mLianhongNoData.setVisibility(View.GONE);
                            } else {
                                mLianhongNoData.setVisibility(View.VISIBLE);
                            }
                            processData(mData);
                        }
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(LianHongRankingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(List<LHRankingBean> data) {
        mAdapter = new LianHongAdapter(this, data);
        mLianhongLv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mLianhongLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳到推荐详情
                Intent intent = new Intent(LianHongRankingActivity.this, ReferInfosActivity.class);
                intent.putExtra(Constent.SAVANT_ID, mData.get(position).id);
                startActivity(intent);
            }
        });
        mLianhongLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mLianhongLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mLianhongLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            LianHongRankingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mLianhongLv.onRefreshComplete();
                                }
                            });
                        }
                    }, 1000);
                    return;
                }
                mPage++;

                SSQSApplication.apiClient(classGuid).recommHotList(mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            CcApiResult.ResultLHRankPage page = (CcApiResult.ResultLHRankPage) result.getData();

                            if (page != null) {
                                mTotalPage = page.getTotalCount();
                                if (page.getItems() != null) {
                                    mData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                    mLianhongLv.onRefreshComplete();
                                }
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(LianHongRankingActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
