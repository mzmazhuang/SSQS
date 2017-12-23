package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;

import java.lang.reflect.InvocationTargetException;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/16 15:34
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LivingActivity extends BaseActivity {

    @Bind(R.id.living_no_tv_activity)
    TextView liveNoTv;
    @Bind(R.id.live_webview)
    WebView livewebview;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    public void reDisPlay() {
        try {
            livewebview.getClass().getMethod("onResume").invoke(livewebview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPauPlay() {
        try {
            livewebview.getClass().getMethod("onPause").invoke(livewebview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.living));
        Intent intent = getIntent();
        String s = intent.getStringExtra(Constent.LIVING);
        if (TextUtils.isEmpty(s)) {
            liveNoTv.setVisibility(View.VISIBLE);
            livewebview.setVisibility(View.GONE);
        } else {
            liveNoTv.setVisibility(View.GONE);
            livewebview.setVisibility(View.VISIBLE);

            /**
             * 停止播放：在页面的onPause方法中使用：
             livewebview.getClass().getMethod("onPause").invoke(livewebview,(Object[])null);
             继续播放：在页面的onResume方法中使用：
             webView.getClass().getMethod("onResume").invoke(webView,(Object[])null);
             这样就可以控制视频的暂停和继续播放了。
             */
            livewebview.setInitialScale(25);//为25%，最小缩放等级
            WebSettings setting = livewebview.getSettings();
            //支持javascript
            setting.setJavaScriptEnabled(true);
            // 设置可以支持缩放
            setting.setSupportZoom(true);
            // 设置可以支持缩放
            setting.setBuiltInZoomControls(false);
            //扩大比例的缩放
            setting.setUseWideViewPort(true);
            //播放视频
            setting.setPluginState(WebSettings.PluginState.ON);
            //自适应屏幕
            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            setting.setLoadWithOverviewMode(true);

            setting.setSavePassword(true);
            setting.setSaveFormData(true);// 保存表单数据
            setting.setJavaScriptEnabled(true);

            setting.setDomStorageEnabled(true);
            setting.setSupportMultipleWindows(true);// 新加

            setting.setUseWideViewPort(true); // 关键点
            setting.setAllowFileAccess(true); // 允许访问文件
            setting.setSupportZoom(true); // 支持缩放

            setting.setCacheMode(WebSettings.LOAD_DEFAULT); // 默认加载缓存内容
            livewebview.setWebChromeClient(new WebChromeClient());
            livewebview.setWebViewClient(new WebViewClient());
            //livewebview.loadUrl("http://www.mgtv.com/b/310102/3818687.html?cxid=94n3624ea");
            livewebview.loadUrl(s);
        }
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_living;
    }

}
