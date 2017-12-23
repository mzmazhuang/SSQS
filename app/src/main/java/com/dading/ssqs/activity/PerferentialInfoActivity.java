package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;

import butterknife.Bind;
import butterknife.OnClick;
import richtextview.RichTextView;

/**
 * 创建者     zcl
 * 创建时间   2017/7/10 15:17
 * 描述	      ${活动详情}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class PerferentialInfoActivity extends BaseActivity {

    @Bind(R.id.perferential_info_web)
    WebView mPerferentialInfoWeb;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.perferential_info_content)
    RichTextView mPerferentialInfoContent;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_perferential_info;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.perferential_info));
        Intent intent = getIntent();
        String s = intent.getStringExtra(Constent.PERFERENTIAL_TITLE);
        String content = intent.getStringExtra(Constent.PERFERENTIAL_CONTENT);
        if (TextUtils.isEmpty(content)) {
            String web = intent.getStringExtra(Constent.PERFERENTIAL_WEB);
            if (!TextUtils.isEmpty(web)) {
                mPerferentialInfoWeb.loadUrl(web);
                mPerferentialInfoWeb.setVisibility(View.VISIBLE);
                mPerferentialInfoWeb.setWebViewClient(new WebViewClient());
            }
        } else {
            mPerferentialInfoContent.setHtml(content, 800);
            mPerferentialInfoContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (!TextUtils.isEmpty(s))
            mTopTitle.setText(s);
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
