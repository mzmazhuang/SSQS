package com.dading.ssqs.controllar.tz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.NoteRecordActivity;
import com.dading.ssqs.adapter.MyFTAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/5 17:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class UpLoadTZControllar extends Fragment {
    private static final String TAG = "MyFollowControlalr";
    private Context context;
    public View mRootView;
    private ListView mList;
    private RelativeLayout mEmpty;

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
        initListener();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.my_tz, null);
        mList = (ListView) view.findViewById(R.id.my_tz_lv);
        mEmpty = (RelativeLayout) view.findViewById(R.id.data_empty);
        return view;
    }

    private void initData() {
        mList.setEmptyView(mEmpty);
        /**
         /v1.0/article/mine/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明：
         page:当前页码
         count:页数
         auth_token：登陆后加入请求头
         */

        SSQSApplication.apiClient(0).getMyArticleList(1, 1000, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMyTzPage page = (CcApiResult.ResultMyTzPage) result.getData();

                    if (page != null && page.getItems() != null) {
                        mList.setAdapter(new MyFTAdapter(context, page.getItems()));
                    }

                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        ((NoteRecordActivity) context).finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void initListener() {
    }
}
