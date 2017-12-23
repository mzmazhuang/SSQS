package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MyNoReadBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 17:07
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyMessageActivity extends BaseActivity {
    private static final String TAG = "MyMessageActivity";
    @Bind(R.id.my_message_topic_introduce)
    TextView mMyMessageTopicIntroduce;
    @Bind(R.id.my_message_topic_num)
    TextView mMyMessageTopicNum;
    @Bind(R.id.my_message_topic_title)
    TextView mMyMessageTopicTitle;

    @Bind(R.id.top_title)
    TextView mTopTitle;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.my_mess));
        /**
         * 2.	获取我的信息类型列表
         a)	请求地址：
         /v1.0/msg/type
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         d)	返回格式
         */

        SSQSApplication.apiClient(classGuid).getMyInfoTypeList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    MyNoReadBean bean = (MyNoReadBean) result.getData();

                    if (bean != null) {
                        mMyMessageTopicTitle.setText(bean.groupName);

                        int msgNum = bean.msgNum;
                        mMyMessageTopicNum.setText(String.valueOf(msgNum));

                        if (msgNum != 0) {
                            mMyMessageTopicIntroduce.setText(bean.content);
                        } else {
                            mMyMessageTopicIntroduce.setText("暂时没有新的信息!");
                        }
                    }

                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(MyMessageActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    /**
     * @param v
     */
    @OnClick({R.id.top_back, R.id.my_message_topic})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.my_message_topic:
                Intent intent = new Intent(this, ToadyTopicActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
