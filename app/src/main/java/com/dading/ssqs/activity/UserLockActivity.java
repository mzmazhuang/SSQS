package com.dading.ssqs.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.UserBindElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.google.gson.Gson;


import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/20 15:29
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class UserLockActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "UserLockActivity";
    @Bind(R.id.phone_lock_state)
    TextView mPhoneLockState;
    @Bind(R.id.qq_lock_state)
    TextView mQqLockState;
    @Bind(R.id.wx_lock_state)
    TextView mWxLockState;
    @Bind(R.id.wb_lock_state)
    TextView mWbLockState;
    @Bind(R.id.phone_lock_arrow)
    ImageView mPhoneLockArrow;
    @Bind(R.id.qq_lock_arrow)
    ImageView mQqLockArrow;
    @Bind(R.id.wx_lock_arrow)
    ImageView mWxLockArrow;
    @Bind(R.id.wb_lock_arrow)
    ImageView mWbLockArrow;
    @Bind(R.id.phone_ly_lock)
    RelativeLayout mPhoneLyLock;
    @Bind(R.id.qq_ly_lock)
    RelativeLayout mQqLyLock;
    @Bind(R.id.phone_iv_wx)
    ImageView mPhoneIvWx;
    @Bind(R.id.wx_ly_lock)
    RelativeLayout mWxLyLock;
    @Bind(R.id.phone_iv_wo)
    ImageView mPhoneIvWo;
    @Bind(R.id.wo_ly_lock)
    RelativeLayout mWoLyLock;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private LoadingBean mBean;
    private RelativeLayout mPopLy;
    private TextView mPopText;
    private TextView mPopKnow;
    private View mPopView;
    private PopupWindow mPop;
    private Handler mHandler;
    private String mUserIcon;
    private String mUserId;
    private String mUserName;
    private int mLoginTag;
    private View mView;

    @Override
    public void reDisPlay() {

    }


    @Override
    protected void setUnDe() {
        super.setUnDe();
        if (mHandler != null)
            mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.activity_user_lock, null);

        mPopView = View.inflate(this, R.layout.lock_pop, null);
        mPopLy = (RelativeLayout) mPopView.findViewById(R.id.lock_popu_ly);
        mPopText = (TextView) mPopView.findViewById(R.id.lock_popu_text);
        mPopKnow = (TextView) mPopView.findViewById(R.id.lock_popu_know);

        mPop = PopUtil.popuMake(mPopView);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_lock;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.account_num));

        SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mBean = (LoadingBean) result.getData();

                    if (mBean != null) {
                        Gson gson = new Gson();

                        UIUtils.getSputils().putString(Constent.LOADING_STATE_SP, gson.toJson(mBean, LoadingBean.class));

                        UIUtils.getSputils().putString(Constent.TOKEN, mBean.authToken);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);
                        UIUtils.getSputils().putString(Constent.GLODS, mBean.banlance + "");
                        UIUtils.getSputils().putString(Constent.DIAMONDS, mBean.diamond + "");
                        processData(mBean);
                    } else {
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(UserLockActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message msg) {
                Platform platform = (Platform) msg.obj;
                switch (msg.arg1) {
                    case 1: { // 成功
                        /**
                         a)	请求地址：
                         /v1.0/user/bind
                         b)	请求方式:
                         POST
                         c)	请求参数说明：
                         mobile:手机号码
                         openID:唯一标识
                         avatar:头像
                         name: 昵称
                         type : 类型：0:手机1:qq2 :wechar 3：新浪
                         auth_token：登陆后加入请求头
                         */
                        UserBindElement element = new UserBindElement();
                        element.setType(mLoginTag + "");
                        if (mLoginTag == 0) {
                            element.setMobile("手机号");
                        } else {
                            element.setOpenID(mUserId);
                            element.setAvatar(mUserIcon);
                            element.setName(mUserName);
                        }
                        SSQSApplication.apiClient(classGuid).bindUserInfo(element, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                mQqLockArrow.setClickable(true);
                                mWbLockArrow.setClickable(true);
                                mPhoneLockArrow.setClickable(true);
                                mWxLockArrow.setClickable(true);

                                if (result.isOk()) {
                                    LoadingBean bean = (LoadingBean) result.getData();

                                    if (bean != null) {
                                        switch (mLoginTag) {
                                            case 0:
                                                mPhoneLockArrow.setVisibility(View.GONE);
                                                mPhoneLockState.setText("手机号");
                                                break;
                                            case 1:
                                                mQqLockArrow.setVisibility(View.GONE);
                                                mQqLockState.setText(mUserName);
                                                break;
                                            case 2:
                                                mWxLockArrow.setVisibility(View.GONE);
                                                mWxLockState.setText(mUserName);
                                                break;
                                            case 3:
                                                mWbLockArrow.setVisibility(View.GONE);
                                                mWbLockState.setText(mUserName);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(UserLockActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                    }
                                }
                            }
                        });
                    }
                    break;
                    case 2: // 失败
                        TmtUtils.midToast(UIUtils.getContext(), platform.getName() + "授权失败", 0);
                        mQqLockArrow.setClickable(true);
                        mWbLockArrow.setClickable(true);
                        mPhoneLockArrow.setClickable(true);
                        mWxLockArrow.setClickable(true);
                        return false;

                    case 3: // 取消
                        TmtUtils.midToast(UIUtils.getContext(), platform.getName() + "授权取消", 0);
                        mQqLockArrow.setClickable(true);
                        mWbLockArrow.setClickable(true);
                        mPhoneLockArrow.setClickable(true);
                        mWxLockArrow.setClickable(true);
                        return false;

                }
                return false;
            }
        });
    }

    private void processData(LoadingBean bean) {
        if (bean.mobile == null || bean.mobile.equals("")) {
            mPhoneLockState.setText("未绑定");
        } else {
            mPhoneLockArrow.setVisibility(View.GONE);
            mPhoneLockState.setText(bean.mobile);
        }
        if (bean.qqName == null || bean.qqName.equals("")) {
            mQqLockState.setText("未绑定");
        } else {
            mQqLockArrow.setVisibility(View.GONE);
            mQqLockState.setText(bean.qqName);
        }
        if (bean.wechatName == null || bean.wechatName.equals("")) {
            mWxLockState.setText("未绑定");
        } else {
            mWxLockArrow.setVisibility(View.GONE);
            mWxLockState.setText(bean.wechatName);
        }
        if (bean.sinaName == null || bean.sinaName.equals("")) {
            mWbLockState.setText("未绑定");
        } else {
            mWbLockArrow.setVisibility(View.GONE);
            mWbLockState.setText(bean.sinaName);
        }
    }

    @Override
    protected void initListener() {
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPhoneLyLock.setClickable(true);
            }
        });
        mPopLy.setOnClickListener(this);
        mPopKnow.setOnClickListener(this);
    }

    @OnClick({R.id.top_back, R.id.phone_ly_lock, R.id.qq_ly_lock, R.id.wx_ly_lock, R.id.wo_ly_lock})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.phone_ly_lock:
                mQqLockArrow.setClickable(false);
                mWbLockArrow.setClickable(false);
                mPhoneLockArrow.setClickable(false);
                mWxLockArrow.setClickable(false);
                if (TextUtils.isEmpty(mBean.mobile)) {
                    //跳转绑定手机的界面
                    mLoginTag = 0;
                    Intent intent = new Intent(this, LockPhoneActivity.class);
                    startActivity(intent);
                    LogUtil.util(TAG, "跳转进绑定手机界面------------------------------:");
                } else {
                    mPopText.setText("当前登录的账号不可以解绑\r\n如需解绑,请先使用其它账号登录");

                    mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                    mPhoneLyLock.setClickable(false);
                }
                break;
            case R.id.qq_ly_lock:
                mQqLockArrow.setClickable(false);
                mWbLockArrow.setClickable(false);
                mPhoneLockArrow.setClickable(false);
                mWxLockArrow.setClickable(false);
                if (TextUtils.isEmpty(mBean.qqName)) {
                    mLoginTag = 1;
                    LogUtil.util(TAG, "qq登陆");
                    Platform qq = ShareSDK.getPlatform(this, QQ.NAME);
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

                            LogUtil.util(TAG, "qq的token是---------------------:" + platDB.getToken() + " gender:" + platDB.getUserGender()
                                    + " usericon:" + platDB.getUserIcon() + " id:" + platDB.getUserId() + " username:" + platDB.getUserName()
                                    + " getPlatformNname:" + platDB.getPlatformNname() + " getExpiresIn:" + platDB.getExpiresIn() + " getTokenSecret" + platDB.getTokenSecret()
                                    + " " + platDB.getExpiresTime());
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
                } else {
                    mPopText.setText("当前登录的账号不可以解绑\r\n如需解绑,请先使用其它账号登录");
                    mPop = PopUtil.popuMake(mPopView);
                    mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.wx_ly_lock:
                mQqLockArrow.setClickable(false);
                mWbLockArrow.setClickable(false);
                mPhoneLockArrow.setClickable(false);
                mWxLockArrow.setClickable(false);
                if (TextUtils.isEmpty(mBean.wechatName)) {
                    mLoginTag = 2;
                    LogUtil.util(TAG, "微信登陆");
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
                            LogUtil.util(TAG, "qq的token是----------------------:" + platDB.getToken() + " gender:" + platDB.getUserGender()
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
                } else {
                    mPopText.setText("当前登录的账号不可以解绑\r\n如需解绑,请先使用其它账号登录");
                    mPop = PopUtil.popuMake(mPopView);
                    mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                }
                break;
            case R.id.wo_ly_lock:
                mQqLockArrow.setClickable(false);
                mWbLockArrow.setClickable(false);
                mPhoneLockArrow.setClickable(false);
                mWxLockArrow.setClickable(false);
                if (TextUtils.isEmpty(mBean.sinaName)) {
                    mLoginTag = 3;
                    LogUtil.util(TAG, "新浪登陆:");
                    Platform weibo = ShareSDK.getPlatform(this, SinaWeibo.NAME);
                    weibo.SSOSetting(false);
                    weibo.authorize();
                    weibo.showUser(null);
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

                            LogUtil.util(TAG, "qq的token是------------------------------:" + platDB.getToken() + " gender:" + platDB.getUserGender()
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
                } else {
                    mPopText.setText("当前登录的账号不可以解绑\r\n如需解绑,请先使用其它账号登录");
                    mPop = PopUtil.popuMake(mPopView);
                    mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        mPop.dismiss();
    }
}
