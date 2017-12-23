package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.RegTrialElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.TryPlayBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class UserTrialGameActivity extends BaseActivity {
    private static final String TAG = "UserTrialGameActivity";
    @Bind(R.id.username_register_et)
    TextView mUsernameRegisterEt;
    @Bind(R.id.userpassword_register_et)
    EditText mUserpasswordRegisterEt;
    @Bind(R.id.confirm_pwd_register_et)
    EditText mConfirmPwdRegisterEt;

    @Bind(R.id.reminder_tv)
    TextView mReminderTv;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_user_trial_game;
    }

    @Override
    protected void initData() {
        /**
         * 14.	获取试玩用户账号
         a)	请求地址：/v1.0/user/reg/trial/msg
         b)	请求方式:get
         */

        SSQSApplication.apiClient(classGuid).getDemoUser(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    TryPlayBean bean = (TryPlayBean) result.getData();

                    if (bean != null) {
                        processedData(bean);
                    }
                } else {
                    ToastUtils.midToast(UserTrialGameActivity.this, result.getMessage(), 0);
                }
            }
        });
    }

    private void processedData(TryPlayBean bean) {
        mUsernameRegisterEt.setText(bean.getAccount());
        mReminderTv.setText(bean.getRules());
    }

    public void clearPWD() {
        mUserpasswordRegisterEt.setText("");
        mConfirmPwdRegisterEt.setText("");
    }

    public String getString(TextView tv) {
        return tv.getText().toString().trim();
    }

    @OnClick({R.id.register_btn, R.id.hava_id_go2login, R.id.user_trial_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_trial_back:
                UIUtils.hideKeyBord(this);
                finish();
                break;
            case R.id.register_btn:
                String userName = getString(mUsernameRegisterEt);
                String pwd = getString(mUserpasswordRegisterEt);
                String configPwd = getString(mConfirmPwdRegisterEt);

                if (pwd.isEmpty()) {
                    errPwd("密码不能为空请重新输入!");
                } else if (configPwd.isEmpty()) {
                    errPwd("确认密码不能为空请重新输入!");
                } else if (pwd.length() < 6 || pwd.length() > 20) {
                    errPwd("密码长度不能小于6或大于20个字符!");
                } else if (configPwd.length() < 6 || configPwd.length() > 20) {
                    errPwd("确认密码长度不能小于6或大于20个字符!");
                } else if (!pwd.equals(configPwd)) {
                    errPwd("两次输入的密码不一致请重新输入!");
                } else {
                    UIUtils.hideKeyBord(this);

                    /**
                     * 15.	注册试用账号
                     a)	请求地址：/v1.0/user/reg/trial
                     b)	请求方式:post
                     c)	请求参数说明
                     字段名	类型	长度	是否必填	备注
                     account	string		是	试玩账号
                     password	string	6-20	是	密码
                     repassword	string	6-20	是	确认密码
                     */
                    RegTrialElement element = new RegTrialElement();
                    element.setAccount(userName);
                    element.setPassword(pwd);
                    element.setRepassword(configPwd);

                    SSQSApplication.apiClient(classGuid).regTrial(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
                                    UIUtils.SendReRecevice(Constent.GQ_RECEVICE);

                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);
                                    UIUtils.getSputils().putString(Constent.GLODS, bean.banlance + "");
                                    UIUtils.getSputils().putString(Constent.DIAMONDS, bean.diamond + "");
                                    UIUtils.getSputils().putString(Constent.LOADING_STATE_SP, bean.toString());
                                    UIUtils.getSputils().putString(Constent.TOKEN, bean.authToken);
                                    finish();
                                }
                            } else {
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                ToastUtils.midToast(UserTrialGameActivity.this, result.getMessage(), 0);
                            }
                        }
                    });
                }
                break;
            case R.id.hava_id_go2login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                UIUtils.hideKeyBord(this);
                finish();
                break;
        }
    }

    private void errPwd(String str) {
        ToastUtils.midToast(UserTrialGameActivity.this, str, 0);
        clearPWD();
    }

}
