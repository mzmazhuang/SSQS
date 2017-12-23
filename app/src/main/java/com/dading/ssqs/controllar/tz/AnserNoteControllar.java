package com.dading.ssqs.controllar.tz;

import android.content.Context;
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
import com.dading.ssqs.adapter.MyGTAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2017/1/10 10:01
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AnserNoteControllar extends Fragment {
    private static final String TAG = "AnserNoteControllar";
    @Bind(R.id.data_empty)
    RelativeLayout mMyTzGtNoRt;
    @Bind(R.id.my_tz_gt_lv)
    ListView mMyTzGtLv;
    private Context context;
    public View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.context = getActivity();
        mRootView = initView();
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.anser_note, null);
        ButterKnife.bind(this, view);
        return view;
    }

    private void initData() {
        mMyTzGtLv.setEmptyView(mMyTzGtNoRt);

        /**
         * 1.	我的跟帖
         a)	请求地址：
         /v1.0/article/follow/page/{page}/count/{count}
         b)	请求方式:
         get
         c)	请求参数说明：
         page:当前页码
         count:页数
         auth_token：登陆后加入请求头
         */

        SSQSApplication.apiClient(0).getMyFollowArticle(1, 2000, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMyTzGTPage page = (CcApiResult.ResultMyTzGTPage) result.getData();

                    if (page != null) {

                        if (page.getItems() != null) {
                            mMyTzGtLv.setAdapter(new MyGTAdapter(context, page.getItems()));
                        }
                    }
                } else {
                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                }
            }
        });
    }

    private void initListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
