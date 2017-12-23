package com.dading.ssqs.activity;

import android.view.View;
import android.widget.TextView;


import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/18 15:15
 * 描述	      ${关于app详细信息}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 */
public class AboutSSQSActivity extends BaseActivity {
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @OnClick({R.id.top_back})
    public void OnClik (View v) {
        finish( );
    }

    @Override
    protected void initData ( ) {
        mTopTitle.setText(getString(R.string.about_ssqs));
    }

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_about_ssqs;
    }
}
