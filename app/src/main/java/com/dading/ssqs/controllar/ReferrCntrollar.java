package com.dading.ssqs.controllar;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.PerferentialInfoActivity;
import com.dading.ssqs.adapter.PerferentialAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.PerferentialBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/22 17:05
 * 描述	      优惠
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ReferrCntrollar extends BaseTabsContainer {

    private static final String TAG = "ReferrCntrollar";
    private View mReferrView;
    private ListView mLv;

    private RelativeLayout mNo;
    private List<PerferentialBean> mData;
    private PerferentialAdapter mAdapter;


    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mContenLy.setVisibility(View.GONE);
    }

    @Override
    public View initContentView(Context context) {
        mReferrView = View.inflate(context, R.layout.referrpager, null);
        mLv = (ListView) mReferrView.findViewById(R.id.referrr_circle_listview);
        mNo = (RelativeLayout) mReferrView.findViewById(R.id.referrr_no_data);
        mLv.setEmptyView(mNo);
        return mReferrView;
    }

    private boolean hasInit = false;

    public void init() {
        if (!hasInit) {
            hasInit = true;

            SSQSApplication.apiClient(0).getActivityList(new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {
                        List<PerferentialBean> items = (List<PerferentialBean>) result.getData();

                        if (items != null) {
                            processedData(items);
                        }
                    } else {
                        Logger.INSTANCE.d(TAG, result.getMessage() + "红人明星中獎失败信息");
                    }
                }
            });
        }
    }

    @Override
    public void initData() {

    }

    private void processedData(List<PerferentialBean> bean) {
        mData = bean;
        mAdapter = new PerferentialAdapter(mContent, mData);
        mLv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContent, PerferentialInfoActivity.class);
                if (mData != null && mData.get(position) != null) {
                    intent.putExtra(Constent.PERFERENTIAL_WEB, mData.get(position).getWebUrl());
                    intent.putExtra(Constent.PERFERENTIAL_TITLE, mData.get(position).getTitle());
                    intent.putExtra(Constent.PERFERENTIAL_CONTENT, mData.get(position).getContent());
                }
                mContent.startActivity(intent);
            }
        });
    }
}
