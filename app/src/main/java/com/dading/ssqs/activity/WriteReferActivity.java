package com.dading.ssqs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.GBMatchBeforeAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;
import pulltorefresh.PullToRefreshExpandableListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/29 17:35
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WriteReferActivity extends BaseActivity {
    private static final String TAG = "WriteReferActivity";
    @Bind(R.id.write_refer_listview)
    PullToRefreshExpandableListView mWriteReferListview;
    @Bind(R.id.write_refer_by_time)
    TextView writeByTime;
    @Bind(R.id.loading_animal)
    LinearLayout loadingAnimal;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private ExpandableListView mRefreshableView;
    private int mType;
    private MatchBeforBeanAll mBean;

    @Override
    protected void initView() {
        mRefreshableView = mWriteReferListview.getRefreshableView();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.write_referr;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.no_start_match));
        mType = 1; //显示时间

        SSQSApplication.apiClient(classGuid).getMatchGuessList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    MatchBeforBeanAll bean = (MatchBeforBeanAll) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    LogUtil.util(TAG, result.getMessage() + "赛前失败信息");
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(WriteReferActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
        /**
         * 1.	赛前列表
         a)	请求地址：
         /v1.0/match/guess/type/{type}
         b)	请求方式:
         Get
         c)	请求参数说明：
         Type: (1：联赛，2：时间)
         auth_token：登陆后加入请求头
         */
    }

    private void processData(MatchBeforBeanAll beanAll) {
        mBean = beanAll;
        loadingAnimal.setVisibility(View.GONE);
        mWriteReferListview.getRefreshableView().setAdapter(new GBMatchBeforeAdapter(WriteReferActivity.this, beanAll, mType));
        mRefreshableView.setDivider(new ColorDrawable(Color.GRAY));
        mRefreshableView.setDividerHeight(1);
        mRefreshableView.setBackgroundColor(Color.GRAY);
    }

    @OnClick({R.id.top_back, R.id.write_refer_by_time,
            R.id.write_refer_by_time_icon, R.id.write_refer_prize_protect})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.write_refer_by_time:
            case R.id.write_refer_by_time_icon:
                if (writeByTime.getText().equals("按时间")) {
                    writeByTime.setText("按联赛");
                    //显示时间比赛列表
                    mType = 2;
                } else {
                    mType = 1;
                    writeByTime.setText("按时间");
                }
                if (mBean != null)
                    mWriteReferListview.getRefreshableView().setAdapter(new GBMatchBeforeAdapter(this, mBean, mType));
                break;
            case R.id.write_refer_prize_protect:
                Intent intent = new Intent(this, WriteReferPrizeProtectActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
