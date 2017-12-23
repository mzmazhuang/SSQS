package com.dading.ssqs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.TodayTopicAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.TodayTopicBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/6 9:50
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ToadyTopicActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ToadyTopicActivity";
    @Bind(R.id.today_topic_list)
    PullToRefreshListView mTodayTopicList;
    @Bind(R.id.data_empty)
    RelativeLayout noData;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTodayTopicDelet;

    private TodayTopicAdapter mAdapter;
    private View mPopView;
    private TextView mToLook;
    private TextView mClearOnce;
    private PopupWindow mPop;
    private View mView;
    private int mPage;
    private List<TodayTopicBean> mDataItems;

    private int totalCount;

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.activity_today_topic, null);

        mPopView = View.inflate(this, R.layout.delete_pop_view, null);
        mToLook = (TextView) mPopView.findViewById(R.id.clear_pop_look);
        mClearOnce = (TextView) mPopView.findViewById(R.id.clear_pop_authentication);

        mPop = PopUtil.popuMake(mPopView);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_today_topic;
    }


    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.today_topic));
        mTodayTopicDelet.setImageResource(R.mipmap.clearbtn);
        mTodayTopicDelet.setVisibility(View.VISIBLE);

        mDataItems = new ArrayList<>();
        mTodayTopicList.setEmptyView(noData);

        /**
         * 2.	获取我的信息列表
         e)	请求地址：
         /v1.0/msg/list/page/{page}/count/{count}
         f)	请求方式:
         get
         g)	请求参数说明：
         page:当前页数
         count:条数
         auth_token：登陆后加入请求头
         */
        mPage = 1;

        SSQSApplication.apiClient(classGuid).getMyMsgList(mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultTodayTopicPage page = (CcApiResult.ResultTodayTopicPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        mDataItems = page.getItems();
                        totalCount = page.getTotalCount();
                        processData(mDataItems);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(ToadyTopicActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(List<TodayTopicBean> data) {
        if (data != null) {
            mAdapter = new TodayTopicAdapter(this, data);
            mTodayTopicList.setAdapter(mAdapter);
        }
    }

    @OnClick({R.id.top_back, R.id.top_icon})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.top_icon:
                Logger.d(TAG, "点击删除------------------------------");
                mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                mTodayTopicDelet.setClickable(false);

                break;
            default:
                break;
        }
    }

    @Override
    protected void initListener() {
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTodayTopicDelet.setClickable(true);
            }
        });
        mTodayTopicList.setMode(PullToRefreshBase.Mode.BOTH);
        mTodayTopicList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;

                        SSQSApplication.apiClient(classGuid).getMyMsgList(1, 10, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    CcApiResult.ResultTodayTopicPage page = (CcApiResult.ResultTodayTopicPage) result.getData();

                                    if (page != null && page.getItems() != null) {
                                        mDataItems = page.getItems();
                                        totalCount = page.getTotalCount();
                                        processData(mDataItems);
                                    }
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(ToadyTopicActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                    }
                                }
                            }
                        });
                        mTodayTopicList.onRefreshComplete();
                    }
                }, 500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mPage++;
                        Logger.d(TAG, "这是第几页--------------:" + mPage);
                        if (mPage > totalCount) {
                            TmtUtils.midToast(UIUtils.getContext(), "已全部加载,无更多数据!", 0);
                        } else {
                            SSQSApplication.apiClient(classGuid).getMyMsgList(mPage, 10, new CcApiClient.OnCcListener() {
                                @Override
                                public void onResponse(CcApiResult result) {
                                    if (result.isOk()) {
                                        CcApiResult.ResultTodayTopicPage page = (CcApiResult.ResultTodayTopicPage) result.getData();

                                        if (page != null && page.getItems() != null) {
                                            addData(page.getItems());
                                        }
                                    } else {
                                        if (403 == result.getErrno()) {
                                            UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                            Intent intent = new Intent(ToadyTopicActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                        }
                                    }
                                }
                            });
                        }
                        //关闭上啦加载的效果
                        mTodayTopicList.onRefreshComplete();
                    }
                }, 500);
            }
        });
        mToLook.setOnClickListener(this);
        mClearOnce.setOnClickListener(this);
    }

    public void addData(List<TodayTopicBean> items) {
        int size = mDataItems.size();
        mDataItems.addAll(items);
        if (mDataItems != null)
            Logger.d(TAG, "总共有" + mDataItems.size() + "条item");
        mAdapter.notifyDataSetChanged();
        mTodayTopicList.getRefreshableView().smoothScrollToPosition(size);
        mTodayTopicList.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_pop_look:
                mPop.dismiss();
                break;
            case R.id.clear_pop_authentication:
                /**
                 *5.	清空信息
                 e)	请求地址：
                 /v1.0/msg/clear
                 f)	请求方式:
                 get
                 g)	请求参数说明：
                 auth_token：登陆后加入请求头
                 */

                SSQSApplication.apiClient(classGuid).clearMsg(new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            if (mDataItems != null) {
                                mDataItems.clear();
                                mAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(ToadyTopicActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                mPop.dismiss();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
