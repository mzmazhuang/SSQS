package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.ReferIncomeBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/21 10:20
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ReferIncomeActivity extends BaseActivity {
    private static final String TAG = "ReferIncomeActivity";

    @Bind(R.id.recom_income_back)
    ImageView recomincomeback;
    @Bind(R.id.refer_income_num)
    TextView referincomenum;
    @Bind(R.id.refer_income_match_refer)
    TextView referincomematchrefer;
    @Bind(R.id.refer_income_is_savant)
    TextView referincomeissavant;
    @Bind(R.id.refer_income_is_savant_des)
    TextView referincomeissavantDes;
    @Bind(R.id.refer_income_go_or_tobe)
    LinearLayout referincomegoortobe;
    private LoadingBean mBean;
    private String mSavant;


    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected void initData() {
        SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mBean = (LoadingBean) result.getData();

                    if (mBean != null) {
                        if (mBean.userType == 2) {
                            switch (mBean.eLevel) {
                                case 1:
                                    mSavant = "初级";
                                    break;
                                case 2:
                                    mSavant = "中级";
                                    break;
                                case 3:
                                    mSavant = "高级";
                                    break;
                                case 4:
                                    mSavant = "资深";
                                    break;

                                default:
                                    break;
                            }
                            String s2 = "您已经是" + mSavant + "专家";
                            referincomeissavant.setText(s2);
                            referincomeissavantDes.setText(getString(R.string.write_refer));
                        } else {
                            referincomeissavant.setText(getString(R.string.is_not_savant));
                            referincomeissavantDes.setText(getString(R.string.how_tobe_savant));
                        }

                        UIUtils.getSputils().putString(Constent.TOKEN, mBean.authToken);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);

                        UIUtils.getSputils().putString(Constent.GLODS, mBean.banlance + "");
                        UIUtils.getSputils().putString(Constent.DIAMONDS, mBean.diamond + "");
                        Logger.d(TAG, "我的金币:" + mBean.banlance + ",我的钻石:" + mBean.diamond);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        //发送广播
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        ToastUtils.midToast(ReferIncomeActivity.this, "您的账号已在其他手机登录!如不是您本人,请修改密码..", 0);
                    }
                }
            }
        });
        /**
         * 6.	推荐收入接口
         1)	请求地址：
         /v1.0/expert/money
         */

        SSQSApplication.apiClient(classGuid).expertMoney(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    ReferIncomeBean bean = (ReferIncomeBean) result.getData();

                    if (bean != null) {
                        referincomenum.setText(String.valueOf(bean.getData()));
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                       /* Intent intent = new Intent(ReferIncomeActivity.this, LoadingActivity.class);
                        startActivity(intent);*/
                        finish();
                    } else {
                        ToastUtils.midToast(ReferIncomeActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_refer_income;
    }


    @OnClick({R.id.recom_income_back, R.id.refer_income_match_refer, R.id.refer_income_go_or_tobe})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recom_income_back:
                finish();
                break;
            case R.id.refer_income_match_refer:
                //发送广播
                Logger.d(TAG, "推荐------------------------------:广播");
                UIUtils.SendReRecevice(Constent.LOADING_HOME_SAVANT);
                finish();
                break;
            case R.id.refer_income_go_or_tobe:
                if (mBean.userType == 2) {
                    Intent intent = new Intent(this, WriteReferActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (mBean.isQualify != 1) {
                        ToastUtils.midToast(this, "对不起,您暂时还没有申请资格..", 0);
                        return;
                    }
                    if (mBean.isApply == 1) {
                        ToastUtils.midToast(this, "您已经申请专家认证,请勿重复申请,请等待审核!", 0);
                    } else {
                        Intent intent = new Intent(this, SavantAuthenticationActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            default:
                break;
        }
    }
}
