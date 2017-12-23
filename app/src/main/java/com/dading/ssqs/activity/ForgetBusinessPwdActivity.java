package com.dading.ssqs.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ForgetPasswordElement;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/2 17:20
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ForgetBusinessPwdActivity extends BaseActivity {
    private static final String TAG = "ForgetBusinessPwdActivity";
    @Bind(R.id.forget_businsess_pwd_setting)
    EditText mForgetBusinsessPwdSetting;
    @Bind(R.id.forget_businsess_pwd_setting_confirm)
    EditText mForgetBusinsessPwdSettingConfirm;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.forget_businsess_old_pwd)
    EditText mForgetBusinsessOldPwd;


    @Override
    protected void initView() {
        mForgetBusinsessPwdSetting.setFocusable(true);
        mForgetBusinsessPwdSetting.setFocusableInTouchMode(true);
        mForgetBusinsessPwdSetting.requestFocus();
        mForgetBusinsessPwdSetting.requestFocusFromTouch();
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.forgot_business_pwd));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_forget_business_pwd;
    }

    @OnClick({R.id.top_back,/* R.id.forget_businsess_pwd_get_code,*/ R.id.forget_businsess_pwd_upload})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                UIUtils.hideKeyBord(this);
                finish();
                break;
            case R.id.forget_businsess_pwd_upload:
                UIUtils.hideKeyBord(this);

                String setPwd = mForgetBusinsessPwdSetting.getText().toString();
                String setPwdConfirm = mForgetBusinsessPwdSettingConfirm.getText().toString();
                // String setPwdCode = mForgetBusinsessPwdStateCode.getText().toString();
                // String setPwdPhone = mForgetBusinsessPwdPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(mForgetBusinsessOldPwd.getText().toString())) {
                    TmtUtils.midToast(this, "请输入您旧的交易密码!", 0);
                    return;
                }
                if (TextUtils.isEmpty(setPwd)) {
                    TmtUtils.midToast(this, "请设置您的交易密码!", 0);
                    return;
                }
                if (TextUtils.isEmpty(setPwdConfirm)) {
                    TmtUtils.midToast(this, "请确认设置的交易密码!", 0);
                    return;
                }
             /*   if (TextUtils.isEmpty(setPwdCode)) {
                    TmtUtils.midToast(this, "请输入验证码!", 0);
                    return;
                }
                if (TextUtils.isEmpty(setPwdPhone)) {
                    TmtUtils.midToast(this, "请输入您的手机号!", 0);
                    return;
                }*/
                /**
                 * 18.	提现密码修改
                 1)	请求地址：
                 /v1.0/extract/pwd/forget
                 2)	请求方式:
                 post
                 3)	请求参数说明：
                 字段名	类型	长度	备注
                 mobile	string	11	手机号码
                 code	string	6	验证码
                 password	string	至少4位	密码
                 repassword	string	至少4位	确认密码
                 */
                ForgetPasswordElement element = new ForgetPasswordElement();
                element.setPassword(setPwd);
                element.setRepassword(setPwdConfirm);

                SSQSApplication.apiClient(classGuid).forgetPassword(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            TmtUtils.midToast(ForgetBusinessPwdActivity.this, "修改交易密码成功!", 0);
                            finish();
                        } else {
                            TmtUtils.midToast(ForgetBusinessPwdActivity.this, result.getMessage(), 0);
                        }
                    }
                });
                break;

            default:
                break;
        }
    }

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

}
