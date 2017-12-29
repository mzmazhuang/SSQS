package com.dading.ssqs.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyStoreAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.OrderStatusElement;
import com.dading.ssqs.bean.AlipaySucBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.controllar.store.StoreDiamondsControllar;
import com.dading.ssqs.controllar.store.StorePrizeControllar;
import com.dading.ssqs.controllar.store.StorePropControllar;
import com.dading.ssqs.controllar.store.StoreTurnTableControllar;
import com.dading.ssqs.controllar.store.StoreVipControllar;
import com.dading.ssqs.bean.PayResult;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.GlideCircleTransform;
import com.dading.ssqs.components.NoScrollViewPager;
import com.google.gson.Gson;


import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/23 14:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StoreActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "StoreActivity";
    @Bind(R.id.store_info_top_ly)
    RelativeLayout mStoreInfoTopLy;
    @Bind(R.id.store_info_ly)
    RelativeLayout mStoreInfoLy;
    @Bind(R.id.store_iv_photo)
    ImageView mStoreIvPhoto;
    @Bind(R.id.store_tv_nikname)
    TextView mStoreTvNikname;
    @Bind(R.id.store_glod_num)
    TextView mStoreGlodNum;
    @Bind(R.id.store_dimonds_num)
    TextView mStoreDimondsNum;
    @Bind(R.id.store_rg)
    RadioGroup mStoreRg;
    @Bind(R.id.store_rg_diamonds)
    RadioButton mStoreRgDiamonds;
    @Bind(R.id.store_rg_prop)
    RadioButton mStoreRgProp;
    @Bind(R.id.store_viewpager)
    NoScrollViewPager mStoreViewpager;
    @Bind(R.id.store_rg_vip)
    RadioButton mStoreRgVip;
    @Bind(R.id.store_rg_turntable)
    RadioButton mStoreRgTurntable;
    @Bind(R.id.store_rg_prize)
    RadioButton mStoreRgPrize;
    @Bind(R.id.top_title)
    TextView mTopTitle;


    private StoreRecevice mRecevice;

    // 签约合作者身份ID
    public static final String PARTNER = "2088421875553442";
    // 商户收款账号
    public static final String SELLER = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    public final String ALIPAY_PACKGE_NAME = "com.eg.android.AlipayGphone";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    Logger.d(TAG, "支付宝成功数据是getResult----------" + payResult.getResult());
                    if (TextUtils.isEmpty(payResult.getResult())) {
                        return;
                    }
                    UIUtils.SendReRecevice(Constent.SERIES);
                    final AlipaySucBean sucBean = JSON.parseObject(payResult.getResult(), AlipaySucBean.class);
                    if (sucBean != null)
                        Logger.d(TAG, "支付宝成功数据订单号是----------" + sucBean.alipay_trade_app_pay_response.out_trade_no);
                    String resultStatus = payResult.getResultStatus();
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
                    if (sucBean != null) {
                        element.setOrderID(sucBean.alipay_trade_app_pay_response.out_trade_no);
                    }
                    element.setStatus(resultStatus);

                    SSQSApplication.apiClient(classGuid).orderStausUpload(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                Logger.d(TAG, "支付结果提交返回数据是-----:" + result.getMessage());
                            } else {
                                Logger.d(TAG, result.getMessage() + "失败信息");
                            }
                        }
                    });

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        Toast.makeText(StoreActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(StoreActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(StoreActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private ArrayList<View> mList;
    private ArrayList<Bitmap> mListBit;
    private StoreTurnTableControllar mTurnTable;
    private StorePrizeControllar mPrize;


    public void Alipay(String order) {
        Logger.d(TAG, "开始拉起支付------------------------------" + order);
        final String payInfo = order;
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(StoreActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //判断是否安装支付宝 传入包名
    public boolean CheckInstall(String packageName) {
        boolean checkResult = false;
        try {
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(packageName, 0);
            checkResult = packageInfo != null;
        } catch (Exception e) {
            checkResult = false;
        }
        return checkResult;
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        UIUtils.UnReRecevice(mRecevice);
    }

    private void processedData(LoadingBean bean) {
        if (bean != null) {
            //显示图片的配置

            SSQSApplication.glide.load(bean.avatar).error(R.mipmap.nologinportrait).centerCrop().transform(new GlideCircleTransform(this)).into(mStoreIvPhoto);

            if (TextUtils.isEmpty(bean.avatar))
                switch (bean.sex) {
                    case 1:
                        mStoreIvPhoto.setImageResource(R.mipmap.touxiang_nan);
                        break;
                    case 2:
                        mStoreIvPhoto.setImageResource(R.mipmap.touxiang_nv);
                        break;
                    case 3:
                        mStoreIvPhoto.setImageResource(R.mipmap.touxiang_baomi);
                        break;
                    default:
                        break;
                }
            String diamond = bean.diamond + "";
            mStoreDimondsNum.setText(diamond);
            String glod = bean.banlance + "";
            mStoreGlodNum.setText(glod);
            mStoreTvNikname.setText(bean.username);
        }
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.store));
        mListBit = new ArrayList<>();
        int px = DensityUtil.dip2px(StoreActivity.this, 130);
        int px2 = DensityUtil.dip2px(StoreActivity.this, 70);
        mRecevice = new StoreRecevice();
        //注册广播

        UIUtils.ReRecevice(mRecevice, Constent.SERIES);

        UIUtils.ReRecevice(mRecevice, Constent._SHOPING_GLOD);

        UIUtils.ReRecevice(mRecevice, Constent.TURN_TABLE);

        mList = new ArrayList<>();

        StoreDiamondsControllar diamonds = new StoreDiamondsControllar(this);
        StorePropControllar prop = new StorePropControllar(this);
        StoreVipControllar vip = new StoreVipControllar(this);
        mTurnTable = new StoreTurnTableControllar(this);
        mPrize = new StorePrizeControllar(this);

        mList.add(diamonds.mRootView);
        mList.add(prop.mRootView);
        mList.add(vip.mRootView);
        mList.add(mTurnTable.mRootView);
        mList.add(mPrize.mRootView);

        mStoreViewpager.setAdapter(new MyStoreAdapter(this, mList));

        Intent intent = getIntent();
        String flag = intent.getStringExtra(Constent.DIAMONDS);
        Logger.d(TAG, "跳转flag数据是------------------------------:" + flag);
        if ("2".equals(flag)) {
            mStoreViewpager.setCurrentItem(1);
            mStoreRgProp.setChecked(true);
        } else if (flag == null) {
            mStoreViewpager.setCurrentItem(0);
            mStoreRgDiamonds.setChecked(true);
        } else if (Constent.TURN_TABLE.equals(flag)) {
            mStoreViewpager.setCurrentItem(3);
            mStoreRgTurntable.setChecked(true);
        }

        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mStoreInfoLy.setVisibility(View.VISIBLE);
            mStoreInfoTopLy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px));

            SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {
                        LoadingBean bean = (LoadingBean) result.getData();

                        if (bean != null) {
                            Gson gson = new Gson();

                            UIUtils.getSputils().putString(Constent.LOADING_STATE_SP, gson.toJson(bean, LoadingBean.class));

                            processedData(bean);
                        }
                    } else {
                        if (403 == result.getErrno()) {
                            UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                            Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                        }
                    }
                }
            });
        } else {
            mStoreInfoLy.setVisibility(View.GONE);
            mStoreInfoTopLy.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px2));
        }

    }

    @Override
    protected void initListener() {
        super.initListener();
        mStoreRg.setOnCheckedChangeListener(this);
    }


    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.store_rg_diamonds:
                mStoreViewpager.setCurrentItem(0);
                mStoreRgDiamonds.setTextColor(Color.parseColor("#F54234"));
                mStoreRgPrize.setTextColor(Color.WHITE);
                mStoreRgProp.setTextColor(Color.WHITE);
                mStoreRgTurntable.setTextColor(Color.WHITE);
                mStoreRgVip.setTextColor(Color.WHITE);

                break;
            case R.id.store_rg_prop:
                mStoreViewpager.setCurrentItem(1);
                mStoreRgDiamonds.setTextColor(Color.WHITE);
                mStoreRgPrize.setTextColor(Color.WHITE);
                mStoreRgProp.setTextColor(Color.parseColor("#F54234"));
                mStoreRgTurntable.setTextColor(Color.WHITE);
                mStoreRgVip.setTextColor(Color.WHITE);
                break;
            case R.id.store_rg_vip:
                mStoreViewpager.setCurrentItem(2);
                mStoreRgDiamonds.setTextColor(Color.WHITE);
                mStoreRgPrize.setTextColor(Color.WHITE);
                mStoreRgProp.setTextColor(Color.WHITE);
                mStoreRgTurntable.setTextColor(Color.WHITE);
                mStoreRgVip.setTextColor(Color.parseColor("#F54234"));
                break;
            case R.id.store_rg_turntable:
                mStoreViewpager.setCurrentItem(3);
                mStoreRgDiamonds.setTextColor(Color.WHITE);
                mStoreRgPrize.setTextColor(Color.WHITE);
                mStoreRgProp.setTextColor(Color.WHITE);
                mStoreRgTurntable.setTextColor(Color.parseColor("#F54234"));
                mStoreRgVip.setTextColor(Color.WHITE);
                break;
            case R.id.store_rg_prize:
                mStoreViewpager.setCurrentItem(4);
                mStoreRgDiamonds.setTextColor(Color.WHITE);
                mStoreRgPrize.setTextColor(Color.parseColor("#F54234"));
                mStoreRgProp.setTextColor(Color.WHITE);
                mStoreRgTurntable.setTextColor(Color.WHITE);
                mStoreRgVip.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    private class StoreRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case Constent.SERIES:
                    Logger.d(TAG, "进入商城广播------------------------------");
                    SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    UIUtils.getSputils().putString(Constent.TOKEN, bean.authToken);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);

                                    UIUtils.getSputils().putString(Constent.GLODS, bean.banlance + "");
                                    UIUtils.getSputils().putString(Constent.DIAMONDS, bean.diamond + "");

                                    String dimons = bean.diamond + "";
                                    mStoreDimondsNum.setText(dimons);
                                    String glod = bean.banlance + "";
                                    mStoreGlodNum.setText(glod);
                                    //发送广播
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(StoreActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                    break;
                case Constent._SHOPING_GLOD:
                    Logger.d(TAG, "收到充值广播");
                    mStoreViewpager.setCurrentItem(1);
                    mStoreRgProp.setChecked(true);
                    break;
                default:
                    break;
            }
        }
    }
}
