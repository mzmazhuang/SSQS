package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyAllCircleAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.AllCircleLBean;
import com.dading.ssqs.bean.AllCircleRBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/18 11:04
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AllCircleActivity extends BaseActivity {
    private static final String TAG = "AllCircleActivity";
    @Bind(R.id.all_circle_rg)
    RadioGroup mAllCircleRg;
    @Bind(R.id.all_circle_listview)
    ListView mAllCircleListview;

    @Bind(R.id.top_title)
    TextView mTopTitle;

    private ArrayList<Integer> mMList;
    private List<AllCircleLBean> mData;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        if (mData != null)
            processData(mData);
    }

    @Override
    public void initData() {
        mTopTitle.setText(getString(R.string.all_circle));
        /**
         * a)	请求地址：
         /v1.0/aCategoryType/list
         b)	请求方式:
         get
         */
        SSQSApplication.apiClient(classGuid).getCategoryTypeList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mData = (List<AllCircleLBean>) result.getData();

                    if (mData != null) {
                        processData(mData);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(AllCircleActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(AllCircleActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_all_circle_activity;
    }

    private void processData(List<AllCircleLBean> lBean) {
        mMList = new ArrayList<>();
        int h = DensityUtil.dip2px(this, 50);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, h);
        if (lBean != null) {
            for (int i = 0; i < lBean.size(); i++) {
                RadioButton rb = new RadioButton(this);
                rb.setLayoutParams(params);
                rb.setText(lBean.get(i).name);
                rb.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
                rb.setBackground(this.getResources().getDrawable(R.drawable.selecotr_collect_circle_rg));
                rb.setGravity(Gravity.CENTER);
                rb.setTextSize(14);
                rb.setTextColor(this.getResources().getColor(R.color.gray2));
                int id = Constent.RG_ID + i;
                mMList.add(id);
                rb.setId(id);
                mAllCircleRg.addView(rb);
            }
            int id = Constent.RG_ID;
            mAllCircleRg.check(id);

            /**
             * a)请求地址：/v1.0/articleCategory/typeID/{type}
             b)	请求方式:get
             c)	请求参数说明：type：文章类别ID  auth_token：登陆后加入请求头
             */
            int matchId = mData.get(0).id;
            setGetRight(matchId);
        }
    }

    private void setGetRight(int matchId) {
        SSQSApplication.apiClient(classGuid).getAllArticelData(matchId + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<AllCircleRBean> items = (List<AllCircleRBean>) result.getData();

                    if (items != null) {
                        processDataR(items);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(AllCircleActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(AllCircleActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processDataR(List<AllCircleRBean> rBean) {
        mAllCircleListview.setAdapter(new MyAllCircleAdapter(this, rBean));
    }

    @Override
    public void initListener() {
        mAllCircleRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mMList.size(); i++) {
                    if (mMList.get(i).equals(checkedId)) {
                        int id = mData.get(i).id;
                        changeRightData(id);
                    }
                }
            }
        });
    }

    private void changeRightData(int id) {
        setGetRight(id);
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }

}
