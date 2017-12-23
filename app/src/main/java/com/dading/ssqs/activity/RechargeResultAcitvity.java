package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;

import butterknife.Bind;
import butterknife.OnClick;

public class RechargeResultAcitvity extends BaseActivity {
    @Bind(R.id.recharge_result_upload_notice)
    TextView mRechargeResultUploadNotice;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_recharge_result;
    }

    @Override
    protected void initData ( ) {
        mTopTitle.setText(getString(R.string.recharge_result));
        Intent intent = getIntent( );
        String account = intent.getStringExtra(Constent.ACCOUNT);
        String notice = "提交成功,您的充值申请已提交,充值金额" +
                (TextUtils.isEmpty(account) ? "0" :account) + "元";
        mRechargeResultUploadNotice.setText(notice);
    }

    @OnClick({R.id.top_back, R.id.recharge_suc_finish})
    public void OnClik (View v) {
        switch (v.getId( )) {
            case R.id.top_back:
            case R.id.recharge_suc_finish:
                finish( );
                break;
        }
    }

}
