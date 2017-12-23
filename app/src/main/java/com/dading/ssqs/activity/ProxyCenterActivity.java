package com.dading.ssqs.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.ProxyAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.MultiItem;
import com.dading.ssqs.bean.ProxyCenterBean;
import com.dading.ssqs.utils.TmtUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/8/3.
 */

public class ProxyCenterActivity extends BaseActivity {
    @Bind(R.id.templete_recycleview)
    RecyclerView mRecycleview;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.loading_animal)
    LinearLayout mLoadingAnimal;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_proxy_center;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.proxy_center));

        SSQSApplication.apiClient(classGuid).getAgentInfo(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mLoadingAnimal.setVisibility(View.GONE);

                if (result.isOk()) {
                    ProxyCenterBean bean = (ProxyCenterBean) result.getData();

                    if (bean != null) {
                        processedData(bean);
                    }
                } else {
                    TmtUtils.midToast(ProxyCenterActivity.this, result.getMessage(), 0);
                }
            }
        });
    }

    private void processedData(ProxyCenterBean bean) {
        ArrayList<MultiItem> listItem = new ArrayList<>();
        MultiItem item = new MultiItem();
        item.setData(bean);
        item.setItemType(MultiItem.PROXY_HEAD);
        listItem.add(item);

        MultiItem itemRecycle = new MultiItem();
        itemRecycle.setItemType(MultiItem.PROXY_RECYCLE_VIEW);
        listItem.add(itemRecycle);

        mRecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecycleview.setAdapter(new ProxyAdapter(this, listItem));
    }


    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }

}
