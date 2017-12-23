package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ShareArticleElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JSShareBean;
import com.dading.ssqs.onekeyshare.OnekeyShare;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;


import java.util.HashMap;

import butterknife.Bind;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/3 15:07
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyJsActivity extends BaseActivity {
    private static final String TAG = "MyJsActivity";
    @Bind(R.id.test_wb)
    WebView testwb;
    @Bind(R.id.loading_animal)
    LinearLayout mLoadingAnimal;
    private String mId;
    private String mbean;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        setWeb(mId);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_js;
    }


    public class Jss {
        @JavascriptInterface
        public void androidLogin() {
            Intent i = new Intent(MyJsActivity.this, LoginActivity.class);
            startActivity(i);
        }

        @JavascriptInterface
        public void androidBack() {
            finish();
        }

        @JavascriptInterface
        public void androidShare(String mbean) {
            LogUtil.util(TAG, "JS分享返回数据是------------------------------:" + mbean);
            try {
                JSShareBean shareBean = JSON.parseObject(mbean, JSShareBean.class);
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false))
                    showShare(shareBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void showShare(JSShareBean bean) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(bean.shareTitle);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(bean.webUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(bean.shareCotent);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setImageUrl(bean.shareImage);
        oks.setUrl(bean.webUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(bean.shareCotent);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("賽事分享");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(bean.webUrl);
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onError(Platform arg0, int arg1, Throwable t) {
            }

            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                /**
                 * 5.	分享文章
                 9)	请求地址：
                 /v1.0/wShare/save
                 10)	请求方式:
                 post
                 writesID	string		是	文章ID
                 auth_token	String		是	token（放在头部）
                 */
                ShareArticleElement element = new ShareArticleElement();
                element.setWritesID(mId);

                SSQSApplication.apiClient(classGuid).shareArticle(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            TmtUtils.midToast(UIUtils.getContext(), "分享成功!", 0);
                        } else {
                            TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                        }
                    }
                });
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
            }
        });
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mId = intent.getStringExtra(Constent.NEWS_ID);
        testwb.getSettings().setJavaScriptEnabled(true);
        testwb.getSettings().setDomStorageEnabled(true);
        testwb.addJavascriptInterface(new Jss(), "jbk");

        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                mLoadingAnimal.setVisibility(View.GONE);
            }
        }, 1500);

        setWeb(mId);
    }

    private void setWeb(String id) {
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mbean = "http://www.ddzlink.com/ssqs/topic.html#/home_page/" + id + "?token=" + UIUtils.getSputils().getString(Constent.TOKEN, null);
        } else {
            mbean = "http://www.ddzlink.com/ssqs/topic.html#/home_page/" + id + "?token=";
        }
        testwb.loadUrl(mbean);
    }

    @Override
    protected void initListener() {
        testwb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    protected void setUnDe() {
        UIUtils.removeTaskAll(null);
    }
}
