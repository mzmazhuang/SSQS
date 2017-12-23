package com.dading.ssqs.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/27 17:28
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class OnLineServerActivity extends BaseActivity {

    @Bind(R.id.top_title)
    TextView  mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;
    @Bind(R.id.online_servers_wb)
    WebView   mOnlineServersWb;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_online_server;
    }

    @Override
    protected void initData ( ) {
        super.initData( );
        mTopTitle.setText(getString(R.string.online_server));
        mTopIcon.setImageResource(R.mipmap.shuaxin);
        WebSettings settings = mOnlineServersWb.getSettings( );
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mOnlineServersWb.loadUrl("http://www.qq.com/");
    }

    @OnClick({R.id.top_back})
    public void OnClik (View v) {
        finish( );
    }
}
