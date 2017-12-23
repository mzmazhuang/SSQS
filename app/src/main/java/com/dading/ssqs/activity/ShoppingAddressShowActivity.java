package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.ShoppingAddBean;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/11/25 10:57
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ShoppingAddressShowActivity extends BaseActivity {


    private static final String TAG = "ShoppingAddressShowActivity";

    @Bind(R.id.shopping_addr_people_name_show)
    TextView mShoppingAddrPeopleName;
    @Bind(R.id.shopping_addr_people_phone_show)
    TextView mShoppingAddrPeoplePhone;
    @Bind(R.id.show_address_shengshixian_show)
    TextView mShowAddressShengshixian;
    @Bind(R.id.show_address_change_show)
    Button mShowAddressChange;
    @Bind(R.id.top_title)
    TextView mTopTitle;


    private String mAddMsg;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shopping_address_shwow;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.shopping_addr));
        /**
         * 13.	获取用户收货地址
         a)	请求地址：
         /v1.0/user/get/address
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         */

        SSQSApplication.apiClient(classGuid).getUserAddress(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {

                if (result.isOk()) {
                    ShoppingAddBean bean = (ShoppingAddBean) result.getData();

                    if (bean != null) {
                        Gson gson = new Gson();
                        mAddMsg = gson.toJson(bean, LoadingBean.class);

                        mShoppingAddrPeopleName.setText(bean.receiver);
                        mShoppingAddrPeoplePhone.setText(bean.mobile);
                        String address = bean.province + bean.city + bean.area + bean.address;
                        mShowAddressShengshixian.setText(address);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(ShoppingAddressShowActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @OnClick({R.id.top_back, R.id.show_address_change_show})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.show_address_change_show:
                Intent intentAdd = new Intent(ShoppingAddressShowActivity.this, ShoppingAddressActivity.class);
                intentAdd.putExtra(Constent.ADD_MSG, mAddMsg);
                ShoppingAddressShowActivity.this.startActivity(intentAdd);
                finish();
                break;

            default:
                break;
        }
    }

}
