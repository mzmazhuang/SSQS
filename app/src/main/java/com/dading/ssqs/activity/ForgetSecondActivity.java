package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ForgetSecondPasswordElement;
import com.dading.ssqs.apis.elements.ForgetUserPasswordElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/20 17:48
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ForgetSecondActivity extends BaseActivity implements View.OnFocusChangeListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "ForgetSecondActivity";
    @Bind(R.id.forget_second_code_icon)
    ImageView mForgetSecondCodeIcon;
    @Bind(R.id.forget_second_code)
    EditText mForgetSecondCode;
    @Bind(R.id.forget_second_sendcode)
    TextView mForgetSecondSendcode;
    @Bind(R.id.forget_second_pwd_icon)
    ImageView mForgetSecondPwdIcon;
    @Bind(R.id.forget_second_pwd)
    EditText mForgetSecondPwd;
    @Bind(R.id.forget_second_pwd_look)
    CheckBox mForgetSecondPwdLook;
    @Bind(R.id.forget_second_forgetbutton)
    TextView mForgetSecondForgetbutton;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private String mPhone;
    private int mTime;
    private Runnable mR;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_second_forget;
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.removeTaskAll(null);
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.look_for_pwd));
        //获取上一步的手机号
        Intent intent = getIntent();
        mPhone = intent.getStringExtra("mobile");
        LogUtil.util(TAG, "手机号码:" + mPhone);
        mForgetSecondSendcode.setClickable(false);
        mTime = 180;
        final String s = mTime + "s";
        mForgetSecondSendcode.setText(s);
        mR = new Runnable() {
            @Override
            public void run() {
                if (mTime > 0) {
                    mTime--;
                    String s = mTime + "s";
                    mForgetSecondSendcode.setText(s);
                    mForgetSecondSendcode.setTextColor(ContextCompat.getColor(ForgetSecondActivity.this, R.color.gray));
                    mForgetSecondSendcode.setClickable(false);
                    UIUtils.postTaskDelay(this, 1000);
                } else {
                    UIUtils.removeTask(this);
                    mForgetSecondSendcode.setText(getString(R.string.send_again));
                    mForgetSecondSendcode.setTextColor(ContextCompat.getColor(ForgetSecondActivity.this, R.color.orange));
                    mForgetSecondSendcode.setClickable(true);
                }

            }
        };
        UIUtils.postTaskDelay(mR, 1000);
    }

    @Override
    protected void initListener() {
        mForgetSecondCode.setOnFocusChangeListener(this);
        mForgetSecondPwd.setOnFocusChangeListener(this);
        mForgetSecondCode.addTextChangedListener(this);
        mForgetSecondPwd.addTextChangedListener(this);
        mForgetSecondPwdLook.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.top_back, R.id.forget_second_forgetbutton, R.id.forget_second_forget_loading
            , R.id.forget_second_sendcode})
    public void OnClik(View v) {
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;

            case R.id.forget_second_forgetbutton:
                /**
                 * 8.	忘记密码
                 a)	请求地址：
                 /v1.0/user/forget
                 b)	请求方式:
                 POST
                 c)	请求参数说明：
                 mobile:手机号码
                 password:密码
                 code: 验证码
                 d)	返回格式
                 {"status":true,"code":0,"msg":"","data":null}
                 */
                ForgetSecondPasswordElement forgetSecondPasswordElement = new ForgetSecondPasswordElement();
                forgetSecondPasswordElement.setMobile(mPhone);
                forgetSecondPasswordElement.setPassword(mForgetSecondPwd.getText().toString());
                forgetSecondPasswordElement.setCode(mForgetSecondCode.getText().toString());

                SSQSApplication.apiClient(classGuid).forgetSecondPassword(forgetSecondPasswordElement, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            TmtUtils.midToast(ForgetSecondActivity.this, "找回密码成功!", 0);

                            Intent intent = new Intent(ForgetSecondActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            TmtUtils.midToast(ForgetSecondActivity.this, result.getMessage(), 0);
                        }
                    }
                });
                break;
            case R.id.forget_second_forget_loading:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_second_sendcode:
                LogUtil.util(TAG, "mForgetSecondSendcode返回数据是------------------------------:" + mForgetSecondSendcode.getText().toString());
                if (mForgetSecondSendcode.getText().toString().equals(getString(R.string.send_again))) {
                    ForgetUserPasswordElement element = new ForgetUserPasswordElement();
                    element.setMobile(mPhone);

                    SSQSApplication.apiClient(classGuid).forgetUserPassword(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                mTime = 180;
                                String s = mTime + "s";
                                mForgetSecondSendcode.setText(s);
                                mForgetSecondSendcode.setTextColor(ContextCompat.getColor(ForgetSecondActivity.this, R.color.gray));
                                mForgetSecondSendcode.setClickable(false);
                                UIUtils.postTaskDelay(mR, 1000);
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(ForgetSecondActivity.this, LoginActivity.class);
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
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.regiset_second_code:
                if (hasFocus) {
                    mForgetSecondCodeIcon.setImageResource(R.mipmap.r_reg_code_select);
                } else {
                    mForgetSecondCodeIcon.setImageResource(R.mipmap.r_reg_code);
                }
                break;
            case R.id.regiset_second_pwd:
                if (hasFocus) {
                    mForgetSecondPwdIcon.setImageResource(R.mipmap.reg_lock_select);
                } else {
                    mForgetSecondPwdIcon.setImageResource(R.mipmap.reg_lock);
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
            case R.id.forget_second_pwd_look:
                if (isChecked) {//显示密码
                    mForgetSecondPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mForgetSecondPwd.setSelection(mForgetSecondPwd.getText().toString().trim().length());
                } else {//隐藏密码
                    mForgetSecondPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mForgetSecondPwd.setSelection(mForgetSecondPwd.getText().toString().trim().length());
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
        int numLength = mForgetSecondCode.getText().length();
        int pwdLength = mForgetSecondPwd.getText().length();
        if (numLength > 0 && pwdLength > 0) {
            mForgetSecondForgetbutton.setBackgroundResource(R.mipmap.register_sel);
        } else {
            mForgetSecondForgetbutton.setBackgroundResource(R.mipmap.register);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
