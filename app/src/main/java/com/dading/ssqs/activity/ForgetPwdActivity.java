package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ForgetUserPasswordElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PhoneUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/20 17:27
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ForgetPwdActivity extends BaseActivity implements View.OnFocusChangeListener, TextWatcher {
    private static final String TAG = "ForgetPwdActivity";
    @Bind(R.id.forget_phone_number_icon)
    ImageView mForgetPhoneNumberIcon;
    @Bind(R.id.forget_phone_number)
    EditText mForgetPhoneNumber;
    @Bind(R.id.forget_phone_code)
    TextView mForgetPhoneCode;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private String mPhoneNum;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.forgot_pwd));
        mForgetPhoneCode.setClickable(false);
    }

    @Override
    protected void initListener() {
        mForgetPhoneNumber.setOnFocusChangeListener(this);
        mForgetPhoneNumber.addTextChangedListener(this);
        mForgetPhoneCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mForgetPhoneCode.setBackgroundColor(ContextCompat.getColor(ForgetPwdActivity.this, R.color.gray));
                    mForgetPhoneCode.setClickable(false);
                } else {
                    mForgetPhoneCode.setBackgroundColor(ContextCompat.getColor(ForgetPwdActivity.this, R.color.orange));
                    mForgetPhoneCode.setClickable(true);
                }
            }
        });
    }

    @OnClick({R.id.forget_phone_code, R.id.top_back, R.id.forget_loading})
    public void onclick(View v) {
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.forget_phone_code:
                /**
                 * 找回密码转进第二部
                 */
                mPhoneNum = mForgetPhoneNumber.getText().toString();

                Logger.d(TAG, mPhoneNum + "这是手机号");

                ForgetUserPasswordElement element=new ForgetUserPasswordElement();
                element.setMobile(mPhoneNum);

                SSQSApplication.apiClient(classGuid).forgetUserPassword(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            Intent intent = new Intent(ForgetPwdActivity.this, ForgetSecondActivity.class);
                            intent.putExtra("mobile", mPhoneNum);
                            startActivity(intent);
                            finish();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(ForgetPwdActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                Logger.d(TAG, result.getMessage());
                            }
                        }
                    }
                });
                break;
            case R.id.forget_loading:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mForgetPhoneNumberIcon.setImageResource(R.mipmap.reg_phone_select);
        } else {
            mForgetPhoneNumberIcon.setImageResource(R.mipmap.reg_phone);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!PhoneUtils.isPhoneNumber(mForgetPhoneNumber.getText().toString())) {
            mForgetPhoneCode.setBackgroundResource(R.mipmap.register);
            mForgetPhoneCode.setClickable(false);
        } else {
            mForgetPhoneCode.setBackgroundResource(R.mipmap.register_sel);
            mForgetPhoneCode.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Logger.d(TAG, s.toString());
    }
}