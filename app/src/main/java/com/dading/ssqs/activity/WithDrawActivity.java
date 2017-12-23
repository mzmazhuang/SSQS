package com.dading.ssqs.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ExtractUploadElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.WithDrawBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/27 17:17
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WithDrawActivity extends BaseActivity {
    private static final String TAG = "WithDrawActivity";
    @Bind(R.id.with_draw_name)
    TextView mWithDrawName;
    @Bind(R.id.with_draw_card_number)
    TextView mWithDrawCardNumber;
    @Bind(R.id.with_draw_now_rmb)
    TextView mWithDrawNowRmb;
    @Bind(R.id.with_draw_now_draw_rmb)
    TextView mWithDrawNowDrawRmb;
    @Bind(R.id.with_draw_not_draw_rmb)
    TextView mWithDrawNotDrawRmb;
    @Bind(R.id.with_draw_draw_rmb)
    EditText mWithDrawDrawRmb;
    @Bind(R.id.with_draw_businsess_pwd)
    EditText mWithDrawBusinsessPwd;
    @Bind(R.id.with_draw_forget_pwd)
    TextView mWithDrawForgetPwd;
    @Bind(R.id.with_draw_confirm)
    Button mWithDrawConfirm;
    /* @Bind(R.id.err_with_draw_again)
     Button         mErrWithDrawAgain;
     @Bind(R.id.err_with_draw)
     RelativeLayout mErrWithDraw;*/

    @Bind(R.id.top_title)
    TextView mTopTitle;

    private String mMoney;
    private AlertDialog mDialog;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_with_draw;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.bank_card_with_draw));
        Intent intent = getIntent();
        String s = intent.getStringExtra(Constent.WITH_DRAW);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        //builder.setView(mPopView);
        builder.setTitle("温馨提醒");
        builder.setMessage("申请提款成功!");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mDialog = builder.create();
        /**
         * 16.	用户提现信息详情
         1)	请求地址：/v1.0/extract/msg/detail
         2)	请求方式:get
         3)	请求参数说明：字段名	    类型	        长度	        备注
         auth_token	varchar		token
         */

        SSQSApplication.apiClient(classGuid).getUserWithdrawalsDetails(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if(result.isOk()){
                    WithDrawBean bean= (WithDrawBean) result.getData();

                    if(bean!=null){
                        processedData(bean);
                    }
                }else{
                    TmtUtils.midToast(WithDrawActivity.this, "拉取提现信息失败请重试!", 0);
                }
            }
        });
    }

    private void processedData(WithDrawBean data) {
        mWithDrawName.setText(data.userName);
        mWithDrawCardNumber.setText(data.bankCard);
        String currentMoney = data.currentMoney + "元";
        mWithDrawNowRmb.setText(currentMoney);//当前金额
        mMoney = data.extractMoney.replaceAll(",", "");
        mWithDrawDrawRmb.setMaxEms(mMoney.length());
        String extraMoney = mMoney + "元";
        mWithDrawNowDrawRmb.setText(extraMoney);//可提现金额
        String doNotUsed = data.dontUsed + "元";
        mWithDrawNotDrawRmb.setText(doNotUsed);//不可提现金额
    }

    @OnClick({R.id.top_back, R.id.with_draw_confirm, R.id.with_draw_forget_pwd
           /* R.id.err_with_draw_again*/})
    public void onClick(View v) {
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.with_draw_forget_pwd:
                Intent intent = new Intent(this, ForgetBusinessPwdActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.with_draw_confirm:
                String drawRmb = mWithDrawDrawRmb.getText().toString();
                String drawPwd = mWithDrawBusinsessPwd.getText().toString();
                if (TextUtils.isEmpty(drawRmb)) {
                    TmtUtils.midToast(this, "请输入提现金额!", 0);
                    return;
                } else if (Double.valueOf(drawRmb) < 10) {
                    TmtUtils.midToast(this, "提现金额最低不得少于10元!", 0);
                    return;
                } else if (Double.valueOf(drawRmb) > Double.valueOf(mMoney)) {
                    TmtUtils.midToast(this, "提现金额不得大于可提现金额!", 0);
                    return;
                }
                if (TextUtils.isEmpty(drawPwd) || drawPwd.length() < 6) {
                    TmtUtils.midToast(this, "请输入6位交易密码!", 0);
                    return;
                }
                mWithDrawConfirm.setClickable(false);

                ExtractUploadElement element = new ExtractUploadElement();
                element.setMoney(drawRmb);
                element.setPassword(drawPwd);

                SSQSApplication.apiClient(classGuid).extractUpload(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mWithDrawConfirm.setClickable(true);

                        if (result.isOk()) {
                            if (mDialog != null)
                                mDialog.show();
                            mWithDrawDrawRmb.setText("");
                            mWithDrawBusinsessPwd.setText("");
                        } else {
                            TmtUtils.midToast(UIUtils.getContext(), "提现上传失败." + result.getMessage(), 0);
                            LogUtil.util(TAG, result.getMessage() + "失败信息");
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}
