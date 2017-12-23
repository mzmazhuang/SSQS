package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.TurnTablePrizeBean;
import com.dading.ssqs.controllar.store.MyPrizeInfoAdapter;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/15 10:11
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PrizeInfoRecordActivity extends BaseActivity {
    private static final String TAG = "PrizeInfoRecordActivity";
    @Bind(R.id.prize_info_record_list)
    ListView mPrizeInfoRecordList;
    @Bind(R.id.data_empty)
    RelativeLayout mPrizeRecordNoData;

    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_prize_info_record;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.prize_record));
        /**
         * a)	请求地址：
         /v1.0/awardExchange/list/gift
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         */

        SSQSApplication.apiClient(classGuid).getGiftHistory(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<TurnTablePrizeBean> items = (List<TurnTablePrizeBean>) result.getData();

                    if (items != null) {
                        processData(items);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(PrizeInfoRecordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(List<TurnTablePrizeBean> data) {
        if (data != null && data.size() > 0) {
            mPrizeInfoRecordList.setVisibility(View.VISIBLE);
            mPrizeRecordNoData.setVisibility(View.GONE);
            mPrizeInfoRecordList.setAdapter(new MyPrizeInfoAdapter(this, data));
        } else {
            mPrizeInfoRecordList.setVisibility(View.GONE);
            mPrizeRecordNoData.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
