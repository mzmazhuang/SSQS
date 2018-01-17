package com.dading.ssqs.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.cells.TitleCell;
import com.dading.ssqs.components.LoadingDialog;
import com.dading.ssqs.utils.WebViewEx;

import java.lang.ref.WeakReference;

public class WebActivity extends BaseActivity {
    private Context mContext;
    private WebViewEx mWebView;
    private WebHandler handler;
    private TitleCell titleCell;
    private static String curr_title;//传参过来的标题
    private static String curr_url;//传参过来的url
    private static String curr_content;//传参过来的网页内容
    private LoadingDialog loadingDialog;

    private static final int WEB_OPEN_PROGRESS = 0x3002;
    private static final int WEB_OPEN_FINISH = 0x3003;
    private static final int WEB_GET_TITLE = 0x3004;

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        curr_url = getIntent().getStringExtra("url");
        curr_title = getIntent().getStringExtra("title");
        curr_content = getIntent().getStringExtra("url_content");

        mContext = this;

        handler = new WebHandler(this);

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);

        titleCell = new TitleCell(mContext, "加载中...");
        titleCell.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48));

        mWebView = new WebViewEx(this);
        mWebView.setWebChromeClient(new MyWebViewChromeClient(mWebView));
        MyWebViewClient mWebViewClient = new MyWebViewClient(this, mWebView);
        mWebView.setWebViewClient(mWebViewClient);
        container.addView(mWebView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        loadingDialog = new LoadingDialog(mContext);
        loadingDialog.show();

        setUrl(curr_url, curr_content);

        return container;
    }

    private void setUrl(final String url, final String content) {
        SSQSApplication.getHandler().post(new Runnable() {
            @SuppressLint("SetJavaScriptEnabled")
            public void run() {
                if (mWebView == null || mWebView.getSettings() == null) {
                    return;
                }
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setSupportZoom(true);
                mWebView.setHorizontalScrollBarEnabled(false);
                mWebView.setVerticalScrollBarEnabled(false);
                mWebView.getSettings().setBuiltInZoomControls(true);
                mWebView.getSettings().setDomStorageEnabled(true);
                mWebView.getSettings().setSupportMultipleWindows(true);
                mWebView.getSettings().setUseWideViewPort(true);
                String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
                mWebView.getSettings().setGeolocationDatabasePath(dir);
                mWebView.getSettings().setGeolocationEnabled(true);
                mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                mWebView.getSettings().setLoadWithOverviewMode(true);
                mWebView.getSettings().setDefaultFontSize(16);
                mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                mWebView.clearHistory();

                if (!TextUtils.isEmpty(url)) {
                    mWebView.loadUrl(url);
                } else if (!TextUtils.isEmpty(content)) {
                    mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                }

                mWebView.scrollTo(0, 0);
            }
        });
    }

    public class MyWebViewClient extends WebViewEx.WebViewClientEx {

        public MyWebViewClient(Context context, WebViewEx mWeb) {
            super(mWeb);
            mContext = context;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Message m = new Message();
            m.what = WEB_OPEN_FINISH;
            m.obj = url;
            handler.sendMessage(m);

            if (view != null) {
                Message msg = new Message();
                msg.what = WEB_GET_TITLE;
                Bundle b = new Bundle();
                b.putString("title", view.getTitle());
                msg.setData(b);
                handler.sendMessage(msg);
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //加载失败
        }
    }

    private class MyWebViewChromeClient extends WebViewEx.WebChromeClientEx {

        public MyWebViewChromeClient(WebViewEx mWeb) {
            super(mWeb);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            Message m = handler.obtainMessage();
            m.what = WEB_OPEN_PROGRESS;
            m.arg1 = newProgress;
            handler.sendMessage(m);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Message m = handler.obtainMessage();
            m.what = WEB_GET_TITLE;
            Bundle b = new Bundle();
            b.putString("title", title);
            m.setData(b);
            handler.sendMessage(m);
        }
    }

    static class WebHandler extends Handler {
        final WeakReference<WebActivity> mContext;

        WebHandler(WebActivity context) {
            mContext = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int WEB_OPEN_NEW = 0x3001;
            if (msg.what == WEB_OPEN_NEW) {

            } else if (msg.what == WEB_OPEN_PROGRESS) {
                if (mContext != null && mContext.get() != null) {
                    //加载进度
//                    mContext.get().onProgress(msg.arg1);
                }
            } else if (msg.what == WEB_OPEN_FINISH) {
                if (mContext != null && mContext.get() != null) {
                    mContext.get().onOpenFinish();
                }
            } else if (msg.what == WEB_GET_TITLE) {
                if (mContext != null && mContext.get() != null) {
                    Bundle b = msg.getData();
                    String t = b.getString("title");
                    mContext.get().onTitled(t);
                }
            }
        }
    }

    private void onOpenFinish() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void onTitled(String t) {
        if (TextUtils.isEmpty(curr_title)) {
            titleCell.setTitle(t);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            loadingDialog.cancel();
        }
        if (mWebView != null)
            mWebView.destroy();
    }
}
