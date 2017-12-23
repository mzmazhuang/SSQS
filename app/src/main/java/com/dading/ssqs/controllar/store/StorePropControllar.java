package com.dading.ssqs.controllar.store;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.DiamondExchangeElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.OrderBean;
import com.dading.ssqs.bean.StoreBean;
import com.dading.ssqs.bean.WXOrderBean;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/10 14:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class StorePropControllar implements View.OnClickListener {
    private static final String TAG = "StoreDiamondsControllar";
    private static final String APP_ID = "wxf03354b453566347";
    private final Context context;
    public View mRootView;
    @Bind(R.id.store_prop1)
    TextView mStoreProp1;
    @Bind(R.id.store_prop2)
    TextView mStoreProp2;
    @Bind(R.id.store_prop3)
    TextView mStoreProp3;
    @Bind(R.id.buy_prop12)
    TextView mBuy1;
    @Bind(R.id.buy_prop30)
    TextView mBuy2;
    @Bind(R.id.buy_prop68)
    TextView mBuy3;
    @Bind(R.id.store_prop4)
    TextView mStoreProp4;
    @Bind(R.id.store_prop5)
    TextView mStoreProp5;
    @Bind(R.id.store_prop6)
    TextView mStoreProp6;
    @Bind(R.id.buy_prop128)
    TextView mBuy4;
    @Bind(R.id.buy_prop328)
    TextView mBuy5;
    @Bind(R.id.buy_prop648)
    TextView mBuy6;
    @Bind(R.id.store_prop7)
    TextView mStoreProp7;
    @Bind(R.id.store_prop8)
    TextView mStoreProp8;
    @Bind(R.id.store_prop9)
    TextView mStoreProp9;
    @Bind(R.id.buy_prop1000)
    TextView mBuy7;
    @Bind(R.id.buy_prop3000)
    TextView mBuy8;
    @Bind(R.id.buy_prop5000)
    TextView mBuy9;
    @Bind(R.id.store_prop_remark1)
    TextView mStorePropRemark1;
    @Bind(R.id.store_prop_remark2)
    TextView mStorePropRemark2;
    @Bind(R.id.store_prop_remark3)
    TextView mStorePropRemark3;
    @Bind(R.id.store_prop_remark4)
    TextView mStorePropRemark4;
    @Bind(R.id.store_prop_remark5)
    TextView mStorePropRemark5;
    @Bind(R.id.store_prop_remark6)
    TextView mStorePropRemark6;
    @Bind(R.id.store_prop_remark7)
    TextView mStorePropRemark7;
    @Bind(R.id.store_prop_remark8)
    TextView mStorePropRemark8;
    @Bind(R.id.store_prop_remark9)
    TextView mStorePropRemark9;
    @Bind(R.id.store_prop1_img)
    ImageView mStoreProp1Img;
    @Bind(R.id.store_prop2_img)
    ImageView mStoreProp2Img;
    @Bind(R.id.store_prop3_img)
    ImageView mStoreProp3Img;
    @Bind(R.id.store_prop4_img)
    ImageView mStoreProp4Img;
    @Bind(R.id.store_prop5_img)
    ImageView mStoreProp5Img;
    @Bind(R.id.store_prop6_img)
    ImageView mStoreProp6Img;
    @Bind(R.id.store_prop7_img)
    ImageView mStoreProp7Img;
    @Bind(R.id.store_prop8_img)
    ImageView mStoreProp8Img;
    @Bind(R.id.store_prop9_img)
    ImageView mStoreProp9Img;
    @Bind(R.id.buy_prop_ly1)
    LinearLayout mBuyPropLy1;
    @Bind(R.id.buy_prop_ly2)
    LinearLayout mBuyPropLy2;
    @Bind(R.id.buy_prop_ly3)
    LinearLayout mBuyPropLy3;
    @Bind(R.id.buy_prop_ly4)
    LinearLayout mBuyPropLy4;
    @Bind(R.id.buy_prop_ly5)
    LinearLayout mBuyPropLy5;
    @Bind(R.id.buy_prop_ly6)
    LinearLayout mBuyPropLy6;
    @Bind(R.id.buy_prop_ly7)
    LinearLayout mBuyPropLy7;
    @Bind(R.id.buy_prop_ly8)
    LinearLayout mBuyPropLy8;
    @Bind(R.id.buy_prop_ly9)
    LinearLayout mBuyPropLy9;
    @Bind(R.id.line_1_prop)
    LinearLayout mLine1Prop;
    @Bind(R.id.line_1_prop_price)
    LinearLayout mLine1PropPrice;
    @Bind(R.id.line_2_prop)
    LinearLayout mLine2Prop;
    @Bind(R.id.line_2_prop_price)
    LinearLayout mLine2PropPrice;
    @Bind(R.id.line_3_prop)
    LinearLayout mLine3Prop;
    @Bind(R.id.line_3_prop_price)
    LinearLayout mLine3PropPrice;


    private View mView;

    private View mPopview;
    private RelativeLayout mPopBg;
    private ImageView mPayMethodClose;
    private TextView mPayMethodRmb;
    private TextView mPayMethodGlod;
    private RelativeLayout mPayMethodWxPay;
    private RelativeLayout mPayMethodAliyPay;
    private PopupWindow mPopu;
    private ArrayList<TextView> mGold;
    private ArrayList<TextView> mRemark;
    private ArrayList<TextView> mPrice;
    private ArrayList<ImageView> mImg;
    private List<StoreBean> mData;
    private int mId;
    private IWXAPI mWxapi;
    private boolean mInstall;
    private ArrayList<LinearLayout> Ly;
    private TextView mTextView;
    private ArrayList<LinearLayout> mListLine;
    private ArrayList<LinearLayout> mListLinePrice;

    public StorePropControllar(Context context) {
        this.context = context;
        mRootView = initView(context);
        initData();
        initListener();
    }

    private View initView(Context context) {
        mView = View.inflate(context, R.layout.store_prop, null);
        ButterKnife.bind(this, mView);

        mPopview = View.inflate(context, R.layout.store_diamonds_pop, null);
        mPopBg = ButterKnife.findById(mPopview, R.id.pop_bg);
        mPayMethodClose = ButterKnife.findById(mPopview, R.id.pay_method_close);
        mPayMethodRmb = ButterKnife.findById(mPopview, R.id.pay_method_rmb);
        mPayMethodGlod = ButterKnife.findById(mPopview, R.id.pay_method_glod);
        mPayMethodWxPay = ButterKnife.findById(mPopview, R.id.pay_method_wx_pay);
        mPayMethodAliyPay = ButterKnife.findById(mPopview, R.id.pay_method_aliy_pay);
        return mView;
    }

    private void initData() {
        mWxapi = WXAPIFactory.createWXAPI(context, APP_ID, true);
        mWxapi.registerApp(APP_ID);

        mListLine = new ArrayList<>();
        mListLine.add(mLine1Prop);
        mListLine.add(mLine2Prop);
        mListLine.add(mLine3Prop);

        mListLinePrice = new ArrayList<>();
        mListLinePrice.add(mLine1PropPrice);
        mListLinePrice.add(mLine2PropPrice);
        mListLinePrice.add(mLine3PropPrice);

        Ly = new ArrayList<>();
        Ly.add(mBuyPropLy1);
        Ly.add(mBuyPropLy2);
        Ly.add(mBuyPropLy3);
        Ly.add(mBuyPropLy4);
        Ly.add(mBuyPropLy5);
        Ly.add(mBuyPropLy6);
        Ly.add(mBuyPropLy7);
        Ly.add(mBuyPropLy8);
        Ly.add(mBuyPropLy9);

        mImg = new ArrayList<>();
        mImg.add(mStoreProp1Img);
        mImg.add(mStoreProp2Img);
        mImg.add(mStoreProp3Img);
        mImg.add(mStoreProp4Img);
        mImg.add(mStoreProp5Img);
        mImg.add(mStoreProp6Img);
        mImg.add(mStoreProp7Img);
        mImg.add(mStoreProp8Img);
        mImg.add(mStoreProp9Img);

        mGold = new ArrayList<>();
        mGold.add(mStoreProp1);
        mGold.add(mStoreProp2);
        mGold.add(mStoreProp3);
        mGold.add(mStoreProp4);
        mGold.add(mStoreProp5);
        mGold.add(mStoreProp6);
        mGold.add(mStoreProp7);
        mGold.add(mStoreProp8);
        mGold.add(mStoreProp9);

        mRemark = new ArrayList<>();
        mRemark.add(mStorePropRemark1);
        mRemark.add(mStorePropRemark2);
        mRemark.add(mStorePropRemark3);
        mRemark.add(mStorePropRemark4);
        mRemark.add(mStorePropRemark5);
        mRemark.add(mStorePropRemark6);
        mRemark.add(mStorePropRemark7);
        mRemark.add(mStorePropRemark8);
        mRemark.add(mStorePropRemark9);

        mPrice = new ArrayList<>();
        mPrice.add(mBuy1);
        mPrice.add(mBuy2);
        mPrice.add(mBuy3);
        mPrice.add(mBuy4);
        mPrice.add(mBuy5);
        mPrice.add(mBuy6);
        mPrice.add(mBuy7);
        mPrice.add(mBuy8);
        mPrice.add(mBuy9);


        /**
         1.	奖品列表
         a)	请求地址：
         /v1.0/award/type/{type}
         b)	请求方式:
         get
         c)	请求参数说明：
         type：奖品类型
         1-道具2-转盘3-奖品4-钻石5-Vip
         d)	返回格式
         1，4，5类型格式
         */

        SSQSApplication.apiClient(0).getAwardType(1, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<StoreBean> items = (List<StoreBean>) result.getData();

                    if (items != null) {
                        mData = items;
                        processData(mData);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "商店道具失败信息");
                }
            }
        });
    }

    private void processData(List<StoreBean> bean) {
        switch (bean.size()) {
            case 1:
            case 2:
            case 3:
                mListLine.get(0).setVisibility(View.VISIBLE);
                mListLinePrice.get(0).setVisibility(View.VISIBLE);
                mListLine.get(1).setVisibility(View.GONE);
                mListLinePrice.get(1).setVisibility(View.GONE);
                mListLine.get(1).setVisibility(View.GONE);
                mListLinePrice.get(1).setVisibility(View.GONE);
                break;
            case 4:
            case 5:
            case 6:
                mListLine.get(0).setVisibility(View.VISIBLE);
                mListLinePrice.get(0).setVisibility(View.VISIBLE);
                mListLine.get(1).setVisibility(View.VISIBLE);
                mListLinePrice.get(1).setVisibility(View.VISIBLE);
                mListLine.get(2).setVisibility(View.GONE);
                mListLinePrice.get(2).setVisibility(View.GONE);
                break;
            case 7:
            case 8:
            case 9:
                mListLine.get(0).setVisibility(View.VISIBLE);
                mListLinePrice.get(0).setVisibility(View.VISIBLE);
                mListLine.get(1).setVisibility(View.VISIBLE);
                mListLinePrice.get(1).setVisibility(View.VISIBLE);
                mListLine.get(2).setVisibility(View.VISIBLE);
                mListLinePrice.get(2).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        for (int i = 0; i < Ly.size(); i++) {
            Ly.get(i).setVisibility(View.VISIBLE);
            mPrice.get(i).setVisibility(View.VISIBLE);
            if (i < bean.size()) {
                Ly.get(i).setClickable(true);
                mPrice.get(i).setClickable(true);

                mGold.get(i).setText(bean.get(i).name);
                String price = "¥" + bean.get(i).cost + "元";
                mPrice.get(i).setText(price);
                mRemark.get(i).setText(bean.get(i).remark);

                SSQSApplication.glide.load(bean.get(i).itemImageUrl).error(R.mipmap.fail).centerCrop().into(mImg.get(i));
            } else {
                Ly.get(i).setClickable(false);
                mPrice.get(i).setClickable(false);

                mRemark.get(i).setText("");
                mPrice.get(i).setText("");
                mRemark.get(i).setText("");
                Ly.get(i).setBackground(null);
                mPrice.get(i).setBackground(null);
            }
        }
    }

    private void initListener() {
        mPayMethodClose.setOnClickListener(this);
        mPopBg.setOnClickListener(this);
        mPayMethodWxPay.setOnClickListener(this);
        mPayMethodAliyPay.setOnClickListener(this);
    }

    @OnClick({R.id.buy_prop12, R.id.buy_prop30, R.id.buy_prop68, R.id.buy_prop128, R.id.buy_prop328, R.id.buy_prop648, R.id.buy_prop1000, R.id.buy_prop3000,
            R.id.buy_prop5000, R.id.buy_prop_ly1, R.id.buy_prop_ly2, R.id.buy_prop_ly3, R.id.buy_prop_ly4, R.id.buy_prop_ly5, R.id.buy_prop_ly6, R.id.buy_prop_ly7,
            R.id.buy_prop_ly8, R.id.buy_prop_ly9})
    public void OnClik(View v) {
        String price = "0";
        switch (v.getId()) {
            case R.id.buy_prop12:
            case R.id.buy_prop_ly1:
                if (mData != null && mData.size() > 0) {
                    mId = 0;
                    price = mData.get(0).cost;
                }
                break;
            case R.id.buy_prop30:
            case R.id.buy_prop_ly2:
                if (mData != null && mData.size() > 1) {
                    mId = 1;
                    price = mData.get(1).cost;
                }
                break;
            case R.id.buy_prop68:
            case R.id.buy_prop_ly3:
                if (mData != null && mData.size() > 2) {
                    mId = 2;
                    price = mData.get(2).cost;
                }
                break;
            case R.id.buy_prop128:
            case R.id.buy_prop_ly4:
                if (mData != null && mData.size() > 3) {
                    price = mData.get(3).cost;
                    mId = 3;
                }
                break;
            case R.id.buy_prop328:
            case R.id.buy_prop_ly5:
                if (mData != null && mData.size() > 4) {
                    price = mData.get(4).cost;
                    mId = 4;
                }
                break;
            case R.id.buy_prop648:
            case R.id.buy_prop_ly6:
                if (mData != null && mData.size() > 5) {
                    price = mData.get(5).cost;
                    mId = 5;
                }
                break;
            case R.id.buy_prop1000:
            case R.id.buy_prop_ly7:
                if (mData != null && mData.size() > 6) {
                    price = mData.get(6).cost;
                    mId = 6;
                }
                break;
            case R.id.buy_prop3000:
            case R.id.buy_prop_ly8:
                if (mData != null && mData.size() > 7) {
                    price = mData.get(7).cost;
                    mId = 7;
                }
                break;
            case R.id.buy_prop5000:
            case R.id.buy_prop_ly9:
                if (mData != null && mData.size() > 8) {
                    price = mData.get(8).cost;
                    mId = 8;
                }
                break;
            default:
                break;
        }
        if ("0".equals(price)) {
            return;
        }
        mPopu = PopUtil.popuMake(mPopview);
        String s = "¥" + price + "元";
        mPayMethodRmb.setText(s);
        String s1 = price + "金币";
        mPayMethodGlod.setText(s1);
        int i = DensityUtil.dip2px(context, 50);
        int j = DensityUtil.dip2px(context, 200);
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mPopu.showAtLocation(mView, Gravity.CENTER, j, i);
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
            ((StoreActivity) context).finish();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_method_close:
                mPopu.dismiss();
                break;
            case R.id.pop_bg:
                mPopu.dismiss();
                break;
            case R.id.pay_method_wx_pay:
                //调用微信
                Logger.d(TAG, "调用微信");
                WXOrder();
                mPayMethodWxPay.setClickable(false);
                break;
            case R.id.pay_method_aliy_pay:
                //调用支付宝
                if (Build.MODEL.contains("OPPO")) {
                    mInstall = ((StoreActivity) context).CheckInstall(((StoreActivity) context).ALIPAY_PACKGE_NAME);
                    if (!mInstall) {
                        TmtUtils.midToast(context, "您还没有安装支付宝,请更换支付方式!", 0);
                        return;
                    }
                }
                mPayMethodAliyPay.setClickable(false);
                Logger.d(TAG, "调用支付宝");
                getPay();
                break;

            default:
                break;
        }
    }

    private void WXOrder() {
        /**
         a)	请求地址：
         /v1.0/awardExchange/props
         b)	请求方式:
         post
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         id：奖品ID
         payType: 1:支付包2:微信3:苹果支付
         */

        DiamondExchangeElement element = new DiamondExchangeElement();
        element.setId(String.valueOf(mData.get(mId).id));
        element.setPayType("2");

        SSQSApplication.apiClient(0).awardExchangeProps(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    WXOrderBean bean = (WXOrderBean) result.getData();

                    if (bean != null) {
                        sendWXOrder(bean);
                        mPayMethodWxPay.setClickable(true);
                        mPopu.dismiss();
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        TmtUtils.midToast(context, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void sendWXOrder(WXOrderBean data) {
        PayReq req = new PayReq();
        req.appId = data.appid;
        req.partnerId = data.partnerid;
        req.prepayId = data.prepayid;
        req.nonceStr = data.noncestr;
        req.timeStamp = data.timestamp;
        req.packageValue = data.packages;
        req.sign = data.sign;
        req.extData = "app data";
        UIUtils.getSputils().putString(Constent.WX, data.orderID);
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        mWxapi.sendReq(req);
    }

    private void getPay() {
        /**
         a)	请求地址：
         /v1.0/awardExchange/props
         b)	请求方式:
         post
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         id：奖品ID
         payType: 1:支付包2:微信3:苹果支付
         */
        DiamondExchangeElement element = new DiamondExchangeElement();
        element.setId(String.valueOf(mData.get(mId).id));
        element.setId("1");

        SSQSApplication.apiClient(0).awardExchangeProps2(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    OrderBean bean = (OrderBean) result.getData();

                    if (bean != null) {
                        ((StoreActivity) context).Alipay(bean.data);
                        mPayMethodAliyPay.setClickable(true);
                        mPopu.dismiss();
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    } else {
                        TmtUtils.midToast(context, result.getMessage(), 0);
                    }
                }
            }
        });
    }
}
