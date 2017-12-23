package com.dading.ssqs.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.LoginElement;
import com.dading.ssqs.apis.elements.ThreeLoginElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


/**
 * 创建者     ZCL
 * 创建时间   2016/7/6 15:53
 * 描述
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "LoginActivity";
    @Bind(R.id.et_loading_number)
    EditText mEtLoadingNumber;
    @Bind(R.id.et_loading_pwd)
    EditText mEtLoadingPwd;
    @Bind(R.id.et_loading_number_icon)
    ImageView mEtLoadingNumberIcon;
    @Bind(R.id.et_loading_pwd_icon)
    ImageView mEtLoadingPwdIcon;
    @Bind(R.id.loading_button)
    Button mLoadingButton;
    @Bind(R.id.iv_loading_number_close)
    ImageView mIvLoadingNumberClose;
    @Bind(R.id.et_loading_pwd_close)
    ImageButton mEtLoadingPwdClose;
    @Bind(R.id.et_loading_pwd_look)
    CheckBox mCbLoadingPwdLook;
    @Bind(R.id.loading_iv_wx_loading)
    ImageButton mLoadingIvWxLoading;
    @Bind(R.id.loading_iv_qq_loading)
    ImageButton mLoadingIvQqLoading;
    @Bind(R.id.loading_iv_xl_loading)
    ImageButton mLoadingIvXlLoading;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.loading_button_try_play)
    Button mLoadingButtonTryPlay;
    @Bind(R.id.loading_tv_new_user_phone)
    TextView mLoadingTvNewUserPhone;
    @Bind(R.id.loading_tv_new_user_number)
    TextView mLoadingTvNewUserNumber;
    @Bind(R.id.loading_tv_forgot_pwd)
    TextView mLoadingTvForgotPwd;
    @Bind(R.id.loading_animal)
    LinearLayout mLoadingAnimal;
    private Handler mHandler;
    private String mUserIcon;
    private String mUserId;
    private String mUserName;
    private int mLoginTag;

    @Override
    protected void setUnDe() {
        super.setUnDe();
        mHandler.removeCallbacksAndMessages(null);
        ShareSDK.stopSDK(this);
    }

    @Override
    public void initView() {
        mCbLoadingPwdLook.setChecked(false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.login;
    }

    @Override
    public void initData() {
        mTopTitle.setText(getString(R.string.login));
        ShareSDK.initSDK(this);

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                setClicks();
                switch (msg.arg1) {
                    case 1: // 成功
                        /**
                         *   /v1.0/user/third
                         b)	请求方式:POST
                         c)	请求参数说明：
                         openID:唯一标识  avatar:头像  name: 昵称
                         type : 第三方类型：1:qq2 :wechar 3：新浪
                         备注：QQ，wechat:  openeid, nickname, figureurl_qq_2  SINA: avatar_hd, screen_name,id
                         registrationID	string		是	极光推送唯一标识
                         */
                        mLoadingAnimal.setVisibility(View.VISIBLE);

                        ThreeLoginElement element = new ThreeLoginElement();
                        element.setOpenID(mUserId);
                        element.setAvatar(mUserIcon);
                        element.setName(mUserName);
                        element.setType(mLoginTag + "");
                        element.setRegistrationID(JPushInterface.getRegistrationID(LoginActivity.this));

                        SSQSApplication.apiClient(classGuid).threeLogin(element, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    LoadingBean bean = (LoadingBean) result.getData();

                                    if (bean != null) {
                                        try {
                                            UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, bean.userType);

                                            Gson gson = new Gson();

                                            UIUtils.getSputils().putString(Constent.GLODS, bean.banlance + "");
                                            UIUtils.getSputils().putString(Constent.DIAMONDS, bean.diamond + "");
                                            UIUtils.getSputils().putBoolean(Constent.USER_TYPE, bean.userType == 3);
                                            UIUtils.getSputils().putInt(Constent.IS_VIP, bean.isVip);
                                            Logger.d(TAG, "返回金币:" + UIUtils.getSputils().getString(Constent.GLODS, null) + "钻石" + UIUtils.getSputils().getString(Constent.DIAMONDS, null));

                                            goSP(new JSONObject(gson.toJson(bean, LoadingBean.class)), bean.authToken);
                                        } catch (Exception ex) {
                                            Logger.e(TAG, "json failure");
                                        }
                                    }
                                } else {
                                    mLoadingAnimal.setVisibility(View.GONE);
                                    TmtUtils.midToast(UIUtils.getContext(), "授权失败,请重新登录..", 0);
                                }
                            }
                        });
                        break;
                    case 2: // 失败
                        TmtUtils.midToast(UIUtils.getContext(), "授权失败..", 0);
                        return false;

                    case 3: // 取消
                        TmtUtils.midToast(UIUtils.getContext(), "授权取消..", 0);
                        return false;

                }
                return false;
            }
        });
    }

    private void setClicks() {
        mLoadingIvWxLoading.setClickable(true);
        mLoadingIvQqLoading.setClickable(true);
        mLoadingIvXlLoading.setClickable(true);
    }

    private void setClicked() {
        mLoadingIvWxLoading.setClickable(false);
        mLoadingIvQqLoading.setClickable(false);
        mLoadingIvXlLoading.setClickable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setClicks();
    }

    @Override
    public void initListener() {
        /**
         * 焦点监听
         */
        mEtLoadingNumber.setOnFocusChangeListener(this);
        mEtLoadingPwd.setOnFocusChangeListener(this);

        /**
         * text监听
         */
        mEtLoadingNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mIvLoadingNumberClose.setVisibility(View.VISIBLE);
                } else {
                    mIvLoadingNumberClose.setVisibility(View.GONE);
                }
            }
        });
        mEtLoadingPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mEtLoadingPwdClose.setVisibility(View.VISIBLE);
                } else {
                    mEtLoadingPwdClose.setVisibility(View.GONE);
                    mLoadingButton.setBackgroundResource(R.mipmap.register);
                }
                if (!s.toString().equals("") && !mEtLoadingNumber.getText().toString().equals("")) {
                    mLoadingButton.setBackgroundResource(R.mipmap.register_sel);
                } else {
                    mLoadingButton.setBackgroundResource(R.mipmap.register);
                }
            }
        });
    }

    /**
     * 处理从授权页面返回的结果：异常则给出提示，正常则跳转页面
     */

    @OnClick({R.id.top_back, R.id.et_loading_pwd_look, R.id.loading_button, R.id.loading_tv_new_user_phone,
            R.id.loading_tv_new_user_number, R.id.loading_tv_forgot_pwd, R.id.iv_loading_number_close, R.id.et_loading_pwd_close,
            R.id.loading_iv_wx_loading, R.id.loading_iv_qq_loading, R.id.loading_iv_xl_loading, R.id.loading_button_try_play})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loading_iv_wx_loading:
                setClicked();

                mLoginTag = 2;
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.authorize();
                wechat.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Message msg = new Message();
                        msg.arg1 = 1;
                        msg.obj = platform;
                        PlatformDb platDB = platform.getDb();
                        mUserIcon = platDB.getUserIcon();
                        mUserId = platDB.getUserId();
                        mUserName = platDB.getUserName();
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Message msg = new Message();
                        msg.arg1 = 2;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Message msg = new Message();
                        msg.arg1 = 3;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            case R.id.loading_iv_qq_loading:
                mLoginTag = 1;
                setClicked();
                Logger.d(TAG, "qq登陆");
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.authorize();
                qq.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Message msg = new Message();
                        msg.arg1 = 1;
                        msg.obj = platform;
                        //用户资源都保存到res
                        //通过打印res数据看看有哪些数据是你想要的
                        PlatformDb platDB = platform.getDb();//获取数平台数据DB
                        //通过DB获取各种数据
                        mUserIcon = platDB.getUserIcon();
                        mUserId = platDB.getUserId();
                        mUserName = platDB.getUserName();
                        Logger.d(TAG, "qq的token是---------------------:" + platDB.getToken() + " gender:" + platDB.getUserGender() + " usericon:" + platDB.getUserIcon() + " id:" + platDB.getUserId() + " username:" + platDB.getUserName());
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Message msg = new Message();
                        msg.arg1 = 2;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Message msg = new Message();
                        msg.arg1 = 3;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            case R.id.loading_iv_xl_loading:
                mLoginTag = 3;
                Logger.d(TAG, "新浪登陆:");
                setClicked();
                Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                weibo.SSOSetting(false);
                weibo.authorize();
                // weibo.showUser(null);
                weibo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Message msg = new Message();
                        msg.arg1 = 1;
                        msg.obj = platform;
                        PlatformDb platDB = platform.getDb();
                        mUserIcon = platDB.getUserIcon();
                        mUserId = platDB.getUserId();
                        mUserName = platDB.getUserName();

                        Logger.d(TAG, "qq的token是-----" + platDB.getToken() + " gender:" + platDB.getUserGender()
                                + " usericon:" + platDB.getUserIcon() + " id:" + platDB.getUserId() + " username:" + platDB.getUserName());
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        Message msg = new Message();
                        msg.arg1 = 2;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                        Message msg = new Message();
                        msg.arg1 = 3;
                        msg.obj = platform;
                        mHandler.sendMessage(msg);
                    }
                });
                break;
            case R.id.top_back:
                Logger.d(TAG, "我被点击了:返回");
                UIUtils.hideKeyBord(this);
                finish();
                break;
            case R.id.iv_loading_number_close:
                mEtLoadingNumber.setText(null);
                mLoadingButton.setBackgroundResource(R.mipmap.register);
                break;
            case R.id.et_loading_pwd_close:
                mEtLoadingPwd.setText(null);
                mLoadingButton.setBackgroundResource(R.mipmap.register);
                break;

            case R.id.et_loading_pwd_look:
                Logger.d(TAG, "我被点击了查看");

                if (mCbLoadingPwdLook.isChecked()) {//显示密码
                    mEtLoadingPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtLoadingPwd.setSelection(mEtLoadingPwd.getText().toString().trim().length());
                } else {//隐藏密码
                    mEtLoadingPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEtLoadingPwd.setSelection(mEtLoadingPwd.getText().toString().trim().length());
                }

                int numLength = mEtLoadingNumber.getText().length();
                int pwdLength = mEtLoadingPwd.getText().length();
                if (numLength > 0 && pwdLength > 0) {
                    mLoadingButton.setBackgroundResource(R.mipmap.register_sel);
                } else {
                    mLoadingButton.setBackgroundResource(R.mipmap.register);
                }
                break;

            case R.id.loading_button:

                final String UserNumber = mEtLoadingNumber.getText().toString();
                final String UserPwd = mEtLoadingPwd.getText().toString();
                if (TextUtils.isEmpty(UserNumber)) {
                    TmtUtils.midToast(LoginActivity.this, "账号不能为空!", 0);
                    return;
                }
                if (TextUtils.isEmpty(UserPwd)) {
                    TmtUtils.midToast(LoginActivity.this, "密码不能为空!", 0);
                    return;
                }

                mLoadingAnimal.setVisibility(View.VISIBLE);

                UIUtils.hideKeyBord(this);
                /**
                 * 上传登陆信息
                 * registrationID	string		是	极光推送唯一标识
                 */
                LoginElement element = new LoginElement();
                element.setMobile(UserNumber);
                element.setPassword(UserPwd);
                element.setRegistrationID(JPushInterface.getRegistrationID(LoginActivity.this));

                SSQSApplication.apiClient(classGuid).login(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            LoadingBean bean = (LoadingBean) result.getData();

                            if (bean != null) {
                                try {
                                    UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, bean.userType);
                                    UIUtils.getSputils().putString(Constent.GLODS, bean.banlance + "");
                                    UIUtils.getSputils().putString(Constent.DIAMONDS, bean.diamond + "");
                                    UIUtils.getSputils().putBoolean(Constent.USER_TYPE, bean.userType == 3);
                                    UIUtils.getSputils().putInt(Constent.IS_VIP, bean.isVip);

                                    Gson gson = new Gson();
                                    goSP(new JSONObject(gson.toJson(bean, LoadingBean.class)), bean.authToken);
                                } catch (Exception ex) {
                                    Logger.e(TAG, "up json failure");
                                }
                            }
                        } else {
                            mLoadingAnimal.setVisibility(View.GONE);
                            TmtUtils.midToast(UIUtils.getContext(), "账户或密码错误..", 0);
                        }
                    }
                });
                mEtLoadingNumber.setText(null);
                mEtLoadingPwd.setText(null);
                mLoadingButton.setBackgroundResource(R.mipmap.register);
                break;

            case R.id.loading_button_try_play:
                Intent intentTryPlay = new Intent(this, UserTrialGameActivity.class);
                startActivity(intentTryPlay);
                finish();
                break;
            case R.id.loading_tv_new_user_phone:
                UIUtils.hideKeyBord(this);
                Intent intentP = new Intent(this, RegisterActivity.class);
                startActivity(intentP);
                finish();
                break;
            case R.id.loading_tv_new_user_number:
                UIUtils.hideKeyBord(this);
                Intent intent = new Intent(this, RegisterNumberActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.loading_tv_forgot_pwd:
                UIUtils.hideKeyBord(this);
                Intent intent3 = new Intent(this, ForgetPwdActivity.class);
                startActivity(intent3);
                finish();
                break;
            default:
                break;
        }
    }

    private void goSP(JSONObject bean, String authToken) {
        //发送广播
        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
        UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
        UIUtils.SendReRecevice(Constent.GQ_RECEVICE);

        Logger.d(TAG, "返回金币:" + UIUtils.getSputils().getString(Constent.GLODS, null) + "钻石" + UIUtils.getSputils().getString(Constent.DIAMONDS, null));
        UIUtils.getSputils().putString(Constent.LOADING_STATE_SP, bean.toString());
        UIUtils.getSputils().putString(Constent.TOKEN, authToken);
        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);
        Logger.d(TAG, "令牌----------" + authToken + "登陆状态是" + true);
        finish();
        mLoadingAnimal.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_loading_number:
                if (hasFocus)
                    mEtLoadingNumberIcon.setImageResource(R.mipmap.reg_phone_select);
                else
                    mEtLoadingNumberIcon.setImageResource(R.mipmap.reg_phone);
                break;
            case R.id.et_loading_pwd:
                if (hasFocus)
                    mEtLoadingPwdIcon.setImageResource(R.mipmap.r_reg_lock_select);
                else
                    mEtLoadingPwdIcon.setImageResource(R.mipmap.r_reg_lock);
                break;
            default:
                break;
        }
    }

}
