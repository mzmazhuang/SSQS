package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.SendYZMElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PhoneUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;



import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/18 14:53
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RegisterActivity extends BaseActivity implements View.OnFocusChangeListener, TextWatcher {
    private static final String TAG = "RegisterActivity";
    @Bind(R.id.regiset_phone_number_icon)
    ImageView mPhoneNumberIcon;
    @Bind(R.id.regiset_phone_number)
    EditText mPhoneNumber;
    @Bind(R.id.regiset_phone_code)
    TextView mPhoneCode;

    @Bind(R.id.top_title)
    TextView mTopTitle;

    private String mMPhoneNumberText;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initData() {
        super.initData();
        mTopTitle.setText(getString(R.string.regiset));
    }

    @Override
    protected void initListener() {
        mPhoneNumber.setOnFocusChangeListener(this);
        mPhoneNumber.addTextChangedListener(this);
    }

    @OnClick({R.id.regiset_loading, R.id.regiset_phone_code, R.id.top_back})
    public void onclick(View v) {
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.regiset_phone_code:
                /**
                 * 注册跳转进第二部
                 */
                mPhoneCode.setClickable(false);
                mMPhoneNumberText = mPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(mMPhoneNumberText) || !UIUtils.isMobileNO(mMPhoneNumberText)) {
                    ToastUtils.midToast(RegisterActivity.this, "請輸入您的手机号码!", 0);
                    return;
                }
                Logger.d(TAG, mMPhoneNumberText + "这是手机号");

                SendYZMElement element=new SendYZMElement();
                element.setMobile(mMPhoneNumberText);

                SSQSApplication.apiClient(classGuid).sendYZM(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mPhoneCode.setClickable(true);

                        if (result.isOk()) {
                            Intent intent = new Intent(RegisterActivity.this, RegisterSecondActivity.class);
                            intent.putExtra("mobile", mMPhoneNumberText);
                            startActivity(intent);
                            finish();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            case R.id.regiset_loading:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mPhoneNumberIcon.setImageResource(R.mipmap.reg_phone_select);
        } else {
            mPhoneNumberIcon.setImageResource(R.mipmap.reg_phone);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!PhoneUtils.isPhoneNumber(mPhoneNumber.getText().toString())) {
            mPhoneCode.setBackgroundResource(R.mipmap.register);
            mPhoneCode.setClickable(false);
        } else {
            mPhoneCode.setBackgroundResource(R.mipmap.register_sel);
            mPhoneCode.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Logger.d(TAG, s.toString());
    }
}
