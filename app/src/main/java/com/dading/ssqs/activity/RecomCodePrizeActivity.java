package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/21 10:09
 * 描述	      ${邀请奖励}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RecomCodePrizeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RecomCodePrizeActivity";

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.input_invited_code)
    EditText inputinvitedcode;
    @Bind(R.id.invited_code_ly)
    LinearLayout inputinviteLy;

    @OnClick({R.id.top_back, R.id.invited_code_checked})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                UIUtils.hideKeyBord(this);
                finish();
                break;
            case R.id.invited_code_checked:
                /**
                 * 4.	添加邀请码接口
                 5)	请求地址：
                 /v1.0/user/invite/{inviteCode}
                 */

                String inviteCode = inputinvitedcode.getText().toString();
                if (inviteCode.isEmpty()) {
                    ToastUtils.midToast(RecomCodePrizeActivity.this, "请输入邀请码!", 0);
                    return;
                }
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

                SSQSApplication.apiClient(classGuid).createInvite(inviteCode, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            ToastUtils.midToast(RecomCodePrizeActivity.this, "恭喜您获得1000金币!", 0);
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(RecomCodePrizeActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.midToast(RecomCodePrizeActivity.this, result.getMessage(), 0);
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
    protected void initData() {
        super.initData();
        mTopTitle.setText(getString(R.string.recom_code_prize));
        inputinviteLy.setBackgroundResource(R.mipmap.invitation_code);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recom_code;
    }
}
