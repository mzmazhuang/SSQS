package com.dading.ssqs.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.Constent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/23 17:39
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LevelActivity extends BaseActivity {
    @Bind(R.id.about_level_web)
    WebView mAboutLevelWeb;
    @Bind(R.id.top_title)
    TextView mTopTitle;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_level;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.about_leve));
        mAboutLevelWeb.loadUrl(SSQSApplication.apiClient(classGuid).getBaseUri() + "/v1.0/level");
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
