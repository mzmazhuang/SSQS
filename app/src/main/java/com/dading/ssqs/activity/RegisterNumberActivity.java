package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.RegAccountElement;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/23 11:26
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RegisterNumberActivity extends BaseActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "RegisterNumberActivity";
    @Bind(R.id.regist_number_username)
    EditText mRegistNumberUsername;
    @Bind(R.id.regist_number_pw)
    EditText mRegistNumberPw;
    @Bind(R.id.regist_number_pw_confirm)
    EditText mRegistNumberPwConfirm;
    @Bind(R.id.regist_number_button)
    Button mRegistNumberButton;
    @Bind(R.id.regiset_loading)
    TextView mRegisetLoading;
    @Bind(R.id.regiset_second_allow_icon)
    CheckBox mRegisetSecondAllowIcon;
    @Bind(R.id.regist_invite_code)
    EditText mRegistInviteCode;
    @Bind(R.id.register_container)
    RelativeLayout container;
    @Bind(R.id.register_scrollview)
    ScrollView scrollView;

    private boolean mIsAgree = true;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_regist_number;
    }

    @Override
    protected void initData() {
        mRegisetSecondAllowIcon.setChecked(mIsAgree);
    }

    @Override
    protected void initListener() {
        mRegistNumberUsername.addTextChangedListener(this);
        mRegistNumberPw.addTextChangedListener(this);
        mRegistNumberPwConfirm.addTextChangedListener(this);
        mRegisetSecondAllowIcon.setOnCheckedChangeListener(this);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBroad();
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyBroad();
                return false;
            }
        });
    }

    private void hideKeyBroad() {
        AndroidUtilities.hideKeyboard(mRegistNumberUsername);
        AndroidUtilities.hideKeyboard(mRegistNumberPw);
        AndroidUtilities.hideKeyboard(mRegistNumberPwConfirm);
        AndroidUtilities.hideKeyboard(mRegistInviteCode);
    }

    @OnClick({R.id.regist_number_return, R.id.regist_number_button, R.id.regiset_loading, R.id.regiset_second_protocol})
    public void OnClik(View v) {
        Intent intent;
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.regist_number_return:
                finish();
                break;
            case R.id.regiset_second_protocol:
                //跳转金百盈足球协议
                intent = new Intent(this, SSQSRegisterProtocolActivity.class);
                startActivity(intent);
                break;
            case R.id.regiset_loading:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.regist_number_button:
                //提交注册

                String pwUsername = mRegistNumberUsername.getText().toString();
                String pw = mRegistNumberPw.getText().toString();
                String pwConfirm = mRegistNumberPwConfirm.getText().toString();

                if (!mIsAgree) {
                    ToastUtils.midToast(RegisterNumberActivity.this, "请选择是否同意注册协议...", 0);
                } else if (pwUsername.length() < 6 || pw.length() < 6 && pwConfirm.length() < 6) {
                    ToastUtils.midToast(RegisterNumberActivity.this, "账号或密码长度不得少于6位!", 0);
                } else if (!pw.equals(pwConfirm)) {
                    ToastUtils.midToast(RegisterNumberActivity.this, "两次密码输入不一致,请检查设置的密码!", 0);
                } else {
                    RegAccountElement element = new RegAccountElement();
                    element.setAccount(pwUsername);
                    element.setPassword(pw);
                    element.setRepassword(pwConfirm);
                    element.setAgentCode(mRegistInviteCode.getText().toString());

                    SSQSApplication.apiClient(classGuid).regAccount(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                ToastUtils.midToast(RegisterNumberActivity.this, "注册成功!", 0);
                                finish();
                            } else {
                                Logger.d(TAG, result.getMessage());
                                ToastUtils.midToast(RegisterNumberActivity.this, result.getMessage(), 0);
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void afterTextChanged(Editable s) {
        String pwUsername = mRegistNumberUsername.getText().toString();
        String pw = mRegistNumberPw.getText().toString();
        String pwConfirm = mRegistNumberPwConfirm.getText().toString();

        if (!"".equals(pwUsername) && !"".equals(pw) && !"".equals(pwConfirm)) {
            mRegistNumberButton.setBackgroundResource(R.drawable.rect_blue_light2);
            mRegistNumberButton.setClickable(true);
        } else {
            mRegistNumberButton.setBackgroundResource(R.drawable.rect_gray);
            mRegistNumberButton.setClickable(false);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //是否同意
        mIsAgree = isChecked;
    }

}
