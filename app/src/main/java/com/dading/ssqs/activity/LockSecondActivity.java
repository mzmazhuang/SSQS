package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.SendBindPhoneYZMElement;
import com.dading.ssqs.apis.elements.UserBindElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/18 17:50
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LockSecondActivity extends BaseActivity implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener, TextWatcher {
    private static final String TAG = "LockSecondActivity";
    @Bind(R.id.lock_phone_second_code_icon)
    ImageView mSecondCodeIcon;
    @Bind(R.id.lock_phone_second_code)
    EditText mSecondCode;

    @Bind(R.id.lock_phone_second_pwd_icon)
    ImageView mSecondPwdIcon;
    @Bind(R.id.lock_phone_second_pwd)
    EditText mSecondPwd;
    @Bind(R.id.lock_phone_second_pwd_look)
    CheckBox mSecondPwdLook;

    @Bind(R.id.lock_phone_second_lock_phonebutton)
    TextView mSecondRegisetButton;
    @Bind(R.id.lock_phone_second_sendcode)
    TextView mSecondSendCode;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private String mPhone;
    private String mCode;
    private static int mTime;
    private String mS;
    private Runnable mR;

    @Override
    protected void setUnDe() {
        UIUtils.removeTaskAll(null);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_lock_phone_second_step;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.lock_phone));
        //获取上一步的手机号
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("mobile");
        Logger.d(TAG, "手机号码:" + mPhone);
        mSecondSendCode.setClickable(false);
        mTime = 180;
        mS = "180s";
        mSecondSendCode.setText(mS);
        mSecondSendCode.setTextColor(getResources().getColor(R.color.gray));
        mR = new Runnable() {
            @Override
            public void run() {
                if (mTime > 0) {
                    --mTime;
                    String s = mTime + "s";
                    mSecondSendCode.setText(s);
                    mSecondSendCode.setTextColor(ContextCompat.getColor(LockSecondActivity.this, R.color.gray));
                    mSecondCode.setClickable(false);
                    UIUtils.postTaskDelay(this, 1000);
                } else {
                    mSecondSendCode.setText(LockSecondActivity.this.getString(R.string.send_again));
                    mSecondSendCode.setTextColor(ContextCompat.getColor(LockSecondActivity.this, R.color.orange));
                    mSecondSendCode.setClickable(true);
                    UIUtils.removeTask(this);
                }
            }
        };
        UIUtils.postTaskDelay(mR, 1000);
    }

    @Override
    protected void initListener() {

        mSecondCode.setOnFocusChangeListener(this);
        mSecondPwd.setOnFocusChangeListener(this);

        mSecondCode.addTextChangedListener(this);
        mSecondPwd.addTextChangedListener(this);

        mSecondPwdLook.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.top_back, R.id.lock_phone_second_sendcode,
            R.id.lock_phone_second_lock_phonebutton})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.lock_phone_second_sendcode:
                SendBindPhoneYZMElement element = new SendBindPhoneYZMElement();
                element.setMobile(mPhone);

                SSQSApplication.apiClient(classGuid).sendBindPhoneYZM(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            mTime = 180;
                            mSecondSendCode.setText(mS);
                            mSecondSendCode.setTextColor(getResources().getColor(R.color.gray));
                            mSecondCode.setClickable(false);
                            UIUtils.postTaskDelay(mR, 1000);
                            Logger.d(TAG, "验证码已发送!接收后请输入验证码...------------------------------:");
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(LockSecondActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            case R.id.lock_phone_second_lock_phonebutton:
                mSecondRegisetButton.setClickable(false);
                /**
                 * a)	请求地址：
                 /v1.0/user/bind
                 b)	请求方式:
                 POST
                 c)	请求参数说明：
                 mobile:手机号码
                 openID:唯一标识
                 avatar:头像
                 name: 昵称
                 authCode:验证码
                 password:新密码
                 type : 类型：0:手机1:qq2 :wechar 3：新浪
                 auth_token：登陆后加入请求头
                 */
                String password = mSecondPwd.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    TmtUtils.midToast(LockSecondActivity.this, "请输入密码!", 0);
                    return;
                }
                if (password.length() < 6) {
                    TmtUtils.midToast(LockSecondActivity.this, "密码长度不得少于六位!", 0);
                    return;
                } else if (password.length() > 32) {
                    TmtUtils.midToast(LockSecondActivity.this, "密码长度不得大于三十二位!", 0);
                    return;
                }
                mCode = mSecondCode.getText().toString();
                if (TextUtils.isEmpty(mCode)) {
                    TmtUtils.midToast(LockSecondActivity.this, "请输入验证码!", 0);
                    return;
                }
                mSecondRegisetButton.setFocusable(true);
                Logger.d(TAG, "验证码:" + mCode);

                UserBindElement userBindElement = new UserBindElement();
                userBindElement.setMobile(mPhone);
                userBindElement.setPassword(password);
                userBindElement.setType("0");
                userBindElement.setAuthCode(mCode);

                SSQSApplication.apiClient(classGuid).bindUserInfo(userBindElement, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mSecondRegisetButton.setClickable(true);
                        if (result.isOk()) {
                            TmtUtils.midToast(UIUtils.getContext(), "绑定手机成功,即将跳转到绑定界面请稍后...", 0);
                            UIUtils.postTaskDelay(new Runnable() {
                                @Override
                                public void run() {
                                    goLoading();
                                }
                            }, 3000);
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(LockSecondActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void goLoading() {
        mSecondRegisetButton.setClickable(false);
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.lock_phone_second_code:
                if (hasFocus) {
                    mSecondCodeIcon.setImageResource(R.mipmap.r_reg_code_select);
                } else {
                    mSecondCodeIcon.setImageResource(R.mipmap.r_reg_code);
                }
                break;
            case R.id.lock_phone_second_pwd:
                if (hasFocus) {
                    mSecondPwdIcon.setImageResource(R.mipmap.reg_lock_select);
                } else {
                    mSecondPwdIcon.setImageResource(R.mipmap.reg_lock);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 查看密码
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.lock_phone_second_pwd_look:
                if (isChecked) {//显示密码
                    mSecondPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mSecondPwd.setSelection(mSecondPwd.getText().toString().trim().length());
                } else {//隐藏密码
                    mSecondPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mSecondPwd.setSelection(mSecondPwd.getText().toString().trim().length());
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int numLength = mSecondCode.getText().length();
        int pwdLength = mSecondPwd.getText().length();
        if (numLength > 0 && pwdLength > 0) {
            mSecondRegisetButton.setBackgroundResource(R.mipmap.register_sel);
        } else {
            mSecondRegisetButton.setBackgroundResource(R.mipmap.register);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
