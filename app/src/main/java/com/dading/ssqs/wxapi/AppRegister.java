package com.dading.ssqs.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dading.ssqs.utils.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//IWXAPI 是第三方app与微信通信的openapi接口
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		//将应用的appID注册到微信
		api.registerApp(Constants.APP_ID);
	}
}
