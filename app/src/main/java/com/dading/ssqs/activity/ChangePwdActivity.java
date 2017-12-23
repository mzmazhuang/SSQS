package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.UpdatePassWordElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/20 15:58
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ChangePwdActivity extends BaseActivity implements View.OnFocusChangeListener {
    private static final String TAG = "ChangePwdActivity";
    @Bind(R.id.et_change_pwd_old_icon)
    ImageView mEtChangePwdOldIcon;
    @Bind(R.id.et_change_pwd_old)
    EditText mEtChangePwdOld;
    @Bind(R.id.et_change_pwd_old_look)
    CheckBox mEtChangePwdOldLook;
    @Bind(R.id.et_change_pwd_old_close)
    ImageButton mEtChangePwdOldClose;
    @Bind(R.id.et_change_pwd_new_icon)
    ImageView mEtChangePwdNewIcon;
    @Bind(R.id.et_change_pwd_new)
    EditText mEtChangePwdNew;
    @Bind(R.id.et_change_pwd_new_look)
    CheckBox mEtChangePwdNewLook;
    @Bind(R.id.et_change_pwd_new_close)
    ImageButton mEtChangePwdNewClose;
    @Bind(R.id.change_pwd_button)
    Button mChangePwdButton;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private String mOldPwd;
    private String mNewPwd;
    private String mToken;
    private Runnable mRunnable;

    @Override
    protected void initData() {
        super.initData();
        mTopTitle.setText(getString(R.string.change_pwd));
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initListener() {
        mEtChangePwdOld.setOnFocusChangeListener(this);
        mEtChangePwdNew.setOnFocusChangeListener(this);
        /**
         * text监听
         */
        mEtChangePwdOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mEtChangePwdOldClose.setVisibility(View.VISIBLE);
                } else {
                    mEtChangePwdOldClose.setVisibility(View.GONE);
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                }
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(mEtChangePwdNew.getText().toString())) {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register_sel);
                } else {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                }
            }

        });


        mEtChangePwdNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mEtChangePwdNewClose.setVisibility(View.VISIBLE);
                } else {
                    mEtChangePwdNewClose.setVisibility(View.GONE);
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                }
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(mEtChangePwdOld.getText().toString())) {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register_sel);
                } else {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                }
            }
        });


    }

    @OnClick({R.id.top_back, R.id.change_pwd_button, R.id.et_change_pwd_new_look,
            R.id.et_change_pwd_old_close, R.id.et_change_pwd_new_close, R.id.et_change_pwd_old_look})
    public void OnClik(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.et_change_pwd_old_close:
                mEtChangePwdOld.setText(null);
                mChangePwdButton.setBackgroundResource(R.mipmap.register);
                break;

            case R.id.et_change_pwd_old_look:
                if (mEtChangePwdOldLook.isChecked()) {//显示密码
                    mEtChangePwdOld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtChangePwdOld.setSelection(mEtChangePwdOld.getText().toString().trim().length());
                } else {//隐藏密码
                    mEtChangePwdOld.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEtChangePwdOld.setSelection(mEtChangePwdOld.getText().toString().trim().length());
                }

                int numLength = mEtChangePwdOld.getText().length();
                int pwdLength = mEtChangePwdNew.getText().length();
                if (numLength > 0 && pwdLength > 0) {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register_sel);
                } else {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                }
                break;

            case R.id.et_change_pwd_new_close:
                mEtChangePwdNew.setText(null);
                mChangePwdButton.setBackgroundResource(R.mipmap.register);
                break;
            case R.id.et_change_pwd_new_look:
                if (mEtChangePwdNewLook.isChecked()) {//显示密码
                    mEtChangePwdNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtChangePwdNew.setSelection(mEtChangePwdNew.getText().toString().trim().length());
                } else {//隐藏密码
                    mEtChangePwdNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEtChangePwdNew.setSelection(mEtChangePwdNew.getText().toString().trim().length());
                }

                int numLengthN = mEtChangePwdOld.getText().length();
                int pwdLengthN = mEtChangePwdNew.getText().length();
                if (numLengthN > 0 && pwdLengthN > 0) {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register_sel);
                } else {
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                }
                break;
            case R.id.change_pwd_button:
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
                boolean b = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (b) {
                    mToken = UIUtils.getSputils().getString(Constent.TOKEN, null);
                    mOldPwd = mEtChangePwdOld.getText().toString();
                    mNewPwd = mEtChangePwdNew.getText().toString();
                    if (TextUtils.isEmpty(mOldPwd)) {
                        TmtUtils.midToast(ChangePwdActivity.this, "请输入原密码!", 0);
                        return;
                    }
                    if (TextUtils.isEmpty(mNewPwd)) {
                        TmtUtils.midToast(ChangePwdActivity.this, "请输入新密码!", 0);
                        return;
                    }
                    if (mOldPwd.length() < 6 || mOldPwd.length() > 32) {
                        TmtUtils.midToast(ChangePwdActivity.this, "请输入正确的原密码!", 0);
                        return;
                    }
                    if (mOldPwd.length() < 6) {
                        TmtUtils.midToast(ChangePwdActivity.this, "密码长度不得少于六位", 0);
                        return;
                    }
                    if (mOldPwd.length() > 32) {
                        TmtUtils.midToast(ChangePwdActivity.this, "密码长度不的大于三十二位", 0);
                        return;
                    }
                    UpdatePassWordElement element = new UpdatePassWordElement();
                    element.setPassword(mOldPwd);
                    element.setNewPassword(mNewPwd);

                    SSQSApplication.apiClient(classGuid).updatePassWord(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    //发送广播
                                    Intent intentOut = new Intent();
                                    intentOut.putExtra(Constent.LOADING_BROCAST_TAG, false);
                                    intentOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intentOut.setAction(Constent.LOADING_ACTION);
                                    ChangePwdActivity.this.sendBroadcast(intentOut);
                                    TmtUtils.midToast(ChangePwdActivity.this, "修改密码成功,2s后将跳转到登陆界面", 0);
                                    mRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                            //mHandler.removeCallbacks(mRunnable);
                                            UIUtils.removeTask(mRunnable);
                                        }
                                    };
                                    //mHandler.postDelayed(mRunnable, 2000);
                                    UIUtils.postTaskDelay(mRunnable, 2000);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    Intent intent = new Intent(ChangePwdActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(UIUtils.getContext(), "密码修改失败..." + result.getMessage(), 0);
                                }
                            }
                        }
                    });
                    mEtChangePwdOld.setText(null);
                    mEtChangePwdNew.setText(null);
                    mChangePwdButton.setBackgroundResource(R.mipmap.register);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_loading_number:
                if (hasFocus) {
                    mEtChangePwdOldIcon.setImageResource(R.mipmap.reg_phone_select);
                } else {
                    mEtChangePwdOldIcon.setImageResource(R.mipmap.reg_phone);
                }
                break;
            case R.id.et_loading_pwd:
                if (hasFocus) {
                    mEtChangePwdNewIcon.setImageResource(R.mipmap.r_reg_lock_select);
                } else {
                    mEtChangePwdNewIcon.setImageResource(R.mipmap.r_reg_lock);
                }
                break;

            default:
                break;
        }
    }
}
