package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.GuessballAceAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GuessACEBean;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 10:13
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GuessBallACEActivity extends BaseActivity {

    private static final String TAG = "GuessBallACEActivity";
    @Bind(R.id.guessball_ace_lv)
    PullToRefreshListView mGuessballAceLv;
    @Bind(R.id.guessball_ace_no_data)
    ImageView mGuessballAceNoData;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private int mPage;
    private List<GuessACEBean> mData;
    private int mTotalPage;
    private GuessballAceAdapter mAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_guessball_ace;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.guessball_ace));
        /**
         * 5.竞彩高手
         a)	请求地址：
         /v1.0/expert/guess/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         */
        mPage = 1;

        SSQSApplication.apiClient(classGuid).expertGuessList(mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultExpertGuessPage page = (CcApiResult.ResultExpertGuessPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        mData = page.getItems();
                        mTotalPage = page.getTotalCount();
                        if (mData.size() <= 0) {
                            mGuessballAceNoData.setVisibility(View.VISIBLE);
                        } else {
                            mGuessballAceNoData.setVisibility(View.GONE);
                        }
                        processData(mData);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(GuessBallACEActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(List<GuessACEBean> data) {

        mAdapter = new GuessballAceAdapter(this, data);
        mGuessballAceLv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mGuessballAceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(GuessBallACEActivity.this, ReferInfosActivity.class);
                intent.putExtra(Constent.SAVANT_ID, mData.get(position).id);
                startActivity(intent);
            }
        });
        mGuessballAceLv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mGuessballAceLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mGuessballAceLv.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage >= mTotalPage) {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            GuessBallACEActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGuessballAceLv.onRefreshComplete();
                                }
                            });
                        }
                    }, 1000);
                    return;
                }
                mPage++;

                SSQSApplication.apiClient(classGuid).expertGuessList(mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            CcApiResult.ResultExpertGuessPage page = (CcApiResult.ResultExpertGuessPage) result.getData();

                            if (page != null) {
                                mTotalPage = page.getTotalCount();

                                if (page.getItems() != null) {
                                    mData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                    mGuessballAceLv.onRefreshComplete();
                                }
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(GuessBallACEActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
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
