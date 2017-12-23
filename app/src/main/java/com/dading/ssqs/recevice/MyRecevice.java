package com.dading.ssqs.recevice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.dading.ssqs.activity.HomeViewPagerActivity;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JPUSHBean;
import com.dading.ssqs.utils.LogUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/22 11:11
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyRecevice extends BroadcastReceiver {
    private static final String TAG = "MyRecevice";
    private String    mTitle;
    private String    mMsg;
    private String    mExEX;
    private String    mNoticTtle;
    private String    mNoticTtle1;
    private String    mNoticTtle2;
    private JPUSHBean mBean;

    @Override
    public void onReceive (Context context, Intent intent) {
        //注意是消息的推送广播不是同志notice
        Intent intentInfo = null;
        if (intent.getAction( ).equals(JPushInterface.ACTION_MESSAGE_RECEIVED)) {
            LogUtil.util(TAG, "受到自定义消息------------------------------:");
            //自定义的推送
            Bundle bundle = intent.getExtras( );
            String title = bundle.getString(JPushInterface.EXTRA_TITLE);
            String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            LogUtil.util(TAG, "返回数据是------------------------------:" + "消息的标题:" + title + " 内容:" + msg);
        } else if (intent.getAction( ).equals(JPushInterface.ACTION_NOTIFICATION_RECEIVED)) {
            Bundle extras = intent.getExtras( );
            if (extras != null) {
                mMsg = extras.getString(JPushInterface.EXTRA_MESSAGE);
                mTitle = extras.getString(JPushInterface.EXTRA_TITLE);
                mNoticTtle = extras.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                mNoticTtle1 = extras.getString(JPushInterface.EXTRA_NOTIFICATION_ID);
                mNoticTtle2 = extras.getString(JPushInterface.EXTRA_NOTIFICATION_DEVELOPER_ARG0);
                LogUtil.util(TAG, "返回数据是------------------------------:" + "通知的标题:" + mTitle + " 内容:" + mMsg + "附加" + mExEX + "通知标题" + mNoticTtle +
                        "---" + mNoticTtle1 + "---" + mNoticTtle2);
            }
        } else if (intent.getAction( ).equals(JPushInterface.ACTION_NOTIFICATION_OPENED)) {
            Bundle extras = intent.getExtras( );
            if (extras != null) {
                mNoticTtle = extras.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                mNoticTtle1 = extras.getString(JPushInterface.EXTRA_NOTIFICATION_ID);
                mExEX = extras.getString(JPushInterface.EXTRA_EXTRA);
                try {
                    mBean = JSON.parseObject(mExEX, JPUSHBean.class);
                    if (mBean != null && mBean.android != null && mBean.android.extras != null) {
                        if (mBean.android.extras.forwardType == 1) {
                            intentInfo = new Intent(context, MatchInfoActivity.class);
                            intentInfo.putExtra(Constent.MATCH_ID, mBean.android.extras.forwardID);
                            intentInfo.putExtra(Constent.INTENT_FROM, "0");
                        } else {
                            intentInfo = new Intent(context, HomeViewPagerActivity.class);
                            intentInfo.putExtra(Constent.INFO_ID, mBean.android.extras.forwardID);
                        }
                        intentInfo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace( );
                }
            }
            boolean b = context instanceof Activity;
            LogUtil.util(TAG, "返回数据是------------------------------:" + "跳转的标题:" + mTitle + " 内容:" + mMsg + "附加" + mExEX + "通知标题" + mNoticTtle +
                    "---" + mNoticTtle1 + "---" + mNoticTtle2 + "intentInfo instance of Activity----" + b);
        }
    }
}
