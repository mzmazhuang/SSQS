package com.dading.ssqs.wxapi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.OrderStatusElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, Constent.APP_ID);
        api.handleIntent(getIntent(), this);
        TextView ly = (TextView) findViewById(R.id.pay_ly);
        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(final BaseResp resp) {
        String order = UIUtils.getSputils().getString(Constent.WX, "");
        Logger.d(TAG, "支付结果返回订单数据是------------------------------:" + order);
        int code = 0;
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            UIUtils.SendReRecevice(Constent.SERIES);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);
            if (resp.errCode == 0) {
                code = 9000;
                TmtUtils.midToast(WXPayEntryActivity.this, "支付成功!", 0);
                finish();
                builder.setMessage(getString(R.string.pay_suc));
            } else if (resp.errCode == -2) {
                code = resp.errCode;
                TmtUtils.midToast(WXPayEntryActivity.this, "支付取消!", 0);
                finish();
                builder.setMessage(getString(R.string.pay_fail));
            } else if (resp.errCode == -1) {
                builder.setMessage(getString(R.string.pay_err));
                TmtUtils.midToast(WXPayEntryActivity.this, "支付错误:-1!", 0);
                finish();
                code = resp.errCode;
            }
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            /**
             10.	支付状态提交
             a)	请求地址：
             /v1.0/awardExchange/order
             b)	请求方式:
             post
             c)	请求参数说明：
             auth_token：登陆后加入请求头
             orderID:订单号
             status:支付状态，支付宝返回码是多少就是多少
             */
            OrderStatusElement element = new OrderStatusElement();
            element.setOrderID(order);
            element.setStatus(String.valueOf(code));

            SSQSApplication.apiClient(0).orderStausUpload(element, new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {
                        Logger.d(TAG, "微信支付结果提交返回数据是-----:" + result.getMessage());
                    } else {
                        Logger.d(TAG, result.getMessage() + "失败信息");
                    }
                }
            });
            // builder.show();
        }
    }
}