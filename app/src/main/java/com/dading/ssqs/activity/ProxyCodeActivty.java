package com.dading.ssqs.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.ProxyCodeAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.ProxyCodeBean;
import com.dading.ssqs.bean.ProxyIntroLookBean;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/8/8.
 */
public class ProxyCodeActivty extends BaseActivity {

    private static final String TAG = "ProxyCodeActivty";
    @Bind(R.id.proxy_code_rb_l)
    RadioButton mProxyCodeRbL;
    @Bind(R.id.proxy_code_rb_r)
    RadioButton mProxyCodeRbR;
    @Bind(R.id.proxy_code_rg)
    RadioGroup mProxyCodeRg;
    @Bind(R.id.proxy_code_lv)
    ListView mProxyCodeLv;
    @Bind(R.id.proxy_make_code_ly)
    LinearLayout mProxyCodeLy;
    @Bind(R.id.proxy_up_code)
    EditText mProxyUpCode;
    @Bind(R.id.proxy_random_code)
    TextView mProxyRandomCode;
    @Bind(R.id.data_empty)
    RelativeLayout mDataEmpty;
    private int RANDOM = 1;
    private int UPLOAD = 2;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_proxy_code;
    }

    @Override
    protected void initData() {
        super.initData();
        mProxyCodeLv.setEmptyView(mDataEmpty);
    }

    @Override
    protected void initListener() {
        mProxyCodeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.proxy_code_rb_l:
                        mProxyCodeLy.setVisibility(View.VISIBLE);
                        break;
                    case R.id.proxy_code_rb_r:
                        mProxyCodeLy.setVisibility(View.GONE);
                        volleyGetList();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.proxy_code_return, R.id.proxy_upload_code, R.id.proxy_random_code})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.proxy_code_return:
                finish();
                break;
            case R.id.proxy_random_code:
                volleyGet("", RANDOM);
                break;
            case R.id.proxy_upload_code:
                if (TextUtils.isEmpty(mProxyUpCode.getText())) {
                    TmtUtils.midToast(UIUtils.getContext(), "请输入您要生成的邀请码!", 0);
                    return;
                }
                /**
                 * /v1.0/agent/code/create/{code}
                 b)	请求方式:
                 get
                 c)	请求参数说明
                 字段名	类型	长度	是否必填	备注
                 auth_token	string		是	token
                 code	string		是	邀请码
                 */
                volleyGet(mProxyUpCode.getText().toString(), UPLOAD);
                break;
        }
    }

    private void volleyGetList() {
        SSQSApplication.apiClient(classGuid).agentCodeList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<ProxyIntroLookBean> beanLook = (List<ProxyIntroLookBean>) result.getData();

                    if (beanLook != null) {
                        processedDataLook(beanLook);
                    }
                } else {
                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                }
            }
        });
    }

    private void volleyGet(String cCode, final int code) {
        SSQSApplication.apiClient(classGuid).agentCodeOperation(cCode, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    ProxyCodeBean bean = (ProxyCodeBean) result.getData();

                    if (bean != null) {
                        switch (code) {
                            case 1:
                                processedData(bean);
                            case 2:
                                processedDataUp();
                                break;
                        }
                    }
                } else {
                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                }
            }
        });
    }

    private void processedDataLook(List<ProxyIntroLookBean> beanLook) {
        //mProxyCodeLv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        //mProxyCodeLv.setAdapter(new ProxyLookCodeAdapter(R.layout.item_proxy_code, data,this));
        mProxyCodeLv.setAdapter(new ProxyCodeAdapter(this, beanLook));
    }


    private void processedDataUp() {
        TmtUtils.midToast(UIUtils.getContext(), "邀请码已生成!", 0);
        mProxyUpCode.setText("");
    }

    private void processedData(ProxyCodeBean bean) {
        mProxyUpCode.setText(bean.getData());
    }
}
