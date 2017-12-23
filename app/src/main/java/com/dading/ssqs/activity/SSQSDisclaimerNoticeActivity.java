package com.dading.ssqs.activity;

import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/16 13:41
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SSQSDisclaimerNoticeActivity extends BaseActivity {
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_discalimer_nitice;
    }

    @Override
    protected void initData ( ) {
        super.initData( );
        mTopTitle.setText(getString(R.string.disclaimer_notices));
    }

    @OnClick({R.id.top_back})
    public void OnClik (View v) {
        finish( );
    }
}
