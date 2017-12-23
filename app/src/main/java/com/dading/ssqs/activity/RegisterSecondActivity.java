package com.dading.ssqs.activity;

import android.content.Intent;
import android.os.CountDownTimer;
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
import com.dading.ssqs.apis.elements.RegAccountElement;
import com.dading.ssqs.apis.elements.SendYZMElement;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
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
public class RegisterSecondActivity extends BaseActivity implements View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener, TextWatcher {
    private static final String TAG = "RegisterSecondActivity";
    @Bind(R.id.regiset_second_code_icon)
    ImageView mSecondCodeIcon;
    @Bind(R.id.regiset_second_code)
    EditText mSecondCode;

    @Bind(R.id.regiset_second_pwd_icon)
    ImageView mSecondPwdIcon;
    @Bind(R.id.regiset_second_pwd)
    EditText mSecondPwd;
    @Bind(R.id.regiset_second_pwd_look)
    CheckBox mSecondPwdLook;

    @Bind(R.id.regiset_second_regisetbutton)
    TextView mSecondRegisetButton;

    @Bind(R.id.regiset_second_allow_icon)
    CheckBox mSecondAllowIcon;
    @Bind(R.id.regiset_second_sendcode)
    TextView mSecondSendCode;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.regiset_invite_code)
    EditText mRegisetInviteCode;

    private boolean mIsAgree = true;
    private String mPhone;
    private String mCode;
    private int mTime;
    private CountDownTimer mCountDownTimer;
    private Runnable mR;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_regist_second_step;
    }

    @Override
    protected void setUnDe() {
        if (mCountDownTimer != null)
            mCountDownTimer.cancel();
        UIUtils.removeTask(mR);
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.regiset));
        mTime = 180;
        mCountDownTimer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                --mTime;
                String s = "(" + mTime + ")";
                mSecondSendCode.setText(s);
                mSecondSendCode.setTextColor(getResources().getColor(R.color.gray));
                mSecondSendCode.setClickable(false);
            }

            @Override
            public void onFinish() {
                mSecondSendCode.setText("重新发送");
                mSecondSendCode.setTextColor(getResources().getColor(R.color.blue_t1));
                mSecondSendCode.setClickable(true);
                mTime = 180;
            }
        };
        mCountDownTimer.start();
        //获取上一步的手机号
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("mobile");
        Logger.d(TAG, "手机号码:" + mPhone);
        mSecondAllowIcon.setChecked(true);
        mSecondSendCode.setClickable(false);
    }

    @Override
    protected void initListener() {

        mSecondCode.setOnFocusChangeListener(this);
        mSecondPwd.setOnFocusChangeListener(this);

        mSecondCode.addTextChangedListener(this);
        mSecondPwd.addTextChangedListener(this);

        mSecondPwdLook.setOnCheckedChangeListener(this);
        mSecondAllowIcon.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.top_back, R.id.regiset_second_regisetbutton,
            R.id.regiset_second_sendcode, R.id.regiset_second_protocol})
    public void onclick(View v) {
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.regiset_second_sendcode:
                SendYZMElement element = new SendYZMElement();
                element.setMobile(mPhone);

                SSQSApplication.apiClient(classGuid).sendYZM(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            mCountDownTimer.start();
                        } else {
                            ToastUtils.midToast(RegisterSecondActivity.this, result.getMessage(), 0);
                            Logger.d(TAG, "注册手机验证码请求失败!...------------------------------:");
                        }
                    }
                });
                break;
            case R.id.regiset_second_regisetbutton:
                mSecondRegisetButton.setClickable(false);
                if (!mIsAgree) {
                    ToastUtils.midToast(RegisterSecondActivity.this, "请选择是否同意注册协议...", 0);
                    return;
                }
                String password = mSecondPwd.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.midToast(RegisterSecondActivity.this, "请输入密码!", 0);
                    return;
                }
                if (password.length() < 6) {
                    ToastUtils.midToast(RegisterSecondActivity.this, "密码长度不得少于六位!", 0);
                    return;
                } else if (password.length() > 32) {
                    ToastUtils.midToast(RegisterSecondActivity.this, "密码长度不得大于三十二位!", 0);
                    return;
                }
                mCode = mSecondCode.getText().toString();
                if (TextUtils.isEmpty(mCode)) {
                    ToastUtils.midToast(RegisterSecondActivity.this, "请输入验证码!", 0);
                    return;
                }

                mSecondRegisetButton.setFocusable(true);

                RegAccountElement element1 = new RegAccountElement();
                element1.setMobile(mPhone);
                element1.setPassword(password);
                element1.setCode(mCode);
                element1.setAgentCode(mRegisetInviteCode.getText().toString());

                SSQSApplication.apiClient(classGuid).regAccount2(element1, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mSecondRegisetButton.setClickable(true);

                        if (result.isOk()) {
                            ToastUtils.midToast(UIUtils.getContext(), "注册成功,即将跳转到登陆界面...", 0);
                            mR = new Runnable() {
                                @Override
                                public void run() {
                                    mCountDownTimer.cancel();
                                    goLoading();
                                }
                            };
                            UIUtils.postTaskDelay(mR, 3000);
                        } else {
                            ToastUtils.midToast(RegisterSecondActivity.this, result.getMessage(), 0);
                        }
                    }
                });
                break;
            case R.id.regiset_second_protocol:
                //跳转金百盈足球协议
                Intent intent = new Intent(this, SSQSRegisterProtocolActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void goLoading() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        mSecondRegisetButton.setClickable(true);
        finish();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.regiset_second_code:
                if (hasFocus) {
                    mSecondCodeIcon.setImageResource(R.mipmap.r_reg_code_select);
                } else {
                    mSecondCodeIcon.setImageResource(R.mipmap.r_reg_code);
                }
                break;
            case R.id.regiset_second_pwd:
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
            case R.id.regiset_second_pwd_look:
                if (isChecked) {//显示密码
                    mSecondPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mSecondPwd.setSelection(mSecondPwd.getText().toString().trim().length());
                } else {//隐藏密码
                    mSecondPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mSecondPwd.setSelection(mSecondPwd.getText().toString().trim().length());
                }
                break;
            case R.id.regiset_second_allow_icon:
                //是否同意
                mIsAgree = isChecked;
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
