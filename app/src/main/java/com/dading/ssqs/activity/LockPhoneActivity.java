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
import com.dading.ssqs.apis.elements.SendBindPhoneYZMElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PhoneUtils;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;



import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/12/5 10:01
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LockPhoneActivity extends BaseActivity implements View.OnFocusChangeListener, TextWatcher {
    private static final String TAG = "LockPhoneActivity";
    @Bind(R.id.lock_phone_phone_number_icon)
    ImageView mLockPhonePhoneNumberIcon;
    @Bind(R.id.lock_phone_phone_number)
    EditText mLockPhonePhoneNumber;
    @Bind(R.id.lock_phone_phone_code)
    TextView mLockPhonePhoneCode;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private String mMPhoneNumberText;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_lock_phone;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.lock_phone));
        mLockPhonePhoneCode.setClickable(false);
    }

    @Override
    protected void initListener() {
        mLockPhonePhoneNumber.setOnFocusChangeListener(this);
        mLockPhonePhoneNumber.addTextChangedListener(this);
    }

    @OnClick({R.id.lock_phone_phone_code, R.id.top_back})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.lock_phone_phone_code:
                mLockPhonePhoneCode.setClickable(false);
                /**
                 * 发送绑定手机验证码
                 *a)	请求地址：
                 /v1.0/authCode/bind
                 b)	请求方式:
                 POST
                 c)	请求参数说明
                 mobile: 手机号
                 */
                mMPhoneNumberText = mLockPhonePhoneNumber.getText().toString();
                if (TextUtils.isEmpty(mMPhoneNumberText)) {
                    TmtUtils.midToast(LockPhoneActivity.this, "请输入手机号!", 0);
                    mLockPhonePhoneCode.setClickable(true);
                    return;
                }
                LogUtil.util(TAG, mMPhoneNumberText + "这是手机号");

                SendBindPhoneYZMElement element=new SendBindPhoneYZMElement();
                element.setMobile(mMPhoneNumberText);

                SSQSApplication.apiClient(classGuid).sendBindPhoneYZM(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            Intent intent = new Intent(LockPhoneActivity.this, LockSecondActivity.class);
                            intent.putExtra("mobile", mMPhoneNumberText);
                            startActivity(intent);
                            finish();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(LockPhoneActivity.this, LoginActivity.class);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mLockPhonePhoneNumberIcon.setImageResource(R.mipmap.reg_phone_select);
        } else {
            mLockPhonePhoneNumberIcon.setImageResource(R.mipmap.reg_phone);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!PhoneUtils.isPhoneNumber(mLockPhonePhoneNumber.getText().toString())) {
            mLockPhonePhoneCode.setBackgroundResource(R.mipmap.register);
            mLockPhonePhoneCode.setClickable(false);
        } else {
            mLockPhonePhoneCode.setBackgroundResource(R.mipmap.register_sel);
            mLockPhonePhoneCode.setClickable(true);
        }
        LogUtil.util(TAG, s.toString());
    }
}
