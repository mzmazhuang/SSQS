package com.dading.ssqs.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JPushCheckedBean;
import com.dading.ssqs.bean.ShareBean;
import com.dading.ssqs.onekeyshare.OnekeyShare;
import com.dading.ssqs.utils.DataCleanManager;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;


import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 18:07
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MoreSettingActivity extends BaseActivity {

    private static final String TAG = "MoreSettingActivity";
    /* @Bind(R.id.setting_only_wifi_iv)
     CheckBox mSettingOnlyWifiIv;*/
    @Bind(R.id.setting_cache_size)
    TextView mSettingCacheSize;
    @Bind(R.id.setting_out_loading)
    Button mOutLoading;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.setting_open_jupsh_iv)
    ImageButton mSettingOpenJupshIv;

    private String mSize;
    private boolean mIsOpen;

    @Override
    protected void initView() {
        try {
            mSize = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSettingCacheSize.setText(mSize);
        ShareSDK.initSDK(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_more_setting;
    }

    @Override
    protected void initData() {
        /**
         * 45.	进球通知开关查询
         a)		请求地址：/v1.0/matchVoice
         */

        SSQSApplication.apiClient(classGuid).getMatchVoice(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    JPushCheckedBean bean = (JPushCheckedBean) result.getData();

                    if (bean != null && mSettingOpenJupshIv != null) {
                        if (bean.getData() == 1) {
                            mIsOpen = true;
                            mSettingOpenJupshIv.setBackgroundResource(R.mipmap.wifi_border_left);
                        } else {
                            mIsOpen = false;
                            mSettingOpenJupshIv.setBackgroundResource(R.mipmap.wifi_border_right);
                        }
                    }
                } else {
                    Logger.INSTANCE.d(TAG, result.getMessage() + "红人明星中獎失败信息");
                }
            }
        });
     /*   if (open)
            JPushInterface.resumePush(UIUtils.getContext());
        else
            JPushInterface.stopPush(UIUtils.getContext());*/
        mTopTitle.setText(getString(R.string.setting));
        Logger.INSTANCE.d(TAG, "是否登录了" + UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false));
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mOutLoading.setVisibility(View.VISIBLE);
        } else {
            mOutLoading.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @OnClick({R.id.setting_lock, R.id.setting_change_pwd, R.id.setting_change_business_pwd, R.id.setting_clear_cache,
            R.id.setting_give_score, R.id.setting_share_to_friend, R.id.setting_user_service_protocol,
            R.id.setting_disclaimer_notices, R.id.setting_about_ssqs, R.id.setting_out_loading,
            R.id.top_back, R.id.setting_open_jupsh_iv})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.setting_open_jupsh_iv:
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    /**
                     46. 进球通知开关编辑
                     a)	请求地址：/v1.0/matchVoice/status/{status}
                     b)	请求方式:get
                     c)	请求参数说明
                     字段名	类型	长度	是否必填	备注
                     auth_token	string		是	Token
                     Status	Int	1	是	1开启0关闭
                     */

                    SSQSApplication.apiClient(classGuid).setMatchVoice(mIsOpen, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                if (!mIsOpen) {
                                    mSettingOpenJupshIv.setBackgroundResource(R.mipmap.wifi_border_left);
                                } else {
                                    mSettingOpenJupshIv.setBackgroundResource(R.mipmap.wifi_border_right);
                                }
                                mIsOpen = !mIsOpen;
                            } else {
                                ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.setting_lock:
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(this, UserLockActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.setting_change_pwd:
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(this, ChangePwdActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.setting_change_business_pwd:
                Intent intent;
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    if (UIUtils.getSputils().getBoolean(Constent.IS_BIND_CARD, false)) {
                        intent = new Intent(this, ForgetBusinessPwdActivity.class);
                    } else {
                        intent = new Intent(this, NewBindBankCardActivity.class);
                    }
                } else {
                    intent = new Intent(this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.setting_clear_cache:
                DataCleanManager.clearAllCache(this);
                try {
                    mSize = DataCleanManager.getTotalCacheSize(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSettingCacheSize.setText(mSize);

                break;
            case R.id.setting_give_score:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent intentS = new Intent(Intent.ACTION_VIEW, uri);
                intentS.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentS);
                break;
            case R.id.setting_share_to_friend:
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    /**
                     * /v1.0/share
                     */
                    SSQSApplication.apiClient(classGuid).getShareInfo(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                ShareBean bean = (ShareBean) result.getData();

                                if (bean != null) {
                                    showShare(bean);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(MoreSettingActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                } else {
                    Intent intent1 = new Intent(this, LoginActivity.class);
                    startActivity(intent1);
                    finish();
                }
                break;
            case R.id.setting_user_service_protocol:
                Intent intent2 = new Intent(this, SSQSRegisterProtocolActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting_disclaimer_notices:
                Intent intents = new Intent(this, SSQSDisclaimerNoticeActivity.class);
                startActivity(intents);
                break;
            case R.id.setting_about_ssqs:
                Intent intenAbout = new Intent(this, AboutSSQSActivity.class);
                startActivity(intenAbout);
                break;
            case R.id.setting_out_loading:
                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);

                UIUtils.getSputils().putString(Constent.TOKEN, "");
                //发送广播
                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                finish();
                break;
            default:
                break;
        }
    }

    private void showShare(ShareBean data) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(data.title);
        Logger.INSTANCE.d(TAG, "data.title------------------------------:" + data.title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(data.forwardUrl);
        Logger.INSTANCE.d(TAG, "data.forwardUrl------------------------------:" + data.forwardUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(data.content + data.forwardUrl);
        Logger.INSTANCE.d(TAG, "data.content------------------------------:" + data.content);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setImageUrl(data.logoUrl);
        oks.setUrl(data.forwardUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(data.content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(data.title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        Logger.INSTANCE.d(TAG, "data.logoUrl------------------------------:" + data.logoUrl);
        oks.setSiteUrl(data.forwardUrl);
        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        ShareSDK.stopSDK(this);
    }
}
