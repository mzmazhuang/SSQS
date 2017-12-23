package com.dading.ssqs.activity;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.ProxyIntroBean;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;
import richtextview.RichTextView;

/**
 * Created by lenovo on 2017/8/8.
 */
public class ProxyIntroActivty extends BaseActivity {
    private static final String TAG = "ProxyIntroActivty";
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.proxy_intro_img)
    ImageView mProxyIntroImg;
    @Bind(R.id.proxy_intro_procontent)
    RichTextView mProxyIntroTxt;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_proxy_intro;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.proxy_intro));

        SSQSApplication.apiClient(classGuid).getAgentRemark(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    ProxyIntroBean bean = (ProxyIntroBean) result.getData();

                    if (bean != null) {
                        processedData(bean);
                    }
                } else {
                    TmtUtils.midToast(ProxyIntroActivty.this, result.getMessage(), 0);
                }
            }
        });
    }

    private void processedData(ProxyIntroBean bean) {
        Glide.with(UIUtils.getContext())
                .load(bean.getImageUrl())
                .error(R.mipmap.fail)
                .centerCrop()
                .into(mProxyIntroImg);

        mProxyIntroTxt.setHtml(bean.getContent(), 800);
        mProxyIntroTxt.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
