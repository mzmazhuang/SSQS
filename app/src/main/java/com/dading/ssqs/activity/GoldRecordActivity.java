package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyRecordAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.DiamondsGlodRecordBean;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/26 10:22
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GoldRecordActivity extends BaseActivity implements View.OnTouchListener {
    private static final String TAG = "GoldRecordActivity";
    @Bind(R.id.glod_record_lv)
    ListView mGlodRecordLv;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    private int mPage = 1;
    private View mViewHead;
    private View mViewFoot;
    private boolean mIsLastRow;
    private float mDownX;
    private float mDownY;
    private float mMoveX;
    private float mMoveY;
    private ArrayList<DiamondsGlodRecordBean> mMItemsData;
    private MyRecordAdapter mAdapter;

    @Override
    protected void initView() {
        mViewHead = View.inflate(this, R.layout.activity_gold_head, null);
        mViewFoot = View.inflate(this, R.layout.activity_gold_foot, null);
        mGlodRecordLv.addHeaderView(mViewHead);
        mGlodRecordLv.addFooterView(mViewFoot);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gold_record;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.glod_record));
        getData(mPage);
    }

    private void getData(int page) {
        SSQSApplication.apiClient(classGuid).getAccountDetailsList(2, page, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultAccountPage page = (CcApiResult.ResultAccountPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        processData(page.getItems());
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(GoldRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected void initListener() {
        mGlodRecordLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                    mIsLastRow = true;
                } else {
                    mIsLastRow = false;
                }
            }
        });
        mGlodRecordLv.setOnTouchListener(this);
    }

    private void processData(List<DiamondsGlodRecordBean> items) {
        mAdapter = new MyRecordAdapter(this, items);
        mGlodRecordLv.setAdapter(mAdapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = event.getX();
                mMoveY = event.getY();

                float diffY = mDownY - mMoveY;

                if (diffY > 10 && mIsLastRow) {
                    mPage++;
                    SSQSApplication.apiClient(classGuid).getAccountDetailsList(2, mPage, 10, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                CcApiResult.ResultAccountPage page = (CcApiResult.ResultAccountPage) result.getData();

                                if (page != null && page.getItems() != null) {
                                    mMItemsData.addAll(page.getItems());
                                    mAdapter.notifyDataSetChanged();
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(GoldRecordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return false;
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
