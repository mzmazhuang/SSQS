package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.SpUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/14 11:37
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LuckBallActivity extends BaseActivity {
    private static final String TAG = "LuckBallActivity";
    @Bind(R.id.luck_ball_wb)
    WebView mLuckBallWb;

    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    protected void initData ( ) {
        SpUtils spUtils = new SpUtils(this);
        Intent intent = getIntent( );
        String name = intent.getStringExtra(Constent.CASINO_NAME);
        String url = intent.getStringExtra(Constent.CASINO_URL);
        if (TextUtils.isEmpty(name))
            mTopTitle.setText("娱乐场");
        else
            mTopTitle.setText(name);

        if (spUtils.getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            url = url + spUtils.getString(Constent.TOKEN, "");
        }
        Logger.d(TAG, "娛樂場項目返回数据是------------------------------:" + url);
        mLuckBallWb.setWebChromeClient(new WebChromeClient( ));
        mLuckBallWb.setWebViewClient(new WebViewClient( ));
        mLuckBallWb.loadUrl("http://www.ddzlink.com/ssqsApp/tpl/game.html?token=8b53ebe9e0ce49719ac1117ab63528fc");
    }


    @OnClick({R.id.top_back})
    public void OnClik (View v) {
        finish( );
    }


    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_luck_ball;
    }
}
