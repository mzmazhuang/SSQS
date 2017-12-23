package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.EssayInfoAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.EssayInfoBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/10 10:11
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class EssayInfoActivity extends BaseActivity {
    private static final String TAG = "EssayInfoActivity";
    @Bind(R.id.essay_he_lv)
    PullToRefreshListView mEssayHeLv;
    @Bind(R.id.essay_he_no_data)
    ImageView mEssayHeNoData;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private int mPage;
    private List<EssayInfoBean> mData;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_essay;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.he_essay));
        /**
         6.根据用户获取帖子列表
         a)	请求地址：/v1.0/article/detail/userID/{userID}/page/{page}/count/{count}
         b)	请求方式:get
         c)	请求参数说明：userID：用户ID    page:第几页     count:页数
         auth_token：登陆后加入请求头
         */
        Intent intent = getIntent();
        String savantID = intent.getStringExtra(Constent.SAVANT_ID);
        mPage = 1;

        SSQSApplication.apiClient(classGuid).getArticleList(savantID, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultArticlePage page = (CcApiResult.ResultArticlePage) result.getData();

                    if (page != null && page.getItems() != null) {
                        mData = page.getItems();

                        if (mData.size() > 0) {
                            mEssayHeNoData.setVisibility(View.GONE);
                        } else {
                            mEssayHeNoData.setVisibility(View.VISIBLE);
                        }
                        processData(mData);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(EssayInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(EssayInfoActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(List<EssayInfoBean> data) {
        mEssayHeLv.setAdapter(new EssayInfoAdapter(this, data));
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();

    }
}
