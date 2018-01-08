package com.dading.ssqs.controllar.task;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.AchieveBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.R;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/6 15:24
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AchieveTaskControllar extends Fragment {
    private static final String TAG = "AchieveTaskControllar";
    private Context context;
    public View mRootView;
    private ListView mLv;
    private View mView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getActivity();
        mRootView = initView();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private View initView() {
        mView = View.inflate(context, R.layout.home_achieve_task_controllar, null);
        mLv = (ListView) mView.findViewById(R.id.free_glod_achieve_lv);
        return mView;
    }

    private void initData() {
        /**
         * 8.	成就奖励
         1)	请求地址：
         /v1.0/achieve/list
         2)	请求方式:
         get
         3)	请求参数说明：
         字段名	类型	长度	备注
         auth_token	varchar		token
         */

        SSQSApplication.apiClient(0).getAchieveList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<AchieveBean> items = (List<AchieveBean>) result.getData();

                    if (items != null) {
                        mLv.setAdapter(new AchieveTaskAdapter(context, items));
                    }
                } else {
                    Logger.INSTANCE.d(TAG, result.getMessage() + "成就任務失败信息");
                }
            }
        });
    }
}
