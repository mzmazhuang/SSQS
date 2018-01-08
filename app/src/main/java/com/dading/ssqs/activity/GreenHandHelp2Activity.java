package com.dading.ssqs.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/11 9:54
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GreenHandHelp2Activity extends BaseActivity {
    private static final String TAG = "GreenHandHelp2Activity";

    @Bind(R.id.green_hand2_webView)
    WebView   mGreenHand2WebView;
    @Bind(R.id.top_title)
    TextView  mTopTitle;
    @Bind(R.id.top_back)
    ImageView mTopBack;

    public class Js {
        @JavascriptInterface
        public void sign ( ) {
            Logger.INSTANCE.d(TAG, "我被JS调用了------------------------------:");
            GreenHandHelp2Activity.this.finish( );
        }
    }

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_greenhand_help2;
    }

    @Override
    protected void initData ( ) {
        mTopTitle.setText(getString(R.string.greenhand_help));
        Intent intent = getIntent( );
        String s = intent.getStringExtra(Constent.GREEN_HAND);
        switch (s) {
            case "0":
                mGreenHand2WebView.getSettings( ).setJavaScriptEnabled(true);//允许使用JS
                mGreenHand2WebView.getSettings( ).setDomStorageEnabled(true);
                mGreenHand2WebView.setDownloadListener(new MyWebViewDownLoadListener( )); //通过实现自己的DownloadListener来实现文件的下载
                   /* mGreenHand2WebView.setWebChromeClient(new WebChromeClient() {//这是加载进度条的方法
                        @Override
                        public void onProgressChanged(WebView view, int progress) {
                            // TODO Auto-generated method stub
                            activity.setTitle("Loading...");
                            activity.setProgress(progress * 100);
                            if (progress == 100)
                                activity.setTitle(R.string.app_name);
                        }*/
                // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
                mGreenHand2WebView.addJavascriptInterface(new Js( ), "js");

                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/instruction.html");
                break;
            case "1":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/diamond.html");
                break;
            case "2":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/get_banlance.html");
                break;
            case "3":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/get_prize.html");
                break;
            case "4":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/isTransaction.html");
                break;
            case "5":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/preplay.html");
                break;
            case "6":
                break;
            case "7":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/string.html");
                break;
            case "8":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/rankList.html");
                break;
            case "9":
                mGreenHand2WebView.loadUrl("http://112.74.130.167:8099/tpl/prize_object.html");
                break;

            default:
                break;
        }
    }

    @OnClick({R.id.top_back})
    public void OnClik (View v) {
        finish( );
    }

    //通过实现自己的DownloadListener来实现文件的下载
    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart (String url, String userAgent, String arg2,
                                     String mimetype, long contentLength) {
            Uri uri = Uri.parse(url); // url为你要链接的地址
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}
